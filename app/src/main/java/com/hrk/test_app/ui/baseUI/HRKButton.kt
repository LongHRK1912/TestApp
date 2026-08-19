package com.hrk.test_app.ui.baseUI

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.hrk.test_app.ui.theme.LocalExtendedColors
import com.hrk.test_app.utils.ComposeUtils.clickableSingle

enum class ButtonType {
    Basic,
    Outlined,
}

enum class TypeSize {
    WrapContent,
    FillMaxWidth,
    FillMaxHeight,
    FillMaxSize
}

data class ButtonModel(
    val text: String,
    val icon: Int? = null,
    val onClick: () -> Unit,
    val enabled: Boolean = true,
    val type: ButtonType = ButtonType.Basic,
    val sizeType: TypeSize = TypeSize.WrapContent,
    val containerColor: Color? = null,
    val contentColor: Color? = null,
    val outlinedColor: Color? = null,
    val disabledContainerColor: Color? = null,
    val disabledContentColor: Color? = null,
    val vertical: Dp = 16.dp,
    val horizontal: Dp = 24.dp
)

@Composable
fun HRKButton(
    modifier: Modifier = Modifier,
    model: ButtonModel
) {
    val colors = LocalExtendedColors.current

    val containerColor =
        if (model.enabled) model.containerColor ?: colors.accent else model.disabledContainerColor
            ?: colors.disabled

    val contentColor =
        if (model.enabled) model.contentColor ?: colors.onAccent else model.disabledContentColor
            ?: colors.onDisabled

    when (model.type) {
        ButtonType.Basic -> BasicButton(
            modifier = modifier,
            text = model.text,
            icon = model.icon,
            onClick = model.onClick,
            enabled = model.enabled,
            containerColor = containerColor,
            contentColor = contentColor,
            sizeType = model.sizeType,
            vertical = model.vertical,
            horizontal = model.horizontal,
        )

        ButtonType.Outlined -> OutlinedButton(
            modifier = modifier,
            text = model.text,
            icon = model.icon,
            onClick = model.onClick,
            enabled = model.enabled,
            outlineColor = contentColor,
            contentColor = contentColor,
            sizeType = model.sizeType,
            vertical = model.vertical,
            horizontal = model.horizontal,
        )
    }
}

@Composable
fun BasicButton(
    modifier: Modifier = Modifier,
    text: String,
    icon: Int? = null,
    onClick: () -> Unit,
    enabled: Boolean,
    containerColor: Color,
    contentColor: Color,
    sizeType: TypeSize,
    vertical: Dp,
    horizontal: Dp,
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .then(
                when (sizeType) {
                    TypeSize.WrapContent -> Modifier.wrapContentSize()
                    else -> Modifier.fillMaxWidth()
                }
            )
            .background(containerColor)
            .clickableSingle(enabled) { onClick() }
            .padding(vertical = vertical, horizontal = horizontal),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        if (icon != null) {
            Icon(
                painter = painterResource(icon),
                contentDescription = null,
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(20.dp),
                tint = contentColor
            )
        }

        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = contentColor,
                fontWeight = FontWeight.ExtraBold
            )
        )
    }
}

@Composable
fun OutlinedButton(
    modifier: Modifier = Modifier,
    text: String,
    icon: Int? = null,
    onClick: () -> Unit,
    enabled: Boolean,
    outlineColor: Color,
    contentColor: Color,
    sizeType: TypeSize,
    vertical: Dp,
    horizontal: Dp,
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .then(
                when (sizeType) {
                    TypeSize.WrapContent -> Modifier.wrapContentSize()
                    else -> Modifier.fillMaxWidth()
                }
            )
            .border(
                width = 1.dp,
                color = outlineColor,
                shape = RoundedCornerShape(16.dp)
            )
            .clickableSingle(enabled) { onClick() }
            .padding(vertical = vertical, horizontal = horizontal),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        if (icon != null) {
            Icon(
                painter = painterResource(icon),
                contentDescription = null,
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(20.dp),
                tint = contentColor
            )
        }

        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = contentColor
            )
        )
    }
}
