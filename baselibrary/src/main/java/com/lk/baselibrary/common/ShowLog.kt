package com.lk.baselibrary.common


import android.content.Context
import android.content.pm.ApplicationInfo
import android.util.Log

/**
 * Created by LK on 2017/10/15.
 */

//   static string threadName  = Thread.currentThread().getName();//线程名字，能判断主线程还是子线程
class ShowLog {
	companion object {
		private var className: String? = null//文件名
		private var methodName: String? = null//方法名
		private var lineNumber: Int = 0//行数
		
		private var isDebug: Boolean? = null
		
		private val isDebuggable: Boolean
			get() = isDebug == null || (!isDebug!!)
		
		fun syncIsDebug(context: Context) {
			if (isDebug == null) {
				isDebug =
						context.applicationInfo != null && context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0
			}
		}
		
		private fun getMethodNames(sElements: Array<StackTraceElement>) {
			className = sElements[1].fileName
			methodName = sElements[1].methodName
			lineNumber = sElements[1].lineNumber
		}
		
		private fun createLog(log: String): String {
			return methodName +
					"(" + className + ":" + lineNumber + ")" +
					log
		}
		
		//各种Log打印
		fun e(msg: String) {
			if (isDebuggable)
				return
			getMethodNames(Throwable().stackTrace)
			Log.e(className, createLog(msg))
			
		}
		
		fun i(msg: String) {
			if (isDebuggable)
				return
			getMethodNames(Throwable().stackTrace)
			Log.i(className, createLog(msg))
			
		}
		
		fun d(msg: String) {
			if (isDebuggable)
				return
			getMethodNames(Throwable().stackTrace)
			Log.d(className, createLog(msg))
			
		}
		
		fun w(msg: String) {
			if (isDebuggable)
				return
			getMethodNames(Throwable().stackTrace)
			Log.w(className, createLog(msg))
			
		}
		
		fun wtf(message: String) {
			if (isDebuggable)
				return
			
			getMethodNames(Throwable().stackTrace)
			Log.wtf(className, createLog(message))
		}
	}
	
}
