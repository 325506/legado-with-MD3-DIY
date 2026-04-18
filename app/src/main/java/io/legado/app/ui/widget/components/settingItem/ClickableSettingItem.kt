package io.legado.app.ui.widget.components.settingItem

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.legado.app.ui.config.themeConfig.ThemeConfig
import io.legado.app.ui.theme.LegadoTheme
import io.legado.app.ui.theme.ThemeResolver
import top.yukonga.miuix.kmp.basic.BasicComponentDefaults
import top.yukonga.miuix.kmp.preference.ArrowPreference

@Composable
fun ClickableSettingItem(
    title: String,
    modifier: Modifier = Modifier,
    description: String? = null,
    option: String? = null,
    imageVector: ImageVector? = null,
    onLongClick: (() -> Unit)? = null,
    trailingContent: (@Composable () -> Unit)? = null,
    showDivider: Boolean = true,
    onClick: () -> Unit
) {
    val enableBorder = ThemeConfig.enableContainerBorder
    val enableItemDivider = ThemeConfig.enableItemDivider
    val itemDividerWidth = ThemeConfig.itemDividerWidth
    val itemDividerLength = ThemeConfig.itemDividerLength
    val itemDividerColor = if (ThemeConfig.itemDividerColor != 0) {
        Color(ThemeConfig.itemDividerColor)
    } else {
        MaterialTheme.colorScheme.outline
    }

    val composeEngine = LegadoTheme.composeEngine
    if (ThemeResolver.isMiuixEngine(composeEngine)) {
        ArrowPreference(
            title = title,
            summary = description,
            insideMargin = BasicComponentDefaults.InsideMargin,
            onClick = onClick
        )
    } else {
        Column {
            SettingItem(
                modifier = modifier,
                title = title,
                description = description,
                option = option,
                imageVector = imageVector,
                trailingContent = trailingContent ?: {
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                onClick = onClick,
                onLongClick = onLongClick
            )

            if (enableBorder && enableItemDivider && showDivider) {
                CenteredDivider(
                    width = itemDividerWidth,
                    lengthPercent = itemDividerLength,
                    color = itemDividerColor
                )
            }
        }
    }
}

@Composable
private fun CenteredDivider(
    width: Float,
    lengthPercent: Float,
    color: Color
) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(lengthPercent / 100f)
                .height(width.dp)
                .background(color)
        )
    }
}