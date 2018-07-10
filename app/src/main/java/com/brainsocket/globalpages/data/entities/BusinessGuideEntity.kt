package com.brainsocket.globalpages.data.entities

import android.support.annotation.ColorInt
import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import java.lang.reflect.Type

/**
 * Created by Adhamkh on 2018-06-28.
 */
class BusinessGuideEntity(@ColorInt var color: Int, @DrawableRes var icon: Int,
                          @StringRes var title: Int, @StringRes var details: Int)