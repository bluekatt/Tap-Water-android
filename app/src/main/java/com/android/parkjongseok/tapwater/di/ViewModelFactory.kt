package com.android.parkjongseok.tapwater.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.MapKey
import javax.inject.Inject
import javax.inject.Provider
import kotlin.reflect.KClass

class ViewModelFactory @Inject constructor(
    val viewModelMap: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
): ViewModelProvider.Factory {
    override fun <T: ViewModel?> create(modelClass: Class<T>): T {
        return viewModelMap[modelClass]?.get() as T
    }
}

@MapKey
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
annotation class ViewModelKey(val value: KClass<out ViewModel>)
