package ru.yandex.school.todoapp.data.provider

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import ru.yandex.school.todoapp.BuildConfig
import ru.yandex.school.todoapp.data.network.interceptor.AuthInterceptor

/**
 * OkHttpProvider class for providing an instance of OkHttpClient
 * @param authInterceptor The AuthInterceptor instance to be added to OkHttpClient
 */
class OkHttpProvider(private val authInterceptor: AuthInterceptor) {

    fun provide(): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)

        if (BuildConfig.DEBUG) {
            builder.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        }

        return builder.build()
    }
}
