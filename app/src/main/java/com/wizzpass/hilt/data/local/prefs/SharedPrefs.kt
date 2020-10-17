package com.wizzpass.hilt.data.local.prefs

/**
 * Created by novuyo on 17,October,2020
 */

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.preference.PreferenceManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken



class SharedPrefs private constructor() {
    fun clearAllPreferences() {
        prefsEditor = sharedPreferences!!.edit()
        prefsEditor!!.clear()
        prefsEditor!!.commit()
    }

    fun clearPreferences(key: String?) {
        prefsEditor!!.remove(key)
        prefsEditor!!.commit()
    }

    fun setIntValue(Tag: String?, value: Int) {
        prefsEditor!!.putInt(Tag, value)
        prefsEditor!!.apply()
    }

    fun getIntValue(Tag: String?): Int {
        return sharedPreferences!!.getInt(Tag, 0)
    }

    fun setLongValue(Tag: String?, value: Long) {
        prefsEditor!!.putLong(Tag, value)
        prefsEditor!!.apply()
    }

    fun getLongValue(Tag: String?): Long {
        return sharedPreferences!!.getLong(Tag, 0)
    }

    fun setValue(Tag: String?, token: String?) {
        prefsEditor!!.putString(Tag, token)
        prefsEditor!!.commit()
    }



    fun getBooleanValue(Tag: String?): Boolean {
        return sharedPreferences!!.getBoolean(Tag, false)
    }

    fun setBooleanValue(Tag: String?, token: Boolean) {
        prefsEditor!!.putBoolean(Tag, token)
        prefsEditor!!.commit()
    }





    companion object {
        var sharedPreferences: SharedPreferences? = null
        var prefsEditor: Editor? = null
        var sharedPrefs: SharedPrefs? = null
        fun getInstance(ctx: Context?): SharedPrefs? {
            if (sharedPrefs == null) {
                sharedPrefs = SharedPrefs()
                sharedPreferences =
                    PreferenceManager.getDefaultSharedPreferences(ctx)
                prefsEditor = sharedPreferences!!.edit()
            }
            return sharedPrefs
        }
    }
}