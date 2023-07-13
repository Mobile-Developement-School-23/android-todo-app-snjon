package ru.yandex.school.todoapp.presentation.settings.theme

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.yandex.school.todoapp.R
import ru.yandex.school.todoapp.domain.model.ThemeType
import ru.yandex.school.todoapp.presentation.util.bind

/**
 * Bottom sheet dialog fragment for selecting theme
 */
class ThemeBottomSheet : BottomSheetDialogFragment(R.layout.theme_bottom_sheet) {

    private val systemButton by bind<LinearLayout>(R.id.settings_theme_system)
    private val lightButton by bind<LinearLayout>(R.id.settings_theme_light)
    private val darkButton by bind<LinearLayout>(R.id.settings_theme_dark)

    private var themeListener: Listener? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        systemButton.setOnClickListener {
            onPrioritySelected(ThemeType.SYSTEM)
        }
        lightButton.setOnClickListener {
            onPrioritySelected(ThemeType.LIGHT)
        }
        darkButton.setOnClickListener {
            onPrioritySelected(ThemeType.DARK)
        }
    }

    private fun onPrioritySelected(theme: ThemeType) {
        themeListener?.onThemeSelected(theme)
        dismiss()
    }

    companion object {

        fun show(fm: FragmentManager, listener: Listener) {
            ThemeBottomSheet()
                .apply { themeListener = listener }
                .show(fm, ThemeBottomSheet::class.java.name)
        }
    }

    interface Listener {

        fun onThemeSelected(theme: ThemeType)
    }
}
