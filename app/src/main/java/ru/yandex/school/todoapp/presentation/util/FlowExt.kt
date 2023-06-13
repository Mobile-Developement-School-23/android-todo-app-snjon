package ru.yandex.school.todoapp.presentation.util

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

fun <T> Flow<T>.repeatOnCreated(lifecycleOwner: LifecycleOwner) {
    lifecycleOwner.lifecycleScope.launch {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
            this@repeatOnCreated.collect()
        }
    }
}

fun <T> Flow<T>.repeatOnCreated(lifecycleOwner: LifecycleOwner, action: suspend (T) -> Unit) {
    onEach { action(it) }
        .repeatOnCreated(lifecycleOwner)
}

suspend fun MutableSharedFlow<Unit>.emit() {
    this.emit(Unit)
}