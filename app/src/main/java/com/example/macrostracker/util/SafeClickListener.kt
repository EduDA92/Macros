package com.example.macrostracker.util

import android.os.SystemClock
import android.view.View

/* This implementation of Click listener will prevent the user from double tapping and crashing
* the app when opening dialog fragments, datepickers, etc. */

class SafeClickListener(
    private val onSafeClick: (View?) -> Unit
) : View.OnClickListener {

    private val interval: Int = 1000
    private var lastTimeClicked: Long = 0

    override fun onClick(p0: View?) {
        if (SystemClock.elapsedRealtime() - lastTimeClicked < interval) {
            return
        }
        lastTimeClicked = SystemClock.elapsedRealtime()
        onSafeClick(p0)
    }

}



fun View.setOnSafeClickListener(onSafeClick: (View?) -> Unit) {
    val safeClickListener = SafeClickListener {
        onSafeClick(it)
    }
    setOnClickListener(safeClickListener)
}