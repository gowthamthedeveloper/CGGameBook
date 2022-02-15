package com.impeccthreads.cggamebook.utility

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.util.Log
import com.google.gson.Gson
import com.impeccthreads.cggamebook.application.Constants
import com.impeccthreads.cggamebook.dto.CardGameDetails
import com.impeccthreads.cggamebook.dto.Player

object PreferenceHelper {

    private var defaultPrefs: SharedPreferences? = null

    private val appCustomPreferenceName = "CGGameBookRef"
    private var customPrefs: SharedPreferences? = null

    fun init(context: Context) {
        defaultPrefs = PreferenceManager.getDefaultSharedPreferences(context)
    }

    fun customInit(context: Context) {
        customPrefs = context.getSharedPreferences(appCustomPreferenceName, Context.MODE_PRIVATE)
    }

    private operator fun set(key: String, value: Any?) {
        when (value) {
            is String? -> defaultPrefs?.edit()?.putString(key, value)?.apply()
            is Int -> defaultPrefs?.edit()?.putInt(key, value)?.apply()
            is Boolean -> defaultPrefs?.edit()?.putBoolean(key, value)?.apply()
            is Float -> defaultPrefs?.edit()?.putFloat(key, value)?.apply()
            is Long -> defaultPrefs?.edit()?.putLong(key, value)?.apply()
            else -> throw UnsupportedOperationException("Not yet implemented")
        }
    }

    private operator inline fun <reified T : Any> get(key: String, defaultValue: T? = null): T? {
        return when (T::class) {
            String::class -> defaultPrefs?.getString(key, defaultValue as? String) as T?
            Int::class -> defaultPrefs?.getInt(key, defaultValue as? Int ?: -1) as T?
            Boolean::class -> defaultPrefs?.getBoolean(key, defaultValue as? Boolean ?: false) as T?
            Float::class -> defaultPrefs?.getFloat(key, defaultValue as? Float ?: -1f) as T?
            Long::class -> defaultPrefs?.getLong(key, defaultValue as? Long ?: -1) as T?
            else -> throw UnsupportedOperationException("Not yet implemented")
        }
    }

    fun isGameStarted(): Boolean? {
        return get(Constants.SharedPrefKeys.isGameStarted)
    }

    fun setGameStarted(isDocSynced: Boolean) {
        set(Constants.SharedPrefKeys.isGameStarted, isDocSynced)
    }

    fun getCurrentGameDetails(): CardGameDetails? {

        get(Constants.SharedPrefKeys.currentGameDetails, "").let {
            Log.d("Get Json From Pref", it!!)

            if (it!!.isNotEmpty())
            {
                return Gson().fromJson(it!!, CardGameDetails::class.java)
            }
        }

        return null
    }


    fun setCurrentGameDetails(cardGameDetails: CardGameDetails) {

        Log.d("Convert to Json", cardGameDetails.toString())

        set(Constants.SharedPrefKeys.currentGameDetails, Gson().toJson(cardGameDetails))
    }



//    fun getCurrentGameDetails(): ArrayList<Player> {
//
//        get(Constants.SharedPrefKeys.currentGameDetails, "").let {
//            Log.d("Get Json From Pref", it!!)
//
//            if (it!!.isNotEmpty())
//            {
//                return Gson().fromJson(it!!, Array<Player>::class.java).toList() as ArrayList<Player>
//            }
//        }
//
//        return arrayListOf()
//    }
//
//
//    fun setCurrentGameDetails(players: ArrayList<Player>) {
//
//        Log.d("Convert to Json", players.toString())
//
//        set(Constants.SharedPrefKeys.currentGameDetails, Gson().toJson(players))
//    }

}