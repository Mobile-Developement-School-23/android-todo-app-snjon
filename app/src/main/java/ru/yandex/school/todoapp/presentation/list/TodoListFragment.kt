package ru.yandex.school.todoapp.presentation.list

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.yandex.school.todoapp.R
import ru.yandex.school.todoapp.domain.model.TodoItem
import ru.yandex.school.todoapp.presentation.list.adapter.TodoListAdapter
import ru.yandex.school.todoapp.presentation.list.model.TodoListScreenState
import ru.yandex.school.todoapp.presentation.util.bind
import ru.yandex.school.todoapp.presentation.util.elevationOrNot
import ru.yandex.school.todoapp.presentation.util.makeGone
import ru.yandex.school.todoapp.presentation.util.makeVisible
import ru.yandex.school.todoapp.presentation.util.repeatOnCreated
import ru.yandex.school.todoapp.presentation.util.setRecyclerViewItemTouchListener
import ru.yandex.school.todoapp.presentation.util.visibleOrGone
import ru.yandex.school.todoapp.presentation.util.visibleOrInvisible

class TodoListFragment : Fragment(R.layout.fragment_todo_list) {

    private val viewModel: TodoListViewModel by viewModel()

    private val toolbar by bind<Toolbar>(R.id.todo_list_toolbar)
    private val collapsingToolbar by bind<CollapsingToolbarLayout>(R.id.todo_list_collapsing_toolbar)
    private val appBar by bind<AppBarLayout>(R.id.todo_list_app_bar)
    private val appBarSubtitle by bind<TextView>(R.id.todo_list_subtitle)
    private val expandedContainer by bind<ConstraintLayout>(R.id.todo_list_expanded_container)
    private val recyclerView by bind<RecyclerView>(R.id.todo_list_recycler_view)
    private val emptyView by bind<TextView>(R.id.todo_list_empty_view)
    private val addNewButton by bind<FloatingActionButton>(R.id.todo_list_add_new)
    private val showCompletedToolbarButton by bind<ImageView>(R.id.todo_list_toolbar_visibility)
    private val showCompletedAppBarButton by bind<ImageView>(R.id.todo_list_app_bar_visibility)

    private val listAdapter by lazy { createAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindViews()
        subscribeOnViewModel()
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshList()
    }

    private fun bindViews() {
        appBar.addOnOffsetChangedListener(createAppBarOnOffsetChangedListener())
        recyclerView.apply {
            adapter = listAdapter
            setRecyclerViewItemTouchListener()
            makeGone()
        }
        emptyView.makeVisible()
        addNewButton.setOnClickListener { viewModel.createNewTodo() }
        showCompletedAppBarButton.setOnClickListener { viewModel.changeCompletedTodosVisibility() }
        showCompletedToolbarButton.setOnClickListener { viewModel.changeCompletedTodosVisibility() }
    }

    private fun subscribeOnViewModel() {
        viewModel.todoListItemsState.repeatOnCreated(this) {
            showContent(it)
        }
    }

    private fun showContent(content: TodoListScreenState) {
        populateAppBar(content)
        populateTodoList(content.listItems)
    }

    private fun populateAppBar(content: TodoListScreenState) {
        appBarSubtitle.visibleOrGone(content.completedCount > 0)
        appBarSubtitle.text = getString(R.string.todo_list_completed_count, content.completedCount)

        val visibilityIcon = if (content.isCompletedShowed) {
            R.drawable.ic_visibility_off
        } else {
            R.drawable.ic_visibility
        }
        showCompletedAppBarButton.setImageResource(visibilityIcon)
        showCompletedToolbarButton.setImageResource(visibilityIcon)
    }

    private fun populateTodoList(items: List<TodoItem>) {
        recyclerView.visibleOrGone(items.isNotEmpty())
        emptyView.visibleOrGone(items.isEmpty())
        listAdapter.submitList(items)
    }

    private fun createAdapter(): TodoListAdapter {
        return TodoListAdapter(
            onInfoClick = { viewModel.openTodoItemInfo(it) },
            onLongClick = {todoItem: TodoItem, resId: Int -> viewModel.actionOnItem(todoItem, resId) },
            onSwipeToCheck = { viewModel.checkTodoItem(it) },
            onSwipeToDelete = { viewModel.deleteTodoItem(it) }
        )
    }

    private fun createAppBarOnOffsetChangedListener(): AppBarLayout.OnOffsetChangedListener {
        return AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
            val shouldShowToolbar = verticalOffset == -collapsingToolbar.height + toolbar.height
            toolbar.visibleOrInvisible(shouldShowToolbar)
            collapsingToolbar.elevationOrNot(shouldShowToolbar)
            expandedContainer.visibleOrInvisible(shouldShowToolbar.not())
        }
    }
}