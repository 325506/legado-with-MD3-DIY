package io.legado.app.ui.config.personalizationConfig

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.legado.app.R
import io.legado.app.ui.config.themeConfig.ThemeConfig
import io.legado.app.ui.theme.adaptiveContentPadding
import io.legado.app.ui.widget.components.AppScaffold
import io.legado.app.ui.widget.components.SplicedColumnGroup
import io.legado.app.ui.widget.components.button.TopBarNavigationButton
import io.legado.app.ui.widget.components.dialog.ColorPickerSheet
import io.legado.app.ui.widget.components.settingItem.ClickableSettingItem
import io.legado.app.ui.widget.components.settingItem.SwitchSettingItem
import io.legado.app.ui.widget.components.topbar.GlassMediumFlexibleTopAppBar
import io.legado.app.ui.widget.components.topbar.GlassTopAppBarDefaults

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalizationConfigScreen(
    onBackClick: () -> Unit,
    onNavigateToFontSelect: () -> Unit
) {
    val scrollBehavior = GlassTopAppBarDefaults.defaultScrollBehavior()
    var showColorPicker by remember { mutableStateOf(false) }
    var currentColorKey by remember { mutableStateOf("cTopBarColor") }

    val topBarColor = ThemeConfig.cTopBarColor
    val navBarColor = ThemeConfig.cNavBarColor
    val fontColor = ThemeConfig.cFontColor
    val bgColor = ThemeConfig.cBgColor
    val enableDeepPersonalization = ThemeConfig.enableDeepPersonalization
    
    // Material Design 3 color roles
    val md3Primary = ThemeConfig.cMD3Primary
    val md3OnPrimary = ThemeConfig.cMD3OnPrimary
    val md3PrimaryContainer = ThemeConfig.cMD3PrimaryContainer
    val md3OnPrimaryContainer = ThemeConfig.cMD3OnPrimaryContainer
    val md3Secondary = ThemeConfig.cMD3Secondary
    val md3OnSecondary = ThemeConfig.cMD3OnSecondary
    val md3SecondaryContainer = ThemeConfig.cMD3SecondaryContainer
    val md3Tertiary = ThemeConfig.cMD3Tertiary
    val md3Error = ThemeConfig.cMD3Error
    val md3Surface = ThemeConfig.cMD3Surface
    val md3OnSurface = ThemeConfig.cMD3OnSurface
    val md3Background = ThemeConfig.cMD3Background
    val md3Outline = ThemeConfig.cMD3Outline
    val md3SurfaceContainerLow = ThemeConfig.cMD3SurfaceContainerLow

    AppScaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            GlassMediumFlexibleTopAppBar(
                title = stringResource(R.string.personalization_setting),
                scrollBehavior = scrollBehavior,
                navigationIcon = {
                    TopBarNavigationButton(onClick = onBackClick)
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = adaptiveContentPadding(
                top = paddingValues.calculateTopPadding(),
                bottom = 120.dp
            )
        ) {
            item {
                SplicedColumnGroup {
                    ClickableSettingItem(
                        title = stringResource(R.string.font_setting),
                        onClick = onNavigateToFontSelect
                    )
                }
            }

            item {
                SplicedColumnGroup {
                    SwitchSettingItem(
                        title = stringResource(R.string.personalization_setting),
                        checked = enableDeepPersonalization,
                        onCheckedChange = { ThemeConfig.enableDeepPersonalization = it }
                    )
                }
            }

            item {
                SplicedColumnGroup(title = stringResource(R.string.color_setting)) {
                    // Primary colors
                    ClickableSettingItem(
                        title = "Primary",
                        option = if (md3Primary != 0) "#${Integer.toHexString(md3Primary).uppercase()}" else stringResource(R.string.click_to_select),
                        onClick = {
                            currentColorKey = "cMD3Primary"
                            showColorPicker = true
                        },
                        trailingContent = {
                            if (md3Primary != 0) {
                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .clip(CircleShape)
                                        .background(Color(md3Primary))
                                        .border(
                                            1.dp,
                                            MaterialTheme.colorScheme.outlineVariant,
                                            CircleShape
                                        )
                                )
                            }
                        }
                    )

                    ClickableSettingItem(
                        title = "On Primary",
                        option = if (md3OnPrimary != 0) "#${Integer.toHexString(md3OnPrimary).uppercase()}" else stringResource(R.string.click_to_select),
                        onClick = {
                            currentColorKey = "cMD3OnPrimary"
                            showColorPicker = true
                        },
                        trailingContent = {
                            if (md3OnPrimary != 0) {
                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .clip(CircleShape)
                                        .background(Color(md3OnPrimary))
                                        .border(
                                            1.dp,
                                            MaterialTheme.colorScheme.outlineVariant,
                                            CircleShape
                                        )
                                )
                            }
                        }
                    )

                    ClickableSettingItem(
                        title = "Primary Container",
                        option = if (md3PrimaryContainer != 0) "#${Integer.toHexString(md3PrimaryContainer).uppercase()}" else stringResource(R.string.click_to_select),
                        onClick = {
                            currentColorKey = "cMD3PrimaryContainer"
                            showColorPicker = true
                        },
                        trailingContent = {
                            if (md3PrimaryContainer != 0) {
                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .clip(CircleShape)
                                        .background(Color(md3PrimaryContainer))
                                        .border(
                                            1.dp,
                                            MaterialTheme.colorScheme.outlineVariant,
                                            CircleShape
                                        )
                                )
                            }
                        }
                    )

                    ClickableSettingItem(
                        title = "On Primary Container",
                        option = if (md3OnPrimaryContainer != 0) "#${Integer.toHexString(md3OnPrimaryContainer).uppercase()}" else stringResource(R.string.click_to_select),
                        onClick = {
                            currentColorKey = "cMD3OnPrimaryContainer"
                            showColorPicker = true
                        },
                        trailingContent = {
                            if (md3OnPrimaryContainer != 0) {
                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .clip(CircleShape)
                                        .background(Color(md3OnPrimaryContainer))
                                        .border(
                                            1.dp,
                                            MaterialTheme.colorScheme.outlineVariant,
                                            CircleShape
                                        )
                                )
                            }
                        }
                    )

                    // Secondary colors
                    ClickableSettingItem(
                        title = "Secondary",
                        option = if (md3Secondary != 0) "#${Integer.toHexString(md3Secondary).uppercase()}" else stringResource(R.string.click_to_select),
                        onClick = {
                            currentColorKey = "cMD3Secondary"
                            showColorPicker = true
                        },
                        trailingContent = {
                            if (md3Secondary != 0) {
                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .clip(CircleShape)
                                        .background(Color(md3Secondary))
                                        .border(
                                            1.dp,
                                            MaterialTheme.colorScheme.outlineVariant,
                                            CircleShape
                                        )
                                )
                            }
                        }
                    )

                    ClickableSettingItem(
                        title = "On Secondary",
                        option = if (md3OnSecondary != 0) "#${Integer.toHexString(md3OnSecondary).uppercase()}" else stringResource(R.string.click_to_select),
                        onClick = {
                            currentColorKey = "cMD3OnSecondary"
                            showColorPicker = true
                        },
                        trailingContent = {
                            if (md3OnSecondary != 0) {
                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .clip(CircleShape)
                                        .background(Color(md3OnSecondary))
                                        .border(
                                            1.dp,
                                            MaterialTheme.colorScheme.outlineVariant,
                                            CircleShape
                                        )
                                )
                            }
                        }
                    )

                    ClickableSettingItem(
                        title = "Secondary Container",
                        option = if (md3SecondaryContainer != 0) "#${Integer.toHexString(md3SecondaryContainer).uppercase()}" else stringResource(R.string.click_to_select),
                        onClick = {
                            currentColorKey = "cMD3SecondaryContainer"
                            showColorPicker = true
                        },
                        trailingContent = {
                            if (md3SecondaryContainer != 0) {
                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .clip(CircleShape)
                                        .background(Color(md3SecondaryContainer))
                                        .border(
                                            1.dp,
                                            MaterialTheme.colorScheme.outlineVariant,
                                            CircleShape
                                        )
                                )
                            }
                        }
                    )

                    // Other colors
                    ClickableSettingItem(
                        title = "Tertiary",
                        option = if (md3Tertiary != 0) "#${Integer.toHexString(md3Tertiary).uppercase()}" else stringResource(R.string.click_to_select),
                        onClick = {
                            currentColorKey = "cMD3Tertiary"
                            showColorPicker = true
                        },
                        trailingContent = {
                            if (md3Tertiary != 0) {
                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .clip(CircleShape)
                                        .background(Color(md3Tertiary))
                                        .border(
                                            1.dp,
                                            MaterialTheme.colorScheme.outlineVariant,
                                            CircleShape
                                        )
                                )
                            }
                        }
                    )

                    ClickableSettingItem(
                        title = "Error",
                        option = if (md3Error != 0) "#${Integer.toHexString(md3Error).uppercase()}" else stringResource(R.string.click_to_select),
                        onClick = {
                            currentColorKey = "cMD3Error"
                            showColorPicker = true
                        },
                        trailingContent = {
                            if (md3Error != 0) {
                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .clip(CircleShape)
                                        .background(Color(md3Error))
                                        .border(
                                            1.dp,
                                            MaterialTheme.colorScheme.outlineVariant,
                                            CircleShape
                                        )
                                )
                            }
                        }
                    )

                    ClickableSettingItem(
                        title = "Surface",
                        option = if (md3Surface != 0) "#${Integer.toHexString(md3Surface).uppercase()}" else stringResource(R.string.click_to_select),
                        onClick = {
                            currentColorKey = "cMD3Surface"
                            showColorPicker = true
                        },
                        trailingContent = {
                            if (md3Surface != 0) {
                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .clip(CircleShape)
                                        .background(Color(md3Surface))
                                        .border(
                                            1.dp,
                                            MaterialTheme.colorScheme.outlineVariant,
                                            CircleShape
                                        )
                                )
                            }
                        }
                    )

                    ClickableSettingItem(
                        title = "On Surface",
                        option = if (md3OnSurface != 0) "#${Integer.toHexString(md3OnSurface).uppercase()}" else stringResource(R.string.click_to_select),
                        onClick = {
                            currentColorKey = "cMD3OnSurface"
                            showColorPicker = true
                        },
                        trailingContent = {
                            if (md3OnSurface != 0) {
                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .clip(CircleShape)
                                        .background(Color(md3OnSurface))
                                        .border(
                                            1.dp,
                                            MaterialTheme.colorScheme.outlineVariant,
                                            CircleShape
                                        )
                                )
                            }
                        }
                    )

                    ClickableSettingItem(
                        title = "Background",
                        option = if (md3Background != 0) "#${Integer.toHexString(md3Background).uppercase()}" else stringResource(R.string.click_to_select),
                        onClick = {
                            currentColorKey = "cMD3Background"
                            showColorPicker = true
                        },
                        trailingContent = {
                            if (md3Background != 0) {
                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .clip(CircleShape)
                                        .background(Color(md3Background))
                                        .border(
                                            1.dp,
                                            MaterialTheme.colorScheme.outlineVariant,
                                            CircleShape
                                        )
                                )
                            }
                        }
                    )

                    ClickableSettingItem(
                        title = "Outline",
                        option = if (md3Outline != 0) "#${Integer.toHexString(md3Outline).uppercase()}" else stringResource(R.string.click_to_select),
                        onClick = {
                            currentColorKey = "cMD3Outline"
                            showColorPicker = true
                        },
                        trailingContent = {
                            if (md3Outline != 0) {
                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .clip(CircleShape)
                                        .background(Color(md3Outline))
                                        .border(
                                            1.dp,
                                            MaterialTheme.colorScheme.outlineVariant,
                                            CircleShape
                                        )
                                )
                            }
                        }
                    )

                    ClickableSettingItem(
                        title = "Surface Container Low",
                        option = if (md3SurfaceContainerLow != 0) "#${Integer.toHexString(md3SurfaceContainerLow).uppercase()}" else stringResource(R.string.click_to_select),
                        onClick = {
                            currentColorKey = "cMD3SurfaceContainerLow"
                            showColorPicker = true
                        },
                        trailingContent = {
                            if (md3SurfaceContainerLow != 0) {
                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .clip(CircleShape)
                                        .background(Color(md3SurfaceContainerLow))
                                        .border(
                                            1.dp,
                                            MaterialTheme.colorScheme.outlineVariant,
                                            CircleShape
                                        )
                                )
                            }
                        }
                    )
                }
            }
        }

        ColorPickerSheet(
            show = showColorPicker,
            initialColor = when (currentColorKey) {
                "cMD3Primary" -> md3Primary
                "cMD3OnPrimary" -> md3OnPrimary
                "cMD3PrimaryContainer" -> md3PrimaryContainer
                "cMD3OnPrimaryContainer" -> md3OnPrimaryContainer
                "cMD3Secondary" -> md3Secondary
                "cMD3OnSecondary" -> md3OnSecondary
                "cMD3SecondaryContainer" -> md3SecondaryContainer
                "cMD3Tertiary" -> md3Tertiary
                "cMD3Error" -> md3Error
                "cMD3Surface" -> md3Surface
                "cMD3OnSurface" -> md3OnSurface
                "cMD3Background" -> md3Background
                "cMD3Outline" -> md3Outline
                "cMD3SurfaceContainerLow" -> md3SurfaceContainerLow
                else -> 0
            },
            onDismissRequest = { showColorPicker = false },
            onColorSelected = {
                when (currentColorKey) {
                    "cMD3Primary" -> ThemeConfig.cMD3Primary = it
                    "cMD3OnPrimary" -> ThemeConfig.cMD3OnPrimary = it
                    "cMD3PrimaryContainer" -> ThemeConfig.cMD3PrimaryContainer = it
                    "cMD3OnPrimaryContainer" -> ThemeConfig.cMD3OnPrimaryContainer = it
                    "cMD3Secondary" -> ThemeConfig.cMD3Secondary = it
                    "cMD3OnSecondary" -> ThemeConfig.cMD3OnSecondary = it
                    "cMD3SecondaryContainer" -> ThemeConfig.cMD3SecondaryContainer = it
                    "cMD3Tertiary" -> ThemeConfig.cMD3Tertiary = it
                    "cMD3Error" -> ThemeConfig.cMD3Error = it
                    "cMD3Surface" -> ThemeConfig.cMD3Surface = it
                    "cMD3OnSurface" -> ThemeConfig.cMD3OnSurface = it
                    "cMD3Background" -> ThemeConfig.cMD3Background = it
                    "cMD3Outline" -> ThemeConfig.cMD3Outline = it
                    "cMD3SurfaceContainerLow" -> ThemeConfig.cMD3SurfaceContainerLow = it
                }
            }
        )
    }
}
