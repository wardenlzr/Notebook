package com.warden.notebook

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.text.format.DateUtils.*
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.dialog_edit.view.*

class NoteAdapter(private var list: MutableList<NoteBean>) :
    RecyclerView.Adapter<NoteAdapter.Holder>() {
    private var cm: ClipboardManager? = null

    fun add(addBean: NoteBean){
        list.add(addBean)
        notifyItemInserted(list.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        //构造一个ClipboardManager类，也就是剪切板管理器类
        cm = parent.context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        return Holder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false)
        )
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val context = holder.itemView.context
        val noteBean = list[position]
        holder.content?.text = noteBean.content
        val flag = FORMAT_SHOW_DATE or FORMAT_SHOW_WEEKDAY or FORMAT_SHOW_TIME
        val formatDateTime = formatDateTime(context, noteBean.editTime, flag)
        holder.time?.text = formatDateTime
        holder.itemView.setOnClickListener {
            Log.e("position", "position.$position")
            //将数据转换为ClipData类
            val str: ClipData = ClipData.newPlainText("Label", holder.content?.text.toString())
            //最后将数据复制到系统剪切板上
            cm?.setPrimaryClip(str)
            makeText(it.context, "已复制到粘贴板", Toast.LENGTH_SHORT).show()
        }
        holder.itemView.setOnLongClickListener {
            val editView = LayoutInflater.from(context).inflate(R.layout.dialog_edit, null, false)
            editView.et_input.setText(noteBean.content)
            AlertDialog.Builder(context)
                .setTitle("编辑")
                .setView(editView)
                .setPositiveButton("保存") { _, _ ->
                    noteBean.editTime = System.currentTimeMillis()
                    noteBean.content = editView.et_input.text.toString()
                    list[position] = noteBean
                    MMKVUtil.setNotes(list)
                    notifyDataSetChanged()
                }
                .setNeutralButton("删除"){ _, _ ->
                    removeDialog(holder.layoutPosition, context)
                }
                .setNegativeButton("取消", null)
                .create()
                .show()
            return@setOnLongClickListener false
        }
    }

    private fun removeDialog(position: Int, context: Context) {
        AlertDialog.Builder(context)
            .setTitle("确定删除")
            .setPositiveButton("确定") { _, _ ->
                remove(position, context);
            }
            .setNeutralButton("取消", null)
            .create()
            .show()
    }

    private fun remove(position: Int, context: Context) {
        Log.e("position", "layoutPosition.$position")
        list.removeAt(position)
        MMKVUtil.setNotes(list)
        notifyItemRemoved(position)
        makeText(context, "已删除", Toast.LENGTH_SHORT).show()
    }

    override fun getItemCount(): Int = list.size

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var content: TextView? = null
        var time: TextView? = null

        init {
            content = itemView.findViewById(R.id.content)
            time = itemView.findViewById(R.id.time)
        }
    }
}