package com.warden.notebook

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val noteAdapter = NoteAdapter(MMKVUtil.getNotes())
        rv.adapter = noteAdapter

        rv.post { gotoBottom(noteAdapter) }
        bt_save.setOnClickListener {
            val input = et_input.text.toString()
            val newBean = NoteBean();
            newBean.content = input
            newBean.createTime = System.currentTimeMillis()
            newBean.editTime = System.currentTimeMillis()
            MMKVUtil.addNote(newBean)
            noteAdapter.add(newBean)
            et_input.setText("")
            gotoBottom(noteAdapter)
            return@setOnClickListener
        }

    }

    private fun gotoBottom(noteAdapter: NoteAdapter) {
        rv.scrollToPosition(noteAdapter.itemCount - 1)
    }
}