package ru.yandex.school.todoapp.data.datastore

import android.content.SharedPreferences
import java.util.UUID


private enum class KEYS {
    ID_KEY,
    REV_KEY,
    MODE_KEY,
    TOKEN_KEY,
    USER_KEY
}

class DataStorage(private val preferences: SharedPreferences) {

    val deviceId: String = preferences.getString(KEYS.ID_KEY.name, null) ?: run {
        val id = UUID.randomUUID().toString().subSequence(0, 6).toString()
        saveToPreferences(id, KEYS.ID_KEY)
        id
    }

    var knownRevision: Int = 0
        set(value) {
            field = value
            saveToPreferences(value, KEYS.REV_KEY)
        }
        get() = preferences.getInt(KEYS.REV_KEY.name, 0)

    var onlineMode: Boolean = false
        set(value) {
            field = value
            saveToPreferences(value, KEYS.MODE_KEY)
        }
        get() = preferences.getBoolean(KEYS.MODE_KEY.name, false)

    var user: String? = null
        set(value) {
            field = value
            saveToPreferences(value, KEYS.USER_KEY)
        }
        get() = preferences.getString(KEYS.USER_KEY.name, null)

    var token: String? = null
        set(value) {
            field = value
            saveToPreferences(value, KEYS.TOKEN_KEY)
        }
        get() = preferences.getString(KEYS.TOKEN_KEY.name, null)


    private fun <T> saveToPreferences(value: T?, key: KEYS) {
        val editor: SharedPreferences.Editor = preferences.edit()
        when (value) {
            is Int -> editor.putInt(key.name, value)
            is String -> editor.putString(key.name, value)
            is Boolean -> editor.putBoolean(key.name, value)
            else -> editor.putString(key.name, null)
        }
        editor.apply()
    }
}