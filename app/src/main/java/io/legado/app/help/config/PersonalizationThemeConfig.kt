package io.legado.app.help.config

import io.legado.app.constant.PreferKey
import io.legado.app.ui.config.themeConfig.ThemeConfig
import io.legado.app.utils.GSON
import io.legado.app.utils.FileUtils
import io.legado.app.utils.hexString
import splitties.init.appCtx
import java.io.File

object PersonalizationThemeConfig {
    const val configFileName = "personalizationThemeConfig.json"
    val configFilePath = FileUtils.getPath(appCtx.filesDir, configFileName)

    private val _configList: ArrayList<Config> by lazy {
        val cList = getConfigs() ?: emptyList()
        ArrayList(cList)
    }

    val configList: List<Config> get() = _configList

    fun save() {
        val json = GSON.toJson(_configList)
        FileUtils.delete(configFilePath)
        FileUtils.createFileIfNotExist(configFilePath).writeText(json)
    }

    fun delConfig(index: Int) {
        _configList.removeAt(index)
        save()
    }

    fun addConfig(json: String): Boolean {
        kotlin.runCatching {
            val config = GSON.fromJson(json, Config::class.java)
            addConfig(config)
            return true
        }.onFailure {}
        return false
    }

    fun addConfig(newConfig: Config) {
        _configList.forEachIndexed { index, config ->
            if (newConfig.themeName == config.themeName) {
                _configList[index] = newConfig
                save()
                return
            }
        }
        _configList.add(newConfig)
        save()
    }

    private fun getConfigs(): List<Config>? {
        val configFile = File(configFilePath)
        if (configFile.exists()) {
            kotlin.runCatching {
                val json = configFile.readText()
                val type = object : com.google.gson.reflect.TypeToken<List<Config>>() {}.type
                return GSON.fromJson(json, type)
            }
        }
        return null
    }

    fun applyConfig(config: Config) {
        // 应用颜色设置
        ThemeConfig.cMD3Primary = config.md3Primary
        ThemeConfig.cMD3OnPrimary = config.md3OnPrimary
        ThemeConfig.cMD3PrimaryContainer = config.md3PrimaryContainer
        ThemeConfig.cMD3OnPrimaryContainer = config.md3OnPrimaryContainer
        ThemeConfig.cMD3Secondary = config.md3Secondary
        ThemeConfig.cMD3OnSecondary = config.md3OnSecondary
        ThemeConfig.cMD3SecondaryContainer = config.md3SecondaryContainer
        ThemeConfig.cMD3Tertiary = config.md3Tertiary
        ThemeConfig.cMD3Error = config.md3Error
        ThemeConfig.cMD3Surface = config.md3Surface
        ThemeConfig.cMD3OnSurface = config.md3OnSurface
        ThemeConfig.cMD3Background = config.md3Background
        ThemeConfig.cMD3Outline = config.md3Outline
        ThemeConfig.cMD3SurfaceContainerLow = config.md3SurfaceContainerLow
        ThemeConfig.cMD3SurfaceVariant = config.md3SurfaceVariant

        // 应用非 MD3 颜色设置
        ThemeConfig.cTopBarColor = config.topBarColor
        ThemeConfig.cNavBarColor = config.navBarColor
        ThemeConfig.cFontColor = config.fontColor
        ThemeConfig.cBgColor = config.bgColor
        ThemeConfig.cBookInfoInputColor = config.bookInfoInputColor

        // 应用字体设置
        ThemeConfig.appFontPath = config.appFontPath

        // 应用边框设置
        ThemeConfig.enableContainerBorder = config.enableContainerBorder
        ThemeConfig.containerBorderWidth = config.containerBorderWidth
        ThemeConfig.containerBorderStyle = config.containerBorderStyle
        ThemeConfig.containerBorderDashWidth = config.containerBorderDashWidth
        ThemeConfig.containerBorderColor = config.containerBorderColor

        // 应用中间单线设置
        ThemeConfig.enableItemDivider = config.enableItemDivider
        ThemeConfig.itemDividerWidth = config.itemDividerWidth
        ThemeConfig.itemDividerLength = config.itemDividerLength
        ThemeConfig.itemDividerColor = config.itemDividerColor
    }

    fun savePersonalizationTheme(name: String): Config {
        val config = Config(
            themeName = name,
            md3Primary = ThemeConfig.cMD3Primary,
            md3OnPrimary = ThemeConfig.cMD3OnPrimary,
            md3PrimaryContainer = ThemeConfig.cMD3PrimaryContainer,
            md3OnPrimaryContainer = ThemeConfig.cMD3OnPrimaryContainer,
            md3Secondary = ThemeConfig.cMD3Secondary,
            md3OnSecondary = ThemeConfig.cMD3OnSecondary,
            md3SecondaryContainer = ThemeConfig.cMD3SecondaryContainer,
            md3Tertiary = ThemeConfig.cMD3Tertiary,
            md3Error = ThemeConfig.cMD3Error,
            md3Surface = ThemeConfig.cMD3Surface,
            md3OnSurface = ThemeConfig.cMD3OnSurface,
            md3Background = ThemeConfig.cMD3Background,
            md3Outline = ThemeConfig.cMD3Outline,
            md3SurfaceContainerLow = ThemeConfig.cMD3SurfaceContainerLow,
            md3SurfaceVariant = ThemeConfig.cMD3SurfaceVariant,
            topBarColor = ThemeConfig.cTopBarColor,
            navBarColor = ThemeConfig.cNavBarColor,
            fontColor = ThemeConfig.cFontColor,
            bgColor = ThemeConfig.cBgColor,
            bookInfoInputColor = ThemeConfig.cBookInfoInputColor,
            appFontPath = ThemeConfig.appFontPath,
            enableContainerBorder = ThemeConfig.enableContainerBorder,
            containerBorderWidth = ThemeConfig.containerBorderWidth,
            containerBorderStyle = ThemeConfig.containerBorderStyle,
            containerBorderDashWidth = ThemeConfig.containerBorderDashWidth,
            containerBorderColor = ThemeConfig.containerBorderColor,
            enableItemDivider = ThemeConfig.enableItemDivider,
            itemDividerWidth = ThemeConfig.itemDividerWidth,
            itemDividerLength = ThemeConfig.itemDividerLength,
            itemDividerColor = ThemeConfig.itemDividerColor
        )
        addConfig(config)
        return config
    }

    data class Config(
        var themeName: String,
        var md3Primary: Int,
        var md3OnPrimary: Int,
        var md3PrimaryContainer: Int,
        var md3OnPrimaryContainer: Int,
        var md3Secondary: Int,
        var md3OnSecondary: Int,
        var md3SecondaryContainer: Int,
        var md3Tertiary: Int,
        var md3Error: Int,
        var md3Surface: Int,
        var md3OnSurface: Int,
        var md3Background: Int,
        var md3Outline: Int,
        var md3SurfaceContainerLow: Int,
        var md3SurfaceVariant: Int,
        var topBarColor: Int,
        var navBarColor: Int,
        var fontColor: Int,
        var bgColor: Int,
        var bookInfoInputColor: Int,
        var appFontPath: String?,
        var enableContainerBorder: Boolean,
        var containerBorderWidth: Float,
        var containerBorderStyle: String,
        var containerBorderDashWidth: Float,
        var containerBorderColor: Int,
        var enableItemDivider: Boolean,
        var itemDividerWidth: Float,
        var itemDividerLength: Float,
        var itemDividerColor: Int
    )
}
