@file:Suppress("DEPRECATION")

package io.legado.app.ui.rss.article

import android.app.Activity
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.appcompat.app.AppCompatActivity
import io.legado.app.R
import io.legado.app.data.entities.RssReadRecord
import io.legado.app.ui.login.SourceLoginActivity
import io.legado.app.ui.rss.read.RedirectPolicy
import io.legado.app.ui.rss.source.edit.RssSourceEditActivity
import io.legado.app.ui.widget.dialog.VariableDialog
import io.legado.app.utils.StartActivityContract
import io.legado.app.utils.showDialogFragment
import io.legado.app.utils.startActivity
import io.legado.app.utils.toastOnUi
import io.legado.app.utils.GSON
import io.legado.app.utils.fromJsonObject
import androidx.compose.runtime.saveable.rememberSaveable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.compose.koinViewModel
import androidx.compose.ui.res.stringResource

@Composable
fun RssSortRouteScreen(
    sourceUrl: String?,
    initialSortUrl: String?,
    onBackClick: () -> Unit,
    onOpenRead: (title: String?, origin: String, link: String?, openUrl: String?) -> Unit,
    viewModel: RssSortViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val activity = LocalActivity.current as? AppCompatActivity
    val scope = rememberCoroutineScope()

    var sortList by remember { mutableStateOf<List<Pair<String, String>>>(emptyList()) }
    var articleStyle by rememberSaveable { mutableIntStateOf(0) }
    var redirectPolicy by rememberSaveable { mutableStateOf(RedirectPolicy.ALLOW_ALL) }
    var screenTitle by rememberSaveable { mutableStateOf("") }
    var showStartPage by rememberSaveable { mutableStateOf(false) }
    var isSourceLoaded by rememberSaveable { mutableStateOf(false) }
    var searchKey by rememberSaveable { mutableStateOf<String?>(null) }
    var pendingSortUrl by rememberSaveable { mutableStateOf<String?>(initialSortUrl) }
    val setSourceVariableText = stringResource(R.string.set_source_variable)
    val errorText = stringResource(R.string.error)

    var showReadRecordSheet by remember { mutableStateOf(false) }
    var readRecords by remember { mutableStateOf<List<RssReadRecord>>(emptyList()) }
    var hasNavigatedFromStartPage by rememberSaveable { mutableStateOf(false) }

    fun reloadSourceState() {
        viewModel.initData(sourceUrl) {
            scope.launch {
                val loadedSortList = viewModel.loadSorts()
                articleStyle = viewModel.currentArticleStyle()
                screenTitle = viewModel.rssSource?.sourceName.orEmpty()
                redirectPolicy = RedirectPolicy.fromString(viewModel.rssSource?.redirectPolicy)
                if (!hasNavigatedFromStartPage) {
                    val newShowStartPage = !viewModel.rssSource?.startHtml.isNullOrBlank()
                    showStartPage = newShowStartPage
                }
                if (initialSortUrl?.contains("@js:") == true && loadedSortList.none { it.second == initialSortUrl }) {
                    sortList = loadedSortList + Pair("链接", initialSortUrl)
                } else {
                    sortList = loadedSortList
                }
                isSourceLoaded = true
            }
        }
    }

    val editSourceResult = rememberLauncherForActivityResult(
        StartActivityContract(RssSourceEditActivity::class.java)
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            reloadSourceState()
        }
    }

    androidx.compose.runtime.LaunchedEffect(sourceUrl) {
        reloadSourceState()
    }

    if (!isSourceLoaded) {
        return
    }

    if (showStartPage) {
        RssStartPage(
            rssSource = viewModel.rssSource,
            onNavigateToArticles = { sortUrl ->
                hasNavigatedFromStartPage = true
                if (!sortUrl.isNullOrBlank()) {
                    val parsedSorts = GSON.fromJsonObject<Map<String, String>>(sortUrl).getOrNull()
                    if (!parsedSorts.isNullOrEmpty()) {
                        sortList = parsedSorts.entries.map { (name, url) ->
                            Pair(name, url.trim().trim('`'))
                        }
                        pendingSortUrl = sortList.firstOrNull()?.second
                    } else {
                        pendingSortUrl = sortUrl.trim().trim('`')
                    }
                }
                showStartPage = false
            },
            modifier = Modifier.fillMaxSize()
        )
        return
    }

    val displaySortList = if (searchKey != null) {
        val searchUrl = viewModel.rssSource?.searchUrl
        if (!searchUrl.isNullOrBlank()) {
            listOf(Pair("搜索", searchUrl))
        } else {
            emptyList()
        }
    } else {
        sortList
    }

    RssSortScreen(
        title = screenTitle.ifBlank { stringResource(R.string.rss) },
        sortList = displaySortList,
        preferredSortUrl = pendingSortUrl,
        hasLogin = !viewModel.rssSource?.loginUrl.isNullOrBlank(),
        hasSearch = !viewModel.rssSource?.searchUrl.isNullOrBlank(),
        searchKey = searchKey,
        redirectPolicy = redirectPolicy,
        showReadRecordSheet = showReadRecordSheet,
        readRecords = readRecords,
        onBackClick = {
            if (searchKey != null) {
                searchKey = null
            } else {
                onBackClick()
            }
        },
        onLogin = {
            context.startActivity<SourceLoginActivity> {
                putExtra("type", "rssSource")
                putExtra("key", viewModel.rssSource?.sourceUrl)
            }
        },
        onRefreshSort = {
            viewModel.clearSortCache {
                scope.launch { sortList = viewModel.loadSorts() }
            }
        },
        onSetSourceVariable = {
            scope.launch {
                val source = viewModel.rssSource
                if (source == null) {
                    context.toastOnUi("源不存在")
                    return@launch
                }
                val comment = source.getDisplayVariableComment("源变量可在js中通过source.getVariable()获取")
                val variable = withContext(Dispatchers.IO) { source.getVariable() }
                activity?.showDialogFragment(
                    VariableDialog(
                        setSourceVariableText,
                        source.getKey(),
                        variable,
                        comment
                    )
                )
            }
        },
        onEditSource = {
            viewModel.rssSource?.sourceUrl?.let { srcUrl ->
                editSourceResult.launch { putExtra("sourceUrl", srcUrl) }
            }
        },
        onSwitchLayout = {
            viewModel.switchLayout()
            articleStyle = viewModel.currentArticleStyle()
        },
        onReadRecord = {
            scope.launch(Dispatchers.IO) {
                val records = viewModel.getRecords()
                withContext(Dispatchers.Main) {
                    readRecords = records
                    showReadRecordSheet = true
                }
            }
        },
        onDismissReadRecord = { showReadRecordSheet = false },
        onClearReadRecord = {
            viewModel.deleteAllRecord()
            readRecords = emptyList()
        },
        onOpenReadRecord = { record ->
            showReadRecordSheet = false
            val openOrigin = record.origin.ifBlank {
                viewModel.rssSource?.sourceUrl ?: sourceUrl.orEmpty()
            }
            if (openOrigin.isBlank()) {
                context.toastOnUi(errorText)
            } else {
                onOpenRead(record.title, openOrigin, null, record.record)
            }
        },
        onClearArticles = { viewModel.clearArticles() },
        onRedirectPolicyChanged = { policy ->
            viewModel.rssSource?.let { source ->
                viewModel.updateRssSourceRedirectPolicy(source.sourceUrl, policy.name)
                redirectPolicy = policy
            }
            context.toastOnUi("重定向策略已更新")
        },
        onSearch = { query ->
            searchKey = query
        },
        pagerContent = { _, sort ->
            val pageViewModel: RssArticlesViewModel = koinViewModel(
                key = "rss_${viewModel.url}_${sort.first}_${sort.second}_$searchKey"
            )
            RssArticlesPage(
                sortName = if (searchKey != null) "搜索" else sort.first,
                sortUrl = sort.second,
                articleStyle = articleStyle,
                rssUrl = viewModel.url,
                rssSource = viewModel.rssSource,
                viewModel = pageViewModel,
                searchKey = searchKey,
                onRead = { article ->
                    viewModel.read(article)
                    onOpenRead(article.title, article.origin, article.link, null)
                }
            )
        }
    )
}
