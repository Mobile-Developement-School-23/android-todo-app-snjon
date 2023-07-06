package ru.yandex.school.todoapp.data.api.interceptor

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import ru.yandex.school.todoapp.data.datastore.DataStorage

private const val AUTH_HEADER_NAME = "Authorization"
private const val AUTH_HEADER_VALUE_MASK = "Bearer %s"

class AuthInterceptor(
    private val dataStorage: DataStorage
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        return if (dataStorage.token == null) {
            Log.e("token", "!isAuthorized AuthInterceptor")
            chain.proceed(chain.request())
        } else {

            val request = chain.request().newBuilder()
                .header(AUTH_HEADER_NAME, AUTH_HEADER_VALUE_MASK.format(dataStorage.token))
                .build()
            chain.proceed(request)
        }
    }
}