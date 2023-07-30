package ru.yandex.school.todoapp.presentation.item.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.yandex.school.todoapp.R

@Composable
fun TodoItemDeleteButton(
    itemModifiedDate: String?,
    onDeleteClicked: () -> Unit
) {
    val isTodoExists = itemModifiedDate != null
    val isDeleteClickable = if (isTodoExists) {
        Modifier
            .clip(RoundedCornerShape(4.dp))
            .clickable { onDeleteClicked.invoke() }
    } else {
        Modifier
    }
    Box(
        modifier = Modifier.padding(4.dp)
    ) {
        Row(
            modifier = isDeleteClickable
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        )
        {
            Icon(
                modifier = Modifier.padding(end = 4.dp),
                painter = painterResource(id = R.drawable.ic_delete),
                contentDescription = "",
                tint = if (isTodoExists) colorResource(id = R.color.color_red) else colorResource(
                    id = R.color.label_disable
                )
            )
            Text(
                text = stringResource(id = R.string.todo_item_view_delete),
                fontSize = 14.sp,
                color = if (isTodoExists) colorResource(id = R.color.color_red) else colorResource(
                    id = R.color.label_disable
                )
            )
        }
    }
}

@Preview
@Composable
fun TodoItemDeleteButtonPreview() {
    TodoItemDeleteButton(
        itemModifiedDate = "",
        onDeleteClicked = { }
    )
}
