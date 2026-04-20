package io.legado.app.ui.config.personalizationConfig

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.legado.app.R
import io.legado.app.help.config.PersonalizationThemeConfig
import io.legado.app.ui.theme.LegadoTheme
import io.legado.app.ui.widget.components.alert.AppAlertDialog
import io.legado.app.ui.widget.components.button.MediumIconButton
import io.legado.app.ui.widget.components.card.GlassCard
import io.legado.app.ui.widget.components.modalBottomSheet.AppModalBottomSheet
import io.legado.app.ui.widget.components.text.AppText
import io.legado.app.utils.GSON
import io.legado.app.utils.getClipText
import io.legado.app.utils.share
import io.legado.app.utils.toastOnUi

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalizationThemeListDialog(
    show: Boolean,
    onDismissRequest: () -> Unit,
    listVersion: Int
) {
    val context = LocalContext.current
    var deleteIndex by remember { mutableStateOf<Int?>(null) }
    val themeList = remember(listVersion) { PersonalizationThemeConfig.configList.toList() }

    AppModalBottomSheet(
        show = show,
        onDismissRequest = onDismissRequest,
        title = stringResource(R.string.theme_list),
        endAction = {
            MediumIconButton(
                onClick = {
                    val clipText = context.getClipText()
                    if (clipText != null && PersonalizationThemeConfig.addConfig(clipText)) {
                        // 导入成功，列表会通过外部的 listVersion 更新
                    } else {
                        context.toastOnUi("导入失败")
                    }
                },
                imageVector = Icons.Default.FileDownload
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                itemsIndexed(themeList, key = { _, item -> item.themeName }) { index, item ->
                    GlassCard(
                        onClick = {
                            PersonalizationThemeConfig.applyConfig(item)
                            onDismissRequest()
                        },
                        modifier = Modifier
                            .fillMaxWidth(),
                        cornerRadius = 20.dp,
                        containerColor = MaterialTheme.colorScheme.surfaceContainer
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 16.dp, end = 8.dp, top = 12.dp, bottom = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .padding(end = 12.dp)
                                    .background(
                                        color = if (item.md3Primary != 0) Color(item.md3Primary) else MaterialTheme.colorScheme.primary,
                                        shape = MaterialTheme.shapes.large
                                    )
                                    .padding(8.dp)
                            )
                            AppText(
                                text = item.themeName,
                                style = LegadoTheme.typography.bodyLarge,
                                modifier = Modifier.weight(1f)
                            )
                            IconButton(
                                onClick = {
                                    val json = GSON.toJson(item)
                                    context.share(json, "个性化主题分享")
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Share,
                                    contentDescription = stringResource(R.string.share)
                                )
                            }
                            IconButton(onClick = { deleteIndex = index }) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = stringResource(R.string.delete)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    AppAlertDialog(
        data = deleteIndex,
        onDismissRequest = { deleteIndex = null },
        title = stringResource(R.string.delete),
        confirmText = stringResource(android.R.string.ok),
        onConfirm = { index ->
            PersonalizationThemeConfig.delConfig(index)
            deleteIndex = null
        },
        dismissText = stringResource(android.R.string.cancel),
        onDismiss = { deleteIndex = null }
    )
}
