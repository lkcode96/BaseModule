package com.lk.baselibrary.base

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * @author LK
 * @date 2019-12-31
 */
open class BaseRepository {

    suspend fun <T : Any> request(call: suspend () -> T): T {
        return withContext(Dispatchers.IO) { call.invoke() }
    }

}