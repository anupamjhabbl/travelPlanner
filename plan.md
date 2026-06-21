# Exhaustive Error Handling Implementation Plan

This plan maps every composable screen in the Travel Planner app to its potential error scenarios and defines the handling strategy following the project's MVI architecture and localized strings.

---

## 1. Core Principles
- **State-Driven Errors**: For initial data fetches. UI: `FullScreenErrorComposable` with a retry option.
- **Effect-Driven Errors**: For user actions (save, delete, update). UI: `showToast` or Snackbar.
- **Localization**: All error messages must be pulled from `strings.xml`.
- **No Base Changes**: Do not modify `BaseMVIVViewModel`. Implement specific states/effects in each ViewModel.

---

## 2. Comprehensive Screen & Error List

### A. Authentication Module
| File Name | Main Composable | API Call / Action | Error Scenarios | Handling Strategy |
| :--- | :--- | :--- | :--- | :--- |
| `AuthLoginScreen.kt` | `AuthLoginScreen` | `loginUser` | 401 (Global), Network Timeout. | Effect: Toast (`R.string.generic_error`) |
| `AuthRegistrationScreen.kt` | `AuthRegistrationScreen` | `registerUser` | Email exists, Validation fail. | State: Inline Error (`R.string.generic_error`) |
| `ForgotPasswordScreen.kt` | `ForgotPasswordScreen` | `resetPasswordLink` | Email not found. | Effect: Toast (`R.string.invalid_email_alert`) |
| `OTPVerificationScreen.kt` | `OTPVerificationScreen` | `verifyOTP` | Invalid OTP, Expired OTP. | State: Input Error (`R.string.otp_error`) |
| `PasswordResetScreen.kt` | `PasswordResetScreen` | `updatePassword` | Token invalid, Network fail. | Effect: Toast (`R.string.generic_error`) |
| `AuthenticationFormScreen.kt` | `AuthenticationFormScreen` | Form Validation | Field errors. | Local UI State |

### B. Home & Destination Module
| File Name | Main Composable | API Call / Action | Error Scenarios | Handling Strategy |
| :--- | :--- | :--- | :--- | :--- |
| `HomeExperienceScreen.kt` | `HomeExperienceScreen` | `getHomeCxeResponse` | CXE Service 500, Timeout. | State: `FullScreenErrorComposable` |
| `TripPlannerSearchScreen.kt` | `TripPlannerSearchScreen` | `searchTrips` | No results found. | State: Empty View (`R.string.no_result_found`) |
| `HomePageToolbar.kt` | `HomePageToolbar` | UI / User Data | Profile fetch fail. | Silent fail (Hide profile icon) |
| `DestinationScreen.kt` | `DestinationScreen` | `getDestination` | Destination 404. | State: `FullScreenErrorComposable` |

### C. User Profile & Account Module
| File Name | Main Composable | API Call / Action | Error Scenarios | Handling Strategy |
| :--- | :--- | :--- | :--- | :--- |
| `MyAccountView.kt` | `MyAccountView` | `fetchUserData` | Auth expire, Fetch fail. | State: `FullScreenErrorComposable` |
| `ProfileScreen.kt` | `ProfileScreen` | `getUserProfile` | Profile removed, No Network. | State: `FullScreenErrorComposable` |
| `EditProfileScreen.kt` | `EditProfileScreen` | `updateProfile` | Update conflict, Image fail. | Effect: Toast (`R.string.photos_save_failed`) |
| `BlockedUsersScreen.kt` | `BlockedUsersScreen` | `unblockUser` | Action failure. | Effect: Toast (`R.string.block_failure`) |
| `ProfileSocialScreen.kt` | `ProfileSocialScreen` | `getFollowCounts` | Fetch fail. | Silent fail (Show 0) |
| `ProfileSocialListScreen.kt` | `ProfileSocialListScreen` | `getSocialList` | List fetch fail. | State: `FullScreenErrorComposable` |
| `HelpSupportScreen.kt` | `HelpSupportScreen` | FAQ Fetch | CMS Service error. | State: `FullScreenErrorComposable` |
| `UserSettingsScreen.kt` | `UserSettingsScreen` | Update Prefs | Save fail. | Effect: Toast (`R.string.generic_error`) |

