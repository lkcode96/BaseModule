package com.lk.baselibrary.common

import android.app.Activity
import java.util.*


object AppManager {
    private val activityStack: Stack<Activity> = Stack()
    fun addActivity(activity: Activity?) {
        activityStack.add(activity!!)
    }

    fun finishActivity(activity: Activity?) {
        activityStack.remove(activity!!)
        activity.finish()
    }

    fun finishLastActivity() {
        finishActivity(activityStack.lastElement())
    }

    fun finishAllActivity(activity: Class<*>? = null) {
        if (activity != null) {
            activityStack.forEach {
                if (it.javaClass.canonicalName != activity.canonicalName) {
                    it.finish()
                }
            }
        } else {
            activityStack.forEach { it.finish() }
            activityStack.clear()
        }
    }

    fun appExit() {
        finishAllActivity()
    }
}