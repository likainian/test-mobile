package com.fly.core.util

import android.widget.Toast
import com.fly.core.base.appContext

object ToastUtil {

    fun showToast(message: String?, duration: Int = Toast.LENGTH_LONG) {
        if (message == null) return
        val toast = Toast.makeText(appContext, message, duration)
        toast.show()
    }

    fun showToast(it: Throwable) {
        showToast(it.message)
    }
}
