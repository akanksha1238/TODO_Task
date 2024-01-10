package com.example.todoappkotlin

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todoappkotlin.Adapter.ToDoAdapter
import com.example.todoappkotlin.Model.ToDoModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.example.todoappkotlin.Utils.DataBaseHelper
import java.util.ArrayList
import java.util.Collections

class MainActivity : AppCompatActivity(), OnDialogCloseListener {
    private lateinit var mRecyclerview: RecyclerView
    private lateinit var fab: FloatingActionButton
    private lateinit var myDB: DataBaseHelper
    private var mList: MutableList<ToDoModel> = ArrayList()
    private lateinit var adapter: ToDoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mRecyclerview = findViewById(R.id.recyclerview)
        fab = findViewById(R.id.fab)
        myDB = DataBaseHelper(this)
        mList = ArrayList()
        adapter = ToDoAdapter(myDB, this)

        mRecyclerview.setHasFixedSize(true)
        mRecyclerview.layoutManager = LinearLayoutManager(this)
        mRecyclerview.adapter = adapter

        mList = myDB.getAllTasks()
        mList.reverse()
        adapter.setTasks(mList)

        fab.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                AddNewTask.newInstance().show(supportFragmentManager, AddNewTask.TAG)
            }
        })

        val itemTouchHelper = ItemTouchHelper(RecyclerViewTouchHelper(adapter))
        itemTouchHelper.attachToRecyclerView(mRecyclerview)
    }

    override fun onDialogClose(dialogInterface: DialogInterface?) {
        mList = myDB.getAllTasks()
        mList.reverse()
        adapter.setTasks(mList)
        adapter.notifyDataSetChanged()
    }
}
