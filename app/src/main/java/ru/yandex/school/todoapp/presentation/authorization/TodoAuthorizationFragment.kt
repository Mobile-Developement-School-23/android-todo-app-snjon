package ru.yandex.school.todoapp.presentation.authorization

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.yandex.school.todoapp.R
import ru.yandex.school.todoapp.presentation.authorization.viewmodel.TodoAuthorizationViewModel
import ru.yandex.school.todoapp.presentation.util.bind
import ru.yandex.school.todoapp.presentation.util.showToast

class TodoAuthorizationFragment : Fragment(R.layout.fragment_authorization) {
    private val viewModel: TodoAuthorizationViewModel by viewModel()

    private val progressBar by bind<LinearLayout>(R.id.authorization_progress_linear)

    private val loginText by bind<EditText>(R.id.authorization_login_edit_text)
    private val loginObligatoryText by bind<TextView>(R.id.authorization_login_obligatory_text)

    private val passwordText by bind<EditText>(R.id.authorization_password_edit_text)
    private val passwordObligatoryText by bind<TextView>(R.id.authorization_password_obligatory_text)

    private val signInButton by bind<MaterialButton>(R.id.authorization_sign_in_button)
    private val continueButton by bind<TextView>(R.id.authorization_continue_button)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindViews()
        subscribeOnViewModel()
    }

    private fun bindViews() {
        continueButton.setOnClickListener {
            viewModel.setInternetMode(false)
            viewModel.openTodoList()
        }

        signInButton.setOnClickListener {
            val login = loginText.text.toString()
            val password = passwordText.text.toString()

            if (login.isEmpty() || password.isEmpty()) {
                if (login.isEmpty()) {
                    loginObligatoryText.isVisible = true
                }
                if (password.isEmpty()) {
                    passwordObligatoryText.isVisible = true
                }
                return@setOnClickListener
            }

            val credentials = Pair(login, password)
            viewModel.authorization(credentials)
        }

        loginText.doOnTextChanged { _, _, _, _ ->
            loginObligatoryText.isVisible = false
        }

        passwordText.doOnTextChanged { _, _, _, _ ->
            passwordObligatoryText.isVisible = false
        }
    }

    private fun subscribeOnViewModel() {
        viewModel.todoAuthorizationState.observe(viewLifecycleOwner) { state ->
            if (state == null) return@observe
            when {
                state.isAuthorized -> viewModel.openTodoList()

                state.loading -> {
                    progressBar.isVisible = true
                }
            }
        }

        viewModel.errorLiveData.observe(viewLifecycleOwner) { message ->
            progressBar.isVisible = false
            if (message != null) {
                showToast(message)
            }
        }
    }
}
