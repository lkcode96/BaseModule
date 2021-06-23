package com.lk.baselibrary.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lk.baselibrary.common.ShowLog
import com.lk.baselibrary.utils.ResponseThrowable
import com.lk.baselibrary.base.ResponseData
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * @author LK
 * @date 2019-12-31
 */
open class BaseViewModule : ViewModel() {
    open val isLading = MutableLiveData<Boolean>()
    open val showEmpty = MutableLiveData<Boolean>()

    //运行在UI线程的协程
    fun launchUI(block: suspend CoroutineScope.() -> Unit) = viewModelScope.launch {
        try {
            block()
        } catch (e: Exception) {
            ShowLog.e(e.toString())
        }
    }

    /**
     * 过滤请求结果，其他全抛异常
     * @param block 请求体
     * @param success 成功回调
     * @param error 失败回调
     * @param complete  完成回调（无论成功失败都会调用）
     */
    fun <T> launchOnlyResult(
        block: suspend CoroutineScope.() -> ResponseData<T>,
        success: (T) -> Unit,
        error: (ResponseThrowable) -> Unit = {
            ShowLog.e(it.msg + "|" + it.code)
            if (it.code == 5106 || it.code == 5107) {
                //EventBus.getDefault().post(EventMessage(it.code, it.msg))
            }
        },
        complete: () -> Unit = { isLading.value = false }
    ) {
        launchUI {
            isLading.value = true
            handleException(
                { withContext(Dispatchers.IO) { block() } },
                { res -> executeResponse(res) { success(it) } },
                { error(it) },
                { complete() }
            )
        }
    }

    /**
     * 请求结果过滤
     */
    private suspend fun <T> executeResponse(
        response: ResponseData<T>,
        success: suspend CoroutineScope.(T) -> Unit
    ) {
        coroutineScope {
            if (response.code == 200) success(response.data)
            else throw ResponseThrowable(response.code, response.message)
        }
    }

    /**
     * 异常统一处理
     */
    private suspend fun <T> handleException(
        block: suspend CoroutineScope.() -> ResponseData<T>,
        success: suspend CoroutineScope.(ResponseData<T>) -> Unit,
        error: suspend CoroutineScope.(ResponseThrowable) -> Unit,
        complete: suspend CoroutineScope.() -> Unit
    ) {
        coroutineScope {
            try {
                success(block())
            } catch (e: ResponseThrowable) {
                error(ResponseThrowable(e.code, e.msg))
            } finally {
                complete()
            }
        }
    }

    /**
     * 用流的方式进行网络请求
     */
    fun <T> launchFlow(block: suspend () -> T): Flow<T> {
        return flow {
            emit(block())
        }
    }

}