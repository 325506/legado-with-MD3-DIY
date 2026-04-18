package io.legado.app.ui.widget.components.card

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import io.legado.app.ui.config.themeConfig.ThemeConfig
import io.legado.app.ui.theme.LegadoTheme
import io.legado.app.ui.theme.ThemeResolver
import top.yukonga.miuix.kmp.basic.BasicComponent

// import top.yukonga.miuix.kmp.basic.theme.LocalColors as MiuixLocalColors

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SettingCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    shape: Shape = CardDefaults.shape,
    colors: CardColors? = null,
    elevation: CardElevation = CardDefaults.cardElevation(),
    border: BorderStroke? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val composeEngine = LegadoTheme.composeEngine
    val containerAlpha = ThemeConfig.containerOpacity / 100f

    // 当 SplicedColumnGroup 已经添加了边框时，SettingCard 不再添加边框
    // 避免边框重叠
    val borderModifier = if (!ThemeConfig.enableDeepPersonalization && ThemeConfig.enableContainerBorder && ThemeConfig.containerBorderColor != 0 && ThemeConfig.containerBorderWidth > 0) {
        val borderColor = androidx.compose.ui.graphics.Color(ThemeConfig.containerBorderColor)
        val borderWidth = ThemeConfig.containerBorderWidth.dp
        
        Modifier.border(
            width = borderWidth,
            color = borderColor,
            shape = shape
        )
    } else {
        Modifier
    }

    if (ThemeResolver.isMiuixEngine(composeEngine)) {
        BasicComponent(
            modifier = modifier.then(borderModifier),
            onClick = onClick,
            content = content
        )
    } else {
        val baseColors = colors ?: CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
        )

        val finalColors = CardDefaults.cardColors(
            containerColor = baseColors.containerColor.copy(alpha = containerAlpha),
            contentColor = baseColors.contentColor,
            disabledContainerColor = baseColors.disabledContainerColor.copy(alpha = containerAlpha),
            disabledContentColor = baseColors.disabledContentColor
        )

        if (onClick != null) {
            Card(
                onClick = onClick,
                modifier = modifier.then(borderModifier),
                shape = shape,
                colors = finalColors,
                elevation = elevation,
                content = content
            )
        } else {
            Card(
                modifier = modifier.then(borderModifier),
                shape = shape,
                colors = finalColors,
                elevation = elevation,
                content = content
            )
        }
    }
}