package com.example.bbltripplanner.screens.userTrip.viewModels

import androidx.lifecycle.viewModelScope
import com.example.bbltripplanner.common.Constants
import com.example.bbltripplanner.common.baseClasses.BaseMVIVViewModel
import com.example.bbltripplanner.common.entity.RequestResponseStatus
import com.example.bbltripplanner.common.entity.TripPlannerException
import com.example.bbltripplanner.common.utils.SafeIOUtil
import com.example.bbltripplanner.screens.userTrip.entity.AddExpenseRequest
import com.example.bbltripplanner.screens.userTrip.entity.Currency
import com.example.bbltripplanner.screens.userTrip.entity.ExpenseItem
import com.example.bbltripplanner.screens.userTrip.entity.ExpenseSummary
import com.example.bbltripplanner.screens.userTrip.entity.SettlementResponse
import com.example.bbltripplanner.screens.userTrip.entity.TripData
import com.example.bbltripplanner.screens.userTrip.entity.TripExpensesDetail
import com.example.bbltripplanner.screens.userTrip.usecases.ExpenseUseCase
import com.example.bbltripplanner.screens.userTrip.usecases.UserTripDetailUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class ExpenseViewModel(
    private val tripId: String?,
    private val expenseUseCase: ExpenseUseCase,
    private val userTripDetailUseCase: UserTripDetailUseCase
) : BaseMVIVViewModel<ExpenseIntent.ViewEvent>() {

    private val _expenseStatus = MutableStateFlow<RequestResponseStatus<TripExpensesDetail?>>(RequestResponseStatus())
    val expenseStatus: StateFlow<RequestResponseStatus<TripExpensesDetail?>> = _expenseStatus

    private val _settlementStatus = MutableStateFlow<RequestResponseStatus<SettlementResponse?>>(RequestResponseStatus())
    val settlementStatus: StateFlow<RequestResponseStatus<SettlementResponse?>> = _settlementStatus

    private val _tripData = MutableStateFlow<RequestResponseStatus<TripData>>(RequestResponseStatus())
    val tripData: StateFlow<RequestResponseStatus<TripData>> = _tripData

    private val _addExpenseStatus = Channel<ExpenseIntent.AddViewEffect>()
    val addExpenseStatus: Flow<ExpenseIntent.AddViewEffect> = _addExpenseStatus.receiveAsFlow()

    private val _deleteExpenseStatus = Channel<ExpenseIntent.DeleteViewEffect>()
    val deleteExpenseStatus: Flow<ExpenseIntent.DeleteViewEffect> = _deleteExpenseStatus.receiveAsFlow()

    init {
        tripId?.let {
            processEvent(ExpenseIntent.ViewEvent.FetchExpenses(it))
            fetchTripDetails(it)
        }
    }

    override fun processEvent(viewEvent: ExpenseIntent.ViewEvent) {
        when (viewEvent) {
            is ExpenseIntent.ViewEvent.FetchExpenses -> fetchExpenses(viewEvent.tripId)
            is ExpenseIntent.ViewEvent.InitiateBudget -> initiateBudget(viewEvent.tripId, viewEvent.budget, viewEvent.currency)
            is ExpenseIntent.ViewEvent.AddExpense -> addExpense(viewEvent.tripId, viewEvent.request)
            is ExpenseIntent.ViewEvent.FetchSettlements -> fetchSettlements()
            is ExpenseIntent.ViewEvent.DeleteExpense -> deleteExpense(viewEvent.expenseId)
        }
    }

    private fun fetchExpenses(tripId: String) {
        viewModelScope.launch {
            _expenseStatus.value = RequestResponseStatus(isLoading = true)
            SafeIOUtil.safeCall {
                expenseUseCase.getExpenses(tripId)
            }.onSuccess {
                _expenseStatus.value = RequestResponseStatus(data = it?.toTripExpenseDetail())
            }.onFailure {
                if (it is TripPlannerException) {
                    _expenseStatus.value = RequestResponseStatus(error = it.message)
                } else {
                    _expenseStatus.value = RequestResponseStatus(error = Constants.DEFAULT_ERROR)
                }
            }
        }
    }

    private fun initiateBudget(tripId: String, budget: Double, currency: Currency) {
        viewModelScope.launch {
            _expenseStatus.value = RequestResponseStatus(isLoading = true)
            SafeIOUtil.safeCall {
                expenseUseCase.initiateExpense(tripId, budget, currency)
            }.onSuccess {
                _expenseStatus.value = RequestResponseStatus(data = it?.toTripExpenseDetail())
            }.onFailure {
                if (it is TripPlannerException) {
                    _expenseStatus.value = RequestResponseStatus(error = it.message)
                } else {
                    _expenseStatus.value = RequestResponseStatus(error = Constants.DEFAULT_ERROR)
                }
            }
        }
    }

    private fun addExpense(tripId: String, request: AddExpenseRequest) {
        viewModelScope.launch {
            _addExpenseStatus.send(ExpenseIntent.AddViewEffect.AddExpenseLoading)
            SafeIOUtil.safeCall {
                expenseUseCase.addExpense(tripId, request)
            }.onSuccess { result ->
                if (result != null) {
                    _addExpenseStatus.send(ExpenseIntent.AddViewEffect.AddExpenseSuccess)
                    expenseStatus.value.data?.let { detail ->
                        _expenseStatus.value = expenseStatus.value.copy(
                            data = detail.copy(
                                expense = calculateTotal(detail.expenses.plus(result)),
                                left = calculateLeft(detail.budget, detail.expenses.plus(result)),
                                expenses = detail.expenses.plus(result)
                            )
                        )
                    }
                } else {
                    _addExpenseStatus.send(ExpenseIntent.AddViewEffect.AddExpenseError(Constants.DEFAULT_ERROR))
                }
            }.onFailure {
                if (it is TripPlannerException) {
                    _addExpenseStatus.send(ExpenseIntent.AddViewEffect.AddExpenseError(it.message ?: Constants.DEFAULT_ERROR))
                } else {
                    _addExpenseStatus.send(ExpenseIntent.AddViewEffect.AddExpenseError(Constants.DEFAULT_ERROR))
                }
            }
        }
    }

    private fun deleteExpense(expenseId: String) {
        viewModelScope.launch {
            _deleteExpenseStatus.send(ExpenseIntent.DeleteViewEffect.DeleteExpenseLoading)
            SafeIOUtil.safeCall {
                expenseUseCase.deleteExpense(expenseId)
            }.onSuccess {
                _deleteExpenseStatus.send(ExpenseIntent.DeleteViewEffect.DeleteExpenseSuccess)
                _expenseStatus.value = expenseStatus.value.copy(
                    data = expenseStatus.value.data?.let { detail ->
                        val updatedExpenses = detail.expenses.filter { it.id != expenseId }
                        detail.copy(
                            expense = calculateTotal(updatedExpenses),
                            left = calculateLeft(detail.budget, updatedExpenses),
                            expenses = updatedExpenses
                        )
                    }
                )
            }.onFailure {
                if (it is TripPlannerException) {
                    _deleteExpenseStatus.send(
                        ExpenseIntent.DeleteViewEffect.DeleteExpenseError(
                            it.message ?: Constants.DEFAULT_ERROR
                        )
                    )
                } else {
                    _deleteExpenseStatus.send(ExpenseIntent.DeleteViewEffect.DeleteExpenseError(Constants.DEFAULT_ERROR))
                }
            }
        }
    }

    private fun fetchSettlements() {
        viewModelScope.launch {
            _settlementStatus.value = RequestResponseStatus(isLoading = true)
            tripId?.let {
                SafeIOUtil.safeCall {
                    expenseUseCase.getSettlements(tripId)
                }.onSuccess {
                    _settlementStatus.value = RequestResponseStatus(data = it)
                }.onFailure {
                    if (it is TripPlannerException) {
                        _settlementStatus.value = RequestResponseStatus(error = it.message)
                    } else {
                        _settlementStatus.value =
                            RequestResponseStatus(error = Constants.DEFAULT_ERROR)
                    }
                }
            } ?: run {
                _settlementStatus.value =
                    RequestResponseStatus(error = Constants.DEFAULT_ERROR)
            }
        }
    }

    private fun fetchTripDetails(tripId: String) {
        viewModelScope.launch {
            _tripData.value = RequestResponseStatus(isLoading = true)
            runCatching {
                userTripDetailUseCase.getUserTripDetail(tripId)
            }.onSuccess {
                _tripData.value = RequestResponseStatus(data = it)
            }.onFailure {
                if (it is TripPlannerException) {
                    _tripData.value = RequestResponseStatus(error = it.message)
                } else {
                    _tripData.value = RequestResponseStatus(error = Constants.DEFAULT_ERROR)
                }
            }
        }
    }

    private fun ExpenseSummary.toTripExpenseDetail(): TripExpensesDetail {
        return TripExpensesDetail(
            budget,
            currencyCode,
            calculateTotal(expenses),
            calculateLeft(budget, expenses),
            expenses
        )
    }

    private fun calculateTotal(expenses: List<ExpenseItem>): Double {
        return expenses.sumOf { it.amount }
    }

    private fun calculateLeft(budget: Double, expenses: List<ExpenseItem>): Double {
        return budget - expenses.sumOf { it.amount }
    }
}
