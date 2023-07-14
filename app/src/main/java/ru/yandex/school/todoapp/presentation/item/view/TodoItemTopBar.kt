package ru.yandex.school.todoapp.presentation.item.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.yandex.school.todoapp.R

@Composable
fun TodoItemTopBar(
    itemText: String,
    onBackClicked: () -> Unit,
    onSaveClicked: () -> Unit
) {
    TopAppBar(
        backgroundColor = MaterialTheme.colorScheme.primary,
        title = {},
        navigationIcon = {
            IconButton(onClick = onBackClicked) {
                Icon(
                    painter = painterResource(R.drawable.ic_close),
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        },
        actions = {
            val isTextFieldNotEmpty = itemText.isNotEmpty()
            val isSaveClickable = if (isTextFieldNotEmpty) {
                Modifier
                    .fillMaxHeight()
                    .padding(4.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .clickable { onSaveClicked.invoke() }
            } else {
                Modifier
                    .padding(4.dp)
            }
            Box(
                modifier = isSaveClickable,
                contentAlignment = Alignment.Center
            ) {
                Text(
                    modifier = Modifier
                        .padding(12.dp),
                    text = stringResource(id = R.string.todo_item_view_save),
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    )
}

@Preview
@Composable
fun TodoItemTopBarPreview() {
    TodoItemTopBar(
        itemText = "Sample Text",
        onBackClicked = { },
        onSaveClicked = { }
    )
}
