package ru.yandex.school.todoapp

import android.app.Application
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import ru.yandex.school.todoapp.data.network.workmanager.UploadTodosWork
import ru.yandex.school.todoapp.di.appModule
import java.util.concurrent.TimeUnit

private const val UPLOAD_WORK_INTERVAL_HOURS = 8L

class TodoAppApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@TodoAppApplication)
            modules(appModule)
        }

        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresStorageNotLow(true)
            .build()

        val periodicWorkRequest =
            PeriodicWorkRequest.Builder(
                UploadTodosWork::class.java,
                UPLOAD_WORK_INTERVAL_HOURS,
                TimeUnit.HOURS
            )
                .setConstraints(constraints)
                .build()

        WorkManager.getInstance(this).enqueue(periodicWorkRequest)
    }
}
