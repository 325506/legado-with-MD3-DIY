package io.legado.app.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.MotionScheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontFamily
import android.graphics.Typeface
import android.net.Uri
import io.legado.app.ui.config.themeConfig.ThemeConfig
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.theme.ThemeController
import io.legado.app.ui.theme.CustomColorScheme
import io.legado.app.ui.theme.ThemeResolver

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val appThemeMode = ThemeResolver.resolveThemeMode(ThemeConfig.appTheme)
    val themeModeValue = ThemeConfig.themeMode
    val isPureBlack = ThemeConfig.isPureBlack
    val paletteStyleValue = ThemeConfig.paletteStyle
    val materialVersion = ThemeConfig.materialVersion
    val composeEngine = ThemeConfig.composeEngine
    val useMiuixMonet = ThemeConfig.useMiuixMonet
    val customPrimary = ThemeConfig.cPrimary
    val appFontPath = ThemeConfig.appFontPath
    val colorSchemeMode = ThemeResolver.resolveColorSchemeMode(themeModeValue)
    val miuixColorSchemeMode = remember(themeModeValue, useMiuixMonet) {
        ThemeResolver.resolveMiuixColorSchemeMode(themeModeValue, useMiuixMonet)
    }
    
    // 加载自定义字体
    val customFontFamily = remember(ThemeConfig.appFontPath, context) {
        if (!ThemeConfig.appFontPath.isNullOrEmpty()) {
            try {
                val uri = Uri.parse(ThemeConfig.appFontPath)
                val typeface: Typeface? = if (uri.scheme == "content") {
                    context.contentResolver.openFileDescriptor(uri, "r")?.use {
                        Typeface.Builder(it.fileDescriptor).build()
                    }
                } else {
                    Typeface.createFromFile(uri.path)
                }
                typeface?.let {
                    FontFamily(it)
                }
            } catch (e: Exception) {
                null
            }
        } else {
            null
        }
    }
    
    val paletteStyle = remember(paletteStyleValue) {
        ThemeResolver.resolvePaletteStyle(paletteStyleValue)
    }

    val colorScheme = remember(
        context,
        appThemeMode,
        darkTheme,
        isPureBlack,
        paletteStyleValue,
        materialVersion
    ) {
        if (ThemeConfig.enableDeepPersonalization && 
            (ThemeConfig.cMD3Primary != 0 || 
             ThemeConfig.cMD3Secondary != 0 || 
             ThemeConfig.cMD3Surface != 0 || 
             ThemeConfig.cMD3Background != 0 ||
             ThemeConfig.cMD3SurfaceContainerLow != 0 ||
             ThemeConfig.cMD3SurfaceVariant != 0)) {
            // 使用自定义颜色角色
            val primary = if (ThemeConfig.cMD3Primary != 0) Color(ThemeConfig.cMD3Primary) else Color(0xFF6750A4)
            val onPrimary = if (ThemeConfig.cMD3OnPrimary != 0) Color(ThemeConfig.cMD3OnPrimary) else Color(0xFFFFFFFF)
            val primaryContainer = if (ThemeConfig.cMD3PrimaryContainer != 0) Color(ThemeConfig.cMD3PrimaryContainer) else Color(0xFFEADDFF)
            val onPrimaryContainer = if (ThemeConfig.cMD3OnPrimaryContainer != 0) Color(ThemeConfig.cMD3OnPrimaryContainer) else Color(0xFF21005D)
            val secondary = if (ThemeConfig.cMD3Secondary != 0) Color(ThemeConfig.cMD3Secondary) else Color(0xFF625B71)
            val onSecondary = if (ThemeConfig.cMD3OnSecondary != 0) Color(ThemeConfig.cMD3OnSecondary) else Color(0xFFFFFFFF)
            val secondaryContainer = if (ThemeConfig.cMD3SecondaryContainer != 0) Color(ThemeConfig.cMD3SecondaryContainer) else Color(0xFFE8DEF8)
            val onSecondaryContainer = if (ThemeConfig.cMD3OnSecondaryContainer != 0) Color(ThemeConfig.cMD3OnSecondaryContainer) else Color(0xFF1E192B)
            val tertiary = if (ThemeConfig.cMD3Tertiary != 0) Color(ThemeConfig.cMD3Tertiary) else Color(0xFF7D5260)
            val error = if (ThemeConfig.cMD3Error != 0) Color(ThemeConfig.cMD3Error) else Color(0xFFB3261E)
            val surface = if (ThemeConfig.cMD3Surface != 0) Color(ThemeConfig.cMD3Surface) else Color(0xFFFEF7FF)
            val onSurface = if (ThemeConfig.cMD3OnSurface != 0) Color(ThemeConfig.cMD3OnSurface) else Color(0xFF1C1B1F)
            val background = if (ThemeConfig.cMD3Background != 0) Color(ThemeConfig.cMD3Background) else Color(0xFFFEF7FF)
            val outline = if (ThemeConfig.cMD3Outline != 0) Color(ThemeConfig.cMD3Outline) else Color(0xFF79747E)
            val surfaceContainerLow = if (ThemeConfig.cMD3SurfaceContainerLow != 0) Color(ThemeConfig.cMD3SurfaceContainerLow) else Color(0xFFF7F2FA)
            val surfaceVariant = if (ThemeConfig.cMD3SurfaceVariant != 0) Color(ThemeConfig.cMD3SurfaceVariant) else Color(0xFFE7E0EC)
            
            // 由于ThemeEngine.getColorScheme不支持customColors参数，这里使用CustomColorScheme直接创建
            val style = ThemeResolver.resolvePaletteStyle(paletteStyleValue)
            val colorSpec = ThemeResolver.resolveColorSpecFromMaterialVersion(materialVersion)
            // 直接使用硬编码的默认值，避免类型推断问题
            CustomColorScheme(
                seed = 0xFF6750A4.toInt(),
                style = style,
                colorSpec = colorSpec
            ).getColorScheme(darkTheme)
                .copy(
                    primary = primary,
                    onPrimary = onPrimary,
                    primaryContainer = primaryContainer,
                    onPrimaryContainer = onPrimaryContainer,
                    secondary = secondary,
                    onSecondary = onSecondary,
                    secondaryContainer = secondaryContainer,
                    onSecondaryContainer = onSecondaryContainer,
                    tertiary = tertiary,
                    error = error,
                    surface = surface,
                    onSurface = onSurface,
                    background = background,
                    outline = outline,
                    surfaceContainerLow = surfaceContainerLow,
                    surfaceVariant = surfaceVariant
                )
        } else {
            ThemeEngine.getColorScheme(
                context = context,
                mode = appThemeMode,
                darkTheme = darkTheme,
                isAmoled = isPureBlack,
                paletteStyle = paletteStyleValue,
                materialVersion = materialVersion
            )
        }
    }

    val customSeedColor = remember(customPrimary, colorScheme.primary) {
        if (customPrimary != 0) Color(customPrimary) else colorScheme.primary
    }
    val themeSeedColor = remember(appThemeMode, customSeedColor, colorScheme.primary) {
        if (appThemeMode == AppThemeMode.Custom) customSeedColor else colorScheme.primary
    }
    val miuixPaletteStyle = remember(paletteStyleValue) {
        ThemeResolver.resolveMiuixPaletteStyle(paletteStyleValue)
    }
    val miuixColorSpec = remember(materialVersion, paletteStyleValue) {
        ThemeResolver.resolveMiuixColorSpec(materialVersion, paletteStyleValue)
    }

    val themeColors = remember(
        colorScheme,
        darkTheme,
        themeSeedColor,
        paletteStyle,
        colorSchemeMode,
        composeEngine
    ) {
        LegadoThemeMode(
            colorScheme = colorScheme,
            isDark = darkTheme,
            seedColor = themeSeedColor,
            paletteStyle = paletteStyle,
            themeMode = colorSchemeMode,
            useDynamicColor = appThemeMode == AppThemeMode.Dynamic,
            composeEngine = composeEngine
        )
    }

    CompositionLocalProvider(
        LocalLegadoThemeColors provides themeColors
    ) {
        if (ThemeResolver.isMiuixEngine(themeColors.composeEngine)) {
            val keyColor = if (useMiuixMonet &&
                themeColors.useDynamicColor &&
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
            ) {
                colorResource(id = android.R.color.system_accent1_500)
            } else {
                themeSeedColor
            }

            val controller = remember(
                miuixColorSchemeMode,
                useMiuixMonet,
                keyColor,
                miuixPaletteStyle,
                miuixColorSpec,
                darkTheme
            ) {
                if (useMiuixMonet) {
                    ThemeController(
                        colorSchemeMode = miuixColorSchemeMode,
                        keyColor = keyColor,
                        paletteStyle = miuixPaletteStyle,
                        colorSpec = miuixColorSpec,
                        isDark = darkTheme
                    )
                } else {
                    ThemeController(
                        colorSchemeMode = miuixColorSchemeMode,
                        isDark = darkTheme
                    )
                }
            }

            MiuixTheme(controller = controller) {
                val miuixStyles = MiuixTheme.textStyles
                val legadoTypography = remember<LegadoTypography>(miuixStyles, customFontFamily) {
                    val typography = miuixStylesToM3Typography(miuixStyles)
                    val baseLegadoTypography = typography.toLegadoTypography()
                    if (customFontFamily != null) {
                        baseLegadoTypography.copy(
                            headlineLarge = baseLegadoTypography.headlineLarge.copy(fontFamily = customFontFamily),
                            headlineLargeEmphasized = baseLegadoTypography.headlineLargeEmphasized.copy(fontFamily = customFontFamily),
                            headlineMedium = baseLegadoTypography.headlineMedium.copy(fontFamily = customFontFamily),
                            headlineMediumEmphasized = baseLegadoTypography.headlineMediumEmphasized.copy(fontFamily = customFontFamily),
                            headlineSmall = baseLegadoTypography.headlineSmall.copy(fontFamily = customFontFamily),
                            headlineSmallEmphasized = baseLegadoTypography.headlineSmallEmphasized.copy(fontFamily = customFontFamily),
                            titleLarge = baseLegadoTypography.titleLarge.copy(fontFamily = customFontFamily),
                            titleLargeEmphasized = baseLegadoTypography.titleLargeEmphasized.copy(fontFamily = customFontFamily),
                            titleMedium = baseLegadoTypography.titleMedium.copy(fontFamily = customFontFamily),
                            titleMediumEmphasized = baseLegadoTypography.titleMediumEmphasized.copy(fontFamily = customFontFamily),
                            titleSmall = baseLegadoTypography.titleSmall.copy(fontFamily = customFontFamily),
                            titleSmallEmphasized = baseLegadoTypography.titleSmallEmphasized.copy(fontFamily = customFontFamily),
                            bodyLarge = baseLegadoTypography.bodyLarge.copy(fontFamily = customFontFamily),
                            bodyLargeEmphasized = baseLegadoTypography.bodyLargeEmphasized.copy(fontFamily = customFontFamily),
                            bodyMedium = baseLegadoTypography.bodyMedium.copy(fontFamily = customFontFamily),
                            bodyMediumEmphasized = baseLegadoTypography.bodyMediumEmphasized.copy(fontFamily = customFontFamily),
                            bodySmall = baseLegadoTypography.bodySmall.copy(fontFamily = customFontFamily),
                            bodySmallEmphasized = baseLegadoTypography.bodySmallEmphasized.copy(fontFamily = customFontFamily),
                            labelLarge = baseLegadoTypography.labelLarge.copy(fontFamily = customFontFamily),
                            labelLargeEmphasized = baseLegadoTypography.labelLargeEmphasized.copy(fontFamily = customFontFamily),
                            labelMedium = baseLegadoTypography.labelMedium.copy(fontFamily = customFontFamily),
                            labelMediumEmphasized = baseLegadoTypography.labelMediumEmphasized.copy(fontFamily = customFontFamily),
                            labelSmall = baseLegadoTypography.labelSmall.copy(fontFamily = customFontFamily),
                            labelSmallEmphasized = baseLegadoTypography.labelSmallEmphasized.copy(fontFamily = customFontFamily)
                        )
                    } else {
                        baseLegadoTypography
                    }
                }

                val miuixColorScheme = MiuixTheme.colorScheme

                val mappedColorScheme = remember(miuixColorScheme) {
                    val customBgColor = if (ThemeConfig.enableDeepPersonalization && ThemeConfig.cBgColor != 0) {
                        Color(ThemeConfig.cBgColor)
                    } else {
                        miuixColorScheme.background
                    }
                    val customFontColor = if (ThemeConfig.enableDeepPersonalization && ThemeConfig.cFontColor != 0) {
                        Color(ThemeConfig.cFontColor)
                    } else {
                        miuixColorScheme.onSurface
                    }
                    val customTopBarColor = if (ThemeConfig.enableDeepPersonalization && ThemeConfig.cTopBarColor != 0) {
                        Color(ThemeConfig.cTopBarColor)
                    } else {
                        miuixColorScheme.surface
                    }
                    val customNavBarColor = if (ThemeConfig.enableDeepPersonalization && ThemeConfig.cNavBarColor != 0) {
                        Color(ThemeConfig.cNavBarColor)
                    } else {
                        miuixColorScheme.surface
                    }

                    LegadoColorScheme(
                        primary = miuixColorScheme.primary,
                        onPrimary = miuixColorScheme.onPrimary,
                        primaryContainer = miuixColorScheme.primaryContainer,
                        onPrimaryContainer = miuixColorScheme.onPrimaryContainer,
                        inversePrimary = miuixColorScheme.primaryVariant,

                        secondary = miuixColorScheme.secondary,
                        onSecondary = miuixColorScheme.onSecondary,
                        secondaryContainer = miuixColorScheme.secondaryContainer,
                        onSecondaryContainer = miuixColorScheme.onSecondaryContainer,

                        tertiary = miuixColorScheme.primary,
                        onTertiary = miuixColorScheme.onPrimary,
                        tertiaryContainer = miuixColorScheme.primaryContainer,
                        onTertiaryContainer = miuixColorScheme.primaryVariant,

                        background = customBgColor,
                        onBackground = customFontColor,

                        surface = miuixColorScheme.surface,
                        onSurface = customFontColor,
                        surfaceVariant = miuixColorScheme.surfaceVariant,
                        onSurfaceVariant = miuixColorScheme.onSurfaceSecondary,
                        surfaceTint = miuixColorScheme.primary,
                        inverseSurface = miuixColorScheme.onSurface,
                        inverseOnSurface = miuixColorScheme.surface,

                        error = miuixColorScheme.error,
                        onError = miuixColorScheme.onError,
                        errorContainer = miuixColorScheme.errorContainer,
                        onErrorContainer = miuixColorScheme.onErrorContainer,

                        outline = miuixColorScheme.outline,
                        outlineVariant = miuixColorScheme.dividerLine,
                        scrim = miuixColorScheme.windowDimming,

                        surfaceBright = miuixColorScheme.surface,
                        surfaceDim = miuixColorScheme.background,
                        surfaceContainer = miuixColorScheme.surfaceContainer,
                        surfaceContainerHigh = miuixColorScheme.surfaceContainerHigh,
                        surfaceContainerHighest = miuixColorScheme.surfaceContainerHighest,
                        surfaceContainerLow = miuixColorScheme.secondaryContainer,
                        surfaceContainerLowest = miuixColorScheme.background,

                        primaryFixed = miuixColorScheme.primaryContainer,
                        primaryFixedDim = miuixColorScheme.primary,
                        onPrimaryFixed = miuixColorScheme.onPrimaryContainer,
                        onPrimaryFixedVariant = miuixColorScheme.onPrimary,
                        secondaryFixed = miuixColorScheme.secondaryContainer,
                        secondaryFixedDim = miuixColorScheme.secondary,
                        onSecondaryFixed = miuixColorScheme.onSecondaryContainer,
                        onSecondaryFixedVariant = miuixColorScheme.onSecondary,
                        tertiaryFixed = miuixColorScheme.tertiaryContainer,
                        tertiaryFixedDim = miuixColorScheme.tertiaryContainerVariant,
                        onTertiaryFixed = miuixColorScheme.onTertiaryContainer,
                        onTertiaryFixedVariant = miuixColorScheme.onTertiaryContainer,

                        cardContainer = miuixColorScheme.disabledPrimary,
                        onCardContainer = miuixColorScheme.primary,
                        onSheetContent = miuixColorScheme.surface.copy(alpha = 0.5f)
                    )
                }

                CompositionLocalProvider(
                    LocalLegadoTypography provides legadoTypography,
                    LocalLegadoColorScheme provides mappedColorScheme
                ) {
                    AppBackground(darkTheme = darkTheme) { content() }
                }
            }
        } else {
            val materialTypography = remember(customFontFamily) {
                if (customFontFamily != null) {
                    Typography(
                        headlineLarge = Typography().headlineLarge.copy(fontFamily = customFontFamily),
                        headlineMedium = Typography().headlineMedium.copy(fontFamily = customFontFamily),
                        headlineSmall = Typography().headlineSmall.copy(fontFamily = customFontFamily),
                        titleLarge = Typography().titleLarge.copy(fontFamily = customFontFamily),
                        titleMedium = Typography().titleMedium.copy(fontFamily = customFontFamily),
                        titleSmall = Typography().titleSmall.copy(fontFamily = customFontFamily),
                        bodyLarge = Typography().bodyLarge.copy(fontFamily = customFontFamily),
                        bodyMedium = Typography().bodyMedium.copy(fontFamily = customFontFamily),
                        bodySmall = Typography().bodySmall.copy(fontFamily = customFontFamily),
                        labelLarge = Typography().labelLarge.copy(fontFamily = customFontFamily),
                        labelMedium = Typography().labelMedium.copy(fontFamily = customFontFamily),
                        labelSmall = Typography().labelSmall.copy(fontFamily = customFontFamily)
                    )
                } else {
                    Typography()
                }
            }
            MaterialExpressiveTheme(
                colorScheme = colorScheme,
                typography = materialTypography,
                motionScheme = MotionScheme.expressive(),
                shapes = Shapes()
            ) {
                val legadoTypography = remember(materialTypography, customFontFamily) {
                    val baseLegadoTypography = materialTypography.toLegadoTypography()
                    if (customFontFamily != null) {
                        baseLegadoTypography.copy(
                            headlineLarge = baseLegadoTypography.headlineLarge.copy(fontFamily = customFontFamily),
                            headlineLargeEmphasized = baseLegadoTypography.headlineLargeEmphasized.copy(fontFamily = customFontFamily),
                            headlineMedium = baseLegadoTypography.headlineMedium.copy(fontFamily = customFontFamily),
                            headlineMediumEmphasized = baseLegadoTypography.headlineMediumEmphasized.copy(fontFamily = customFontFamily),
                            headlineSmall = baseLegadoTypography.headlineSmall.copy(fontFamily = customFontFamily),
                            headlineSmallEmphasized = baseLegadoTypography.headlineSmallEmphasized.copy(fontFamily = customFontFamily),
                            titleLarge = baseLegadoTypography.titleLarge.copy(fontFamily = customFontFamily),
                            titleLargeEmphasized = baseLegadoTypography.titleLargeEmphasized.copy(fontFamily = customFontFamily),
                            titleMedium = baseLegadoTypography.titleMedium.copy(fontFamily = customFontFamily),
                            titleMediumEmphasized = baseLegadoTypography.titleMediumEmphasized.copy(fontFamily = customFontFamily),
                            titleSmall = baseLegadoTypography.titleSmall.copy(fontFamily = customFontFamily),
                            titleSmallEmphasized = baseLegadoTypography.titleSmallEmphasized.copy(fontFamily = customFontFamily),
                            bodyLarge = baseLegadoTypography.bodyLarge.copy(fontFamily = customFontFamily),
                            bodyLargeEmphasized = baseLegadoTypography.bodyLargeEmphasized.copy(fontFamily = customFontFamily),
                            bodyMedium = baseLegadoTypography.bodyMedium.copy(fontFamily = customFontFamily),
                            bodyMediumEmphasized = baseLegadoTypography.bodyMediumEmphasized.copy(fontFamily = customFontFamily),
                            bodySmall = baseLegadoTypography.bodySmall.copy(fontFamily = customFontFamily),
                            bodySmallEmphasized = baseLegadoTypography.bodySmallEmphasized.copy(fontFamily = customFontFamily),
                            labelLarge = baseLegadoTypography.labelLarge.copy(fontFamily = customFontFamily),
                            labelLargeEmphasized = baseLegadoTypography.labelLargeEmphasized.copy(fontFamily = customFontFamily),
                            labelMedium = baseLegadoTypography.labelMedium.copy(fontFamily = customFontFamily),
                            labelMediumEmphasized = baseLegadoTypography.labelMediumEmphasized.copy(fontFamily = customFontFamily),
                            labelSmall = baseLegadoTypography.labelSmall.copy(fontFamily = customFontFamily),
                            labelSmallEmphasized = baseLegadoTypography.labelSmallEmphasized.copy(fontFamily = customFontFamily)
                        )
                    } else {
                        baseLegadoTypography
                    }
                }
                val semanticColors = remember(colorScheme) {
                    val customBgColor = if (ThemeConfig.enableDeepPersonalization && ThemeConfig.cBgColor != 0) {
                        Color(ThemeConfig.cBgColor)
                    } else {
                        colorScheme.background
                    }
                    val customFontColor = if (ThemeConfig.enableDeepPersonalization && ThemeConfig.cFontColor != 0) {
                        Color(ThemeConfig.cFontColor)
                    } else {
                        colorScheme.onSurface
                    }
                    val customTopBarColor = if (ThemeConfig.enableDeepPersonalization && ThemeConfig.cTopBarColor != 0) {
                        Color(ThemeConfig.cTopBarColor)
                    } else {
                        colorScheme.surface
                    }
                    val customNavBarColor = if (ThemeConfig.enableDeepPersonalization && ThemeConfig.cNavBarColor != 0) {
                        Color(ThemeConfig.cNavBarColor)
                    } else {
                        colorScheme.surface
                    }

                    colorScheme.toLegadoColorScheme(
                        customBgColor = customBgColor,
                        customFontColor = customFontColor,
                        customTopBarColor = customTopBarColor,
                        customNavBarColor = customNavBarColor
                    )
                }

                CompositionLocalProvider(
                    LocalLegadoTypography provides legadoTypography,
                    LocalLegadoColorScheme provides semanticColors
                ) {
                    AppBackground(darkTheme = darkTheme) { content() }
                }
            }
        }
    }
}