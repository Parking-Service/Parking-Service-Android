package com.app.buna.foodplace.feature.common.base

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.reflect.KClass

open class BaseViewModel : ViewModel() {
    val activityToStart = MutableLiveData<Pair<KClass<*>, Bundle?>>()
    fun startActivity(className: KClass<*>, bundle: Bundle?) {
        activityToStart.value = Pair(className, bundle)
    }
}