package com.baseandroid.baselibrary.utils.extension

import android.os.Build

val <T> T.exhaustive: T
    get() = this

fun isBuildLargerThan(versionCode: Int) = Build.VERSION.SDK_INT >= versionCode