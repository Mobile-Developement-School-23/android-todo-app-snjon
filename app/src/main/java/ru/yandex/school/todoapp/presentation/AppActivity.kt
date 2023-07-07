package ru.yandex.school.todoapp.presentation

import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import org.koin.android.ext.android.inject
import ru.yandex.school.todoapp.R
import ru.yandex.school.todoapp.presentation.navigation.AppNavigator

class AppActivity : AppCompatActivity(R.layout.activity_app) {
    private val appNavigator: AppNavigator by inject()

    override fun onStart() {
        super.onStart()

        appNavigator.attach(findNavHostFragment().navController)
    }

    override fun onStop() {
        super.onStop()

        appNavigator.detach()
    }

    private fun findNavHostFragment(): NavHostFragment {
        return supportFragmentManager.findFragmentById(R.id.application_nav_host) as NavHostFragment
    }
}