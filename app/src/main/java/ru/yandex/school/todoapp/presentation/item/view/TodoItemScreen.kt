package ru.yandex.school.todoapp.presentation.item.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.StateFlow
import ru.yandex.school.todoapp.presentation.item.model.TodoItemScreenState
import ru.yandex.school.todoapp.presentation.theme.AppTheme

@Composable
fun TodoItemScreen(
    todoItemStateFlow: StateFlow<TodoItemScreenState>,
    onBackClicked: () -> Unit = {},
    onSaveClicked: () -> Unit = {},
    onTextChanged: (String) -> Unit = {},
    onPriorityClicked: () -> Unit = {},
    onSwitchChanged: (Boolean) -> Unit = {},
    onDateBeforeClicked: () -> Unit = {},
    onDeleteClicked: () -> Unit = {}
) {
    val uiState by todoItemStateFlow.collectAsState()

    TodoItemContent(
        uiState = uiState,
        onBackClicked = onBackClicked,
        onSaveClicked = onSaveClicked,
        onTextChanged = onTextChanged,
        onPriorityClicked = onPriorityClicked,
        onSwitchChanged = onSwitchChanged,
        onDateBeforeClicked = onDateBeforeClicked,
        onDeleteClicked = onDeleteClicked
    )
}

@Preview(uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES, name = "DARK")
@Preview(uiMode = android.content.res.Configuration.UI_MODE_NIGHT_NO, name = "LIGHT")
@Composable
private fun TodoItemContent(
    uiState: TodoItemScreenState = TodoItemScreenState(),
    onBackClicked: () -> Unit = {},
    onSaveClicked: () -> Unit = {},
    onTextChanged: (String) -> Unit = {},
    onPriorityClicked: () -> Unit = {},
    onSwitchChanged: (Boolean) -> Unit = {},
    onDateBeforeClicked: () -> Unit = {},
    onDeleteClicked: () -> Unit = {}
) {
    AppTheme {
        Scaffold(
            topBar = {
                TodoItemTopBar(
                    itemText = uiState.text,
                    onBackClicked = onBackClicked,
                    onSaveClicked = onSaveClicked
                )
            },
        ) { paddings ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddings)
                    .verticalScroll(rememberScrollState())
                    .background(MaterialTheme.colorScheme.primary),
            )
            {
                TodoItemTextField(itemText = uiState.text, onTextChanged = onTextChanged)
                TodoItemPriority(
                    itemPriorityRes = uiState.priorityRes,
                    onPriorityClicked = onPriorityClicked
                )
                Spacer(modifier = Modifier.height(16.dp))
                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                ) {
                    Divider(
                        modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colorScheme.outline
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                TodoItemDateBefore(
                    itemDeadlineDate = uiState.deadlineDate,
                    onSwitchChanged = onSwitchChanged,
                    onDateBeforeClicked = onDateBeforeClicked
                )
                Spacer(modifier = Modifier.height(30.dp))
                Divider(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.outline
                )
                Spacer(modifier = Modifier.height(8.dp))
                TodoItemDeleteButton(
                    itemModifiedDate = uiState.modifiedDate,
                    onDeleteClicked = onDeleteClicked
                )
            }
        }
    }
}
