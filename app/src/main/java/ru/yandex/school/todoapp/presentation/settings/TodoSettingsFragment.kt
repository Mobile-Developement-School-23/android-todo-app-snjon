package ru.yandex.school.todoapp.presentation.settings

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.yandex.school.todoapp.R
import ru.yandex.school.todoapp.domain.model.ThemeType
import ru.yandex.school.todoapp.presentation.settings.model.SettingsScreenState
import ru.yandex.school.todoapp.presentation.settings.theme.ThemeBottomSheet
import ru.yandex.school.todoapp.presentation.settings.viewmodel.TodoSettingsViewModel
import ru.yandex.school.todoapp.presentation.util.bind
import ru.yandex.school.todoapp.presentation.util.hideKeyboard
import ru.yandex.school.todoapp.presentation.util.repeatOnCreated
import ru.yandex.school.todoapp.presentation.util.showToast

private const val EMPTY_USERNAME_TEXT = 0
private const val FEW_USERNAME_TEXT_MIN = 1
private const val FEW_USERNAME_TEXT_MAX = 3

class TodoSettingsFragment : Fragment(R.layout.fragment_settings) {

    private val viewModel: TodoSettingsViewModel by viewModel()

    private val toolbar by bind<androidx.appcompat.widget.Toolbar>(R.id.settings_toolbar)

    private val themeCard by bind<LinearLayout>(R.id.settings_change_theme)
    private val theme by bind<TextView>(R.id.settings_theme_text)

    private val userEditText by bind<EditText>(R.id.settings_user_name)
    private val changeUsernameButton by bind<MaterialButton>(R.id.settings_change_user_name)

    private val logoutButton by bind<MaterialButton>(R.id.settings_logout)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindViews()
        subscribeOnViewModel()
    }

    private fun subscribeOnViewModel() {
        viewModel.settingsScreenState.repeatOnCreated(this) {
            showContent(it)
        }
    }

    private fun showContent(content: SettingsScreenState) {
        theme.text = getString(content.themeRes)
        userEditText.setText(viewModel.getUsername())
    }

    private fun bindViews() {
        toolbar.setNavigationOnClickListener {
            viewModel.closeSettings()
        }

        themeCard.setOnClickListener {
            ThemeBottomSheet.show(childFragmentManager, object : ThemeBottomSheet.Listener {
                override fun onThemeSelected(theme: ThemeType) {
                    setTheme(theme)
                }
            })
        }

        userEditText.setOnClickListener {
            userEditText.selectAll()
        }

        changeUsernameButton.setOnClickListener {
            it.hideKeyboard()

            when (userEditText.text.length) {
                EMPTY_USERNAME_TEXT -> showToast(getString(R.string.settings_toast_message_empty))

                in FEW_USERNAME_TEXT_MIN..FEW_USERNAME_TEXT_MAX ->
                    showToast(getString(R.string.settings_toast_message_few))

                else -> {
                    showToast(getString(R.string.settings_toast_message_success))
                    viewModel.changeUsername(userEditText.text.toString())
                }
            }
        }

        logoutButton.setOnClickListener {
            viewModel.logout()
        }
    }

    private fun setTheme(themeValue: ThemeType) {
        viewModel.updateAppTheme(themeValue)
    }
}
