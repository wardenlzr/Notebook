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
import androidx.recyclerview.widget.RecyclerView
import com.tencent.mmkv.MMKV

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
        val noteBean = list[position]
        holder.content?.text = noteBean.content
        val formatDateTime = formatDateTime(holder.itemView.context, noteBean.editTime, FORMAT_SHOW_DATE) +
            formatDateTime(holder.itemView.context, noteBean.editTime, FORMAT_SHOW_TIME)
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
            Log.e("position", "position.$position")
            val layoutPosition = holder.layoutPosition
            Log.e("position", "layoutPosition.$layoutPosition")

            list.removeAt(layoutPosition)
            MMKVUtil.setNotes(list)
            notifyItemRemoved(layoutPosition)
            makeText(it.context, "已删除", Toast.LENGTH_SHORT).show()
            return@setOnLongClickListener false
        }
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