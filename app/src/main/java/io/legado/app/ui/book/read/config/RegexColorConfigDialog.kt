package io.legado.app.ui.book.read.config

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import io.legado.app.R
import io.legado.app.base.BaseBottomSheetDialogFragment
import io.legado.app.constant.EventBus
import io.legado.app.databinding.DialogRegexColorConfigBinding
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.legado.app.help.config.ReadBookConfig
import io.legado.app.lib.dialogs.alert
import io.legado.app.ui.book.read.ReadBookActivity
import io.legado.app.utils.postEvent
import io.legado.app.utils.viewbindingdelegate.viewBinding

class RegexColorConfigDialog : BaseBottomSheetDialogFragment(R.layout.dialog_regex_color_config) {

    private val binding by viewBinding(DialogRegexColorConfigBinding::bind)
    private val callBack2 get() = activity as? ReadBookActivity
    private lateinit var adapter: RegexColorRuleAdapter

    override fun onFragmentCreated(view: View, savedInstanceState: Bundle?) {
        adapter = RegexColorRuleAdapter { position ->
            deleteRule(position)
        }
        initView()
        initViewEvent()
    }

    private fun initView() = binding.run {
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
        adapter.setItems(ReadBookConfig.regexColorRules)
    }

    private fun initViewEvent() = binding.run {
        btnAddRule.setOnClickListener {
            showAddRuleDialog()
        }
    }

    private fun showAddRuleDialog() {
        val defaultPatterns = listOf(
            "\u201C匹配内容\u201D" to "\u201C.+?\u201D",
            "《匹配内容》" to "《.+?》",
            "\"匹配内容\"" to "\".+?\""
        )
        val displayItems = defaultPatterns.map { it.first } + "自定义规则"
        context?.alert(title = "添加正则规则") {
            items(displayItems) { _, i ->
                if (i < defaultPatterns.size) {
                    val (name, pattern) = defaultPatterns[i]
                    addRule(name, pattern)
                } else {
                    showCustomRuleDialog()
                }
            }
        }
    }

    private fun showCustomRuleDialog() {
        val editText = android.widget.EditText(context).apply {
            hint = "输入正则表达式，如：\\u201C.+?\\u201D"
        }
        context?.alert(title = "自定义正则规则") {
            customView { editText }
            okButton {
                val pattern = editText.text.toString().trim()
                if (pattern.isNotEmpty()) {
                    addRule(pattern, pattern)
                }
            }
            cancelButton()
        }
    }

    private fun addRule(name: String, pattern: String) {
        val rule = RegexColorRule(name, pattern, ReadBookConfig.durConfig.curTextAccentColor())
        ReadBookConfig.regexColorRules.add(rule)
        notifyConfigChanged()
    }
    
    private fun deleteRule(position: Int) {
        if (position >= 0 && position < ReadBookConfig.regexColorRules.size) {
            ReadBookConfig.regexColorRules.removeAt(position)
            notifyConfigChanged()
        }
    }

    private fun notifyConfigChanged() {
        ReadBookConfig.saveRegexColorRules()
        adapter.setItems(ReadBookConfig.regexColorRules)
        postEvent(EventBus.UP_CONFIG, arrayListOf(8, 5))
    }
}

data class RegexColorRule(
    var name: String,
    var pattern: String,
    var color: Int
)

class RegexColorRuleAdapter(
    private val onDeleteClick: ((Int) -> Unit)? = null
) : RecyclerView.Adapter<RegexColorRuleAdapter.ViewHolder>() {

    private var items: List<RegexColorRule> = emptyList()

    fun setItems(items: List<RegexColorRule>) {
        this.items = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_regex_color_rule, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item, position)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(itemView: android.view.View) : RecyclerView.ViewHolder(itemView) {
        val tvRuleName = itemView.findViewById<android.widget.TextView>(R.id.tv_rule_name)
        val tvRulePattern = itemView.findViewById<android.widget.TextView>(R.id.tv_rule_pattern)
        val viewColor = itemView.findViewById<android.view.View>(R.id.view_color)
        val btnDelete = itemView.findViewById<android.widget.ImageButton>(R.id.btn_delete)

        fun bind(item: RegexColorRule, position: Int) {
            tvRuleName.text = item.name
            tvRulePattern.text = item.pattern
            viewColor.setBackgroundColor(item.color)
            btnDelete.setOnClickListener {
                onDeleteClick?.invoke(position)
            }
        }
    }
}