### D. User Trips & Itinerary Module
| File Name | Main Composable | API Call / Action | Error Scenarios | Handling Strategy |
| :--- | :--- | :--- | :--- | :--- |
| `UserTripDetailScreen.kt` | `UserTripDetailScreen` | `getTripDetail` | Trip deleted, Private access (404), Fetch fail. | State: `FullScreenErrorComposable` (404 -> `R.string.nothing_to_show`) |
| `TripGroupScreen.kt` | `TripGroupScreen` | `getMembers` | Network failure. | State: Retry Layout |
| `ItineraryListView.kt` | `ItineraryListView` | `getItinerary` | Sync issue, Fetch fail. | State: `FullScreenErrorComposable` |
| `ItineraryDetailView.kt` | `ItineraryDetailView` | `getActivityDetail` | Item removed. | Effect: Toast + Navigate Back |
| `ItineraryMapView.kt` | `ItineraryMapView` | Map Service | GPS / Load Failure. | Effect: Toast |
| `TripExpensesScreen.kt` | `TripExpensesScreen` | `getExpenses` | Load fail, Calculation fail. | State: `FullScreenErrorComposable` |
| `AddExpensesScreen.kt` | `AddExpensesScreen` | `addExpense` | Network failure during POST. | Effect: Toast (`R.string.provide_details`) |
| `ExpenseSettlementScreen.kt` | `ExpenseSettlementScreen` | `getSettlements` | Fetch fail. | State: Empty View (`R.string.settlement_not_available_description`) |
| `TripGalleryScreen.kt` | `TripGalleryScreen` | `uploadMedia` | Timeout, Partial upload. | State: Item Retry Icon (`R.string.photos_save_failed`) |
| `PostingInitScreen.kt` | `PostingInitScreen` | `createTrip` | Validation error. | Effect: Toast (`R.string.trip_name_please`) |
| `PostingEditScreen.kt` | `PostingEditScreen` | `updateTrip` | Save error. | Effect: Toast (`R.string.generic_error`) |
| `InviteBottomSheet.kt` | `InviteBottomSheet` | `sendInvite` | Network fail. | Effect: Toast |
| `LocationBottomSheet.kt` | `LocationBottomSheet` | `searchLocation` | API Limit / No results. | State: Inline message |

### E. Buzz, Vault & Notification Modules
| File Name | Main Composable | API Call / Action | Error Scenarios | Handling Strategy |
| :--- | :--- | :--- | :--- | :--- |
| `BuzzScreen.kt` | `BuzzScreen` | `getFeeds` | Social service 500. | State: `FullScreenErrorComposable` |
| `UserVaultScreen.kt` | `UserVaultScreen` | `getVaultItems` | Vault lock / Fetch fail. | State: `FullScreenErrorComposable` |
| `UserTripsScreen.kt` | `UserTripsScreen` | `getUserTrips` | No trips found. | State: Empty View (`R.string.noting_to_show_subtitle`) |
| `NotificationScreen.kt` | `NotificationScreen` | `getNotifications` | Fetch fail. | State: Empty View (`R.string.no_notifications_to_show`) |

---

## 3. Session Expiry (401/403 Error) Handling
**Status: Globally Handled**
Session expiry is automatically managed by `TokenAuthenticator.kt`. When an API returns a 401/403:
1. `TokenAuthenticator` clears tokens and triggers the global "Session Expired" popup.
2. **ViewModel Responsibility**: Simply ensure `onFailure` stops loading indicators (e.g., `isLoading = false`) to allow the global UI to appear.

---

## 4. Possible Error Scenarios Checklist
- **Network Level**: `IOException` (No Internet) -> Show `R.string.no_internet_connection`.
- **Server Level**: `5xx` (Internal Error) -> Show `R.string.server_error`.
- **Data Level**: `404` (Not Found) -> Show `R.string.nothing_to_show`.
- **Action Level**: `Timeout` or `400` -> Show `showToast(R.string.generic_error)`.
- **Empty States**: Successful fetch but 0 items -> Show appropriate `EmptyState` UI.
