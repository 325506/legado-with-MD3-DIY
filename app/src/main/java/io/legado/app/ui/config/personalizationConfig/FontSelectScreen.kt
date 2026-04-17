package io.legado.app.ui.config.personalizationConfig

import android.content.Context
import android.graphics.Typeface
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.documentfile.provider.DocumentFile
import io.legado.app.R
import io.legado.app.ui.theme.adaptiveContentPadding
import io.legado.app.ui.widget.components.AppScaffold
import io.legado.app.ui.widget.components.button.TopBarNavigationButton
import io.legado.app.ui.widget.components.topbar.GlassMediumFlexibleTopAppBar
import io.legado.app.ui.widget.components.topbar.GlassTopAppBarDefaults
import io.legado.app.utils.FileDoc
import io.legado.app.utils.listFileDocs
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FontSelectScreen(
    onBackClick: () -> Unit
) {
    val scrollBehavior = GlassTopAppBarDefaults.defaultScrollBehavior()
    val context = LocalContext.current
    var fontItems by remember { mutableStateOf<List<FileDoc>>(emptyList()) }
    var selectedFolderUri by remember { mutableStateOf<Uri?>(null) }

    // 加载字体文件
    fun loadFonts() {
        fontItems = loadFontFiles(context, selectedFolderUri)
    }

    // 初始加载
    remember {
        loadFonts()
    }

    // 文件夹选择启动器
    val selectFolderLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocumentTree()
    ) { uri ->
        if (uri != null) {
            selectedFolderUri = uri
            loadFonts()
        }
    }

    AppScaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            GlassMediumFlexibleTopAppBar(
                title = stringResource(R.string.font_setting),
                scrollBehavior = scrollBehavior,
                navigationIcon = {
                    TopBarNavigationButton(onClick = onBackClick)
                }
            )
        }
    ) { paddingValues ->
        if (fontItems.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "没有字体文件",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    Button(onClick = { selectFolderLauncher.launch(null) }) {
                        Text(text = stringResource(R.string.select_font))
                    }
                }
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                contentPadding = adaptiveContentPadding(
                    top = paddingValues.calculateTopPadding(),
                    bottom = 120.dp
                ),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(fontItems) {
                    FontItem(it)
                }
            }
        }
    }
}

@Composable
fun FontItem(fontDoc: FileDoc) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = {
            // 选择字体的逻辑
        }
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = fontDoc.name,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

private fun loadFontFiles(context: Context, folderUri: Uri?): List<FileDoc> {
    val fontRegex = Regex("(?i).*\\.[ot]tf")
    
    if (folderUri != null) {
        // 从选择的文件夹加载字体文件
        val documentFile = DocumentFile.fromTreeUri(context, folderUri)
        val fontFiles = mutableListOf<FileDoc>()
        
        documentFile?.listFiles()?.forEach { docFile ->
            if (docFile.isFile && docFile.name?.matches(fontRegex) == true) {
                // 创建FileDoc对象
                val fileDoc = FileDoc(
                    name = docFile.name ?: "",
                    isDir = false,
                    size = docFile.length(),
                    lastModified = docFile.lastModified(),
                    uri = docFile.uri
                )
                fontFiles.add(fileDoc)
            }
        }
        
        return fontFiles
    } else {
        // 使用默认的字体路径
        val fontPath = "${context.getExternalFilesDir(null)?.absolutePath}/font"
        return File(fontPath).listFileDocs {
            it.name.matches(fontRegex)
        }
    }
}
