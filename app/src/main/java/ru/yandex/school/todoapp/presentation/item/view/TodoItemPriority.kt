package ru.yandex.school.todoapp.presentation.item.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.yandex.school.todoapp.R


@Composable
fun TodoItemPriority(
    itemPriorityRes: Int,
    onPriorityClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(4.dp))
            .clickable { onPriorityClicked.invoke() }
    ) {
        Text(
            text = stringResource(id = R.string.todo_item_view_priority),
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onPrimary
        )
        Text(
            modifier = Modifier
                .padding(vertical = 4.dp),
            text = stringResource(id = itemPriorityRes),
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onTertiary
        )
    }
}

@Preview
@Composable
fun TodoItemPriorityPreview() {
    TodoItemPriority(
        itemPriorityRes = R.string.todo_item_view_priority_default,
        onPriorityClicked = { }
    )
}
