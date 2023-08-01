package ru.yandex.school.todoapp.presentation.item

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.yandex.school.todoapp.domain.model.TodoItemPriority
import ru.yandex.school.todoapp.presentation.datetime.DatePickerDialog
import ru.yandex.school.todoapp.presentation.item.priority.PriorityBottomSheet
import ru.yandex.school.todoapp.presentation.item.view.TodoItemScreen
import ru.yandex.school.todoapp.presentation.item.viewmodel.TodoItemViewModel
import ru.yandex.school.todoapp.presentation.navigation.KEY_TODO_ITEM_ID
import ru.yandex.school.todoapp.presentation.util.repeatOnResumed
import ru.yandex.school.todoapp.presentation.util.showToast

class TodoItemFragment : Fragment() {

    private val viewModel: TodoItemViewModel by viewModel {
        parametersOf(
            requireArguments().getString(KEY_TODO_ITEM_ID)
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeOnViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                TodoItemScreen(
                    viewModel.todoItemScreenState,
                    onBackClicked = { viewModel.closeTodoItem() },
                    onSaveClicked = { viewModel.addTodoItem() },
                    onTextChanged = { viewModel.updateTodoItemText(it) },
                    onPriorityClicked = { showPriorityDialog() },
                    onSwitchChanged = { viewModel.onDeadlineDateActivate(it) },
                    onDateBeforeClicked = { showDatePicker() },
                    onDeleteClicked = { viewModel.deleteTodoItem() }
                )
            }
        }
    }

    private fun subscribeOnViewModel() {
        viewModel.errorLiveData.observe(viewLifecycleOwner) { message ->
            if (message != null) {
                showToast(message)
            }
        }

        viewModel.loading.repeatOnResumed(this) {
            viewModel.onLoading(it)
        }
    }

    private fun showPriorityDialog() {
        PriorityBottomSheet.show(
            childFragmentManager,
            object : PriorityBottomSheet.Listener {
                override fun onPrioritySelected(priority: TodoItemPriority) {
                    viewModel.updateTodoItemPriority(priority)
                }
            }
        )
    }

    private fun showDatePicker() {
        DatePickerDialog
            .newInstance { viewModel.updateDeadlineDate(it) }
            .show(childFragmentManager, DatePickerDialog::class.java.name)
    }
}
