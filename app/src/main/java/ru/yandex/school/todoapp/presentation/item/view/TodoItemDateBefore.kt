package ru.yandex.school.todoapp.presentation.item.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.yandex.school.todoapp.R

@Composable
fun TodoItemDateBefore(
    itemDeadlineDate: String?,
    onSwitchChanged: (Boolean) -> Unit,
    onDateBeforeClicked: () -> Unit
) {
    val makeBeforeModifier = if (itemDeadlineDate != null) {
        Modifier
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(4.dp))
            .clickable { onDateBeforeClicked.invoke() }
    } else {
        Modifier
            .padding(horizontal = 16.dp)
    }
    Row(
        modifier = makeBeforeModifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                modifier = Modifier
                    .padding(vertical = 4.dp),
                text = stringResource(id = R.string.todo_item_view_before),
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Text(
                text = itemDeadlineDate.orEmpty(),
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        Switch(
            checked = itemDeadlineDate != null,
            onCheckedChange = onSwitchChanged,
            colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.colorScheme.onSurface,
                uncheckedThumbColor = MaterialTheme.colorScheme.tertiary,
                checkedTrackColor = MaterialTheme.colorScheme.onSurface,
                uncheckedTrackColor = MaterialTheme.colorScheme.outlineVariant
            )
        )
    }
}

@Preview
@Composable
fun TodoItemDateBeforePreview() {
    TodoItemDateBefore(
        itemDeadlineDate = "15 июля 2023",
        onSwitchChanged = { },
        onDateBeforeClicked = { }
    )
}
