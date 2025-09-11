package com.example.bbltripplanner.common.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object SafeIOUtil {
    suspend fun <T> safeCall(block: suspend () -> T): Result<T> =
        withContext(Dispatchers.IO) { runCatching { block() } }
}