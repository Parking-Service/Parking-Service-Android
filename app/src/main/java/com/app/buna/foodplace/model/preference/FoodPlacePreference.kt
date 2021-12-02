package com.app.buna.foodplace.model.preference

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.app.buna.foodplace.R
import com.app.buna.foodplace.global.App
import java.lang.IllegalStateException
import kotlin.reflect.typeOf

@SuppressLint("StaticFieldLeak")
object FoodPlacePreference {
    private var preference: SharedPreferences

    init {
        val context: Context = App.context?.applicationContext!!
        val preferenceName = context.getString(R.string.preference_name)
        preference = context.getSharedPreferences(preferenceName, MODE_PRIVATE)
    }

    internal fun<T> putValue(key: String, value: T) {
        when (value) {
            is String -> {
                preference.edit().putString(key, value).commit()
            }
            is Int -> {
                preference.edit().putInt(key, value).commit()
            }
            is Boolean -> {
                preference.edit().putBoolean(key, value).commit()
            }
            is Long -> {
                preference.edit().putLong(key, value).commit()
            }
            is Float -> {
                preference.edit().putFloat(key, value).commit()
            } else -> {
                throw IllegalStateException()
            }
        }
    }

    internal fun getString(key: String, defaultValue: String? = null): String? {
        return preference.getString(key, defaultValue)
    }

    internal fun getBoolean(key: String, defaultValue: Boolean? = null): Boolean {
        return preference.getBoolean(key, defaultValue!!)
    }

    internal fun getLong(key: String, defaultValue: Long? = null): Long {
        return preference.getLong(key, defaultValue!!)
    }

    internal fun getFloat(key: String, defaultValue: Float? = null): Float {
        return preference.getFloat(key, defaultValue!!)
    }

    internal fun getInt(key: String, defaultValue: Int? = null): Int {
        return preference.getInt(key, defaultValue!!)
    }
}