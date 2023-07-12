package ru.yandex.school.todoapp.presentation.snackbar.model

/**
 * Model representing a Snackbar, which contains a message and an optional action
 * @property message The message to be displayed in the Snackbar
 * @property action The action associated with the Snackbar. It can be null if no action is required
 */
class SnackbarModel(
    val message: String,
    val action: SnackbarAction? = null
)

class SnackbarAction(
    val onCancelled: () -> Unit,
    val onFinished: () -> Unit
)
