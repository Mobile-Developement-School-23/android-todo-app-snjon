package ru.yandex.school.todoapp.data.datastore

import android.content.SharedPreferences
import java.util.UUID

private enum class KEYS {
    ID_KEY,
    REV_KEY,
    MODE_KEY,
    TOKEN_KEY,
    USER_KEY,
    SYNC_KEY,
    THEME_KEY
}

private const val RANDOM_UUID_SUBSEQUENCE_START = 0
private const val RANDOM_UUID_SUBSEQUENCE_END = 6

/**
 * A class that stores data such as device id, last known version of data,
 * flag application online mode, token, username and sync flag in SharedPreferences.
 * Instance of @param preferences SharedPreferences
 */
class DataStorage(private val preferences: SharedPreferences) {
    val deviceId: String = preferences.getString(KEYS.ID_KEY.name, null) ?: run {
        val id = UUID.randomUUID().toString()
            .subSequence(RANDOM_UUID_SUBSEQUENCE_START, RANDOM_UUID_SUBSEQUENCE_END).toString()
        saveToPreferences(id, KEYS.ID_KEY)
        id
    }

    var knownRevision: Int = 0
        set(value) {
            field = value
            saveToPreferences(value, KEYS.REV_KEY)
        }
        get() = preferences.getInt(KEYS.REV_KEY.name, 0)

    var isSync: Boolean = false
        set(value) {
            field = value
            saveToPreferences(value, KEYS.SYNC_KEY)
        }
        get() = preferences.getBoolean(KEYS.SYNC_KEY.name, false)

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

    var theme: String = "SYSTEM"
        set(value) {
            field = value
            saveToPreferences(value, KEYS.THEME_KEY)
        }
        get() = preferences.getString(KEYS.THEME_KEY.name, "SYSTEM") ?: "SYSTEM"

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
