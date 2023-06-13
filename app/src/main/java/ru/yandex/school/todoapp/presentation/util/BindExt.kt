package ru.yandex.school.todoapp.presentation.util

import android.app.Activity
import android.view.View
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

inline fun <reified T : Any?> View.bind(@IdRes idRes: Int): Lazy<T> {
    return unsafeLazy { findViewById<View>(idRes) as T }
}

fun <T : View> bind(id: Int): ReadOnlyProperty<Fragment, T> {
    return FragmentFindViewDelegate(id)
}

inline fun <reified T : Any?> Activity.bind(@IdRes idRes: Int): Lazy<T> {
    return unsafeLazy { findViewById<View>(idRes) as T }
}

inline fun <reified T : Any?> RecyclerView.ViewHolder.bind(@IdRes idRes: Int): Lazy<T> {
    return unsafeLazy { itemView.findViewById<View>(idRes) as T }
}

fun <T> unsafeLazy(initializer: () -> T) = lazy(LazyThreadSafetyMode.NONE, initializer)

class FragmentFindViewDelegate<T : View>(private val id: Int) : ReadOnlyProperty<Fragment, T> {
    private var value: T? = null
    private val lifecycleObserver = PropertyLifecycleObserver()

    override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
        val view = thisRef.view ?: throw IllegalStateException("View could not be null")

        var currentValue = value
        return if (currentValue == null) {
            lifecycleObserver.attach(thisRef.lifecycle)
            currentValue = view.findViewById(id)
            value = currentValue

            currentValue
        } else {
            currentValue
        }
    }

    private inner class PropertyLifecycleObserver : DefaultLifecycleObserver {

        private var _lifecycle: Lifecycle? = null

        fun attach(lifecycle: Lifecycle) {
            _lifecycle = lifecycle
            _lifecycle?.addObserver(this)
        }

        override fun onPause(owner: LifecycleOwner) {
            removeObserver()
            super.onPause(owner)
        }

        override fun onStop(owner: LifecycleOwner) {
            removeObserver()
            super.onStop(owner)
        }

        override fun onDestroy(owner: LifecycleOwner) {
            removeObserver()
            super.onDestroy(owner)
        }

        private fun removeObserver() {
            value = null
            _lifecycle?.removeObserver(this)
            _lifecycle = null
        }
    }
}