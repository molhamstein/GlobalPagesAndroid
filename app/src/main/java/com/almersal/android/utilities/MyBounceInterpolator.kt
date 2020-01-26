package com.almersal.android.utilities

import android.view.animation.Interpolator
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.pow

internal class MyBounceInterpolator (val bounces: Int, val energy: Double) : Interpolator {
    override fun getInterpolation(x: Float): Float =
        (1.0 + (-abs(cos(x * 10 * bounces / Math.PI)) * getCurveAdjustment(x))).toFloat()

    private fun getCurveAdjustment(x: Float): Double = -(2 * (1 - x) * x * energy + x * x) + 1
}
