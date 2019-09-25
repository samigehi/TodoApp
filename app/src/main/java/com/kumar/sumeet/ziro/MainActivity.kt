package com.kumar.sumeet.ziro

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

import kotlinx.android.synthetic.main.activity_main.*
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.widget.EditText
import android.view.LayoutInflater
import android.view.MenuItem
import android.widget.Toast
import com.kumar.sumeet.ziro.adapter.TodoRecyclerAdapter
import com.kumar.sumeet.ziro.database.DbHelper
import com.kumar.sumeet.ziro.model.Task


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getList()
        fab.setOnClickListener { view ->
            createTodo()
        }
    }

    fun toast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }

    var adapter: TodoRecyclerAdapter? = null
    var dbHelper: DbHelper? = null
    val taskList = ArrayList<Task>()

    fun getList() {
        dbHelper = DbHelper(this@MainActivity)
        taskList.addAll(dbHelper?.list!!)
        recycler.setLayoutManager(LinearLayoutManager(this));
        adapter = TodoRecyclerAdapter(this@MainActivity, taskList)

        recycler.setAdapter(adapter)
        registerForContextMenu(recycler);
        updateHint()
    }

    fun createTodo() {
        val builder = AlertDialog.Builder(this@MainActivity)
        builder.setTitle("Title").setCancelable(false)
        val viewInflated = LayoutInflater.from(this@MainActivity).inflate(R.layout.edittext_dialog, null)
        val input = viewInflated.findViewById(R.id.input_todo) as EditText
        builder.setView(viewInflated)

        builder.setPositiveButton("CREATE", { dialog, which ->
            val task = Task(0, input.text.toString())
            dbHelper?.addTodo(task)
            dialog.dismiss()
            refreshList()

        })
        builder.setNegativeButton(android.R.string.cancel, { dialog, which -> dialog.cancel() })

        builder.show()
    }

    fun updateTodo(task: Task) {
        val builder = AlertDialog.Builder(this@MainActivity)
        builder.setTitle("Title").setCancelable(false)
        val viewInflated = LayoutInflater.from(this@MainActivity).inflate(R.layout.edittext_dialog, null)
        val input = viewInflated.findViewById(R.id.input_todo) as EditText
        builder.setView(viewInflated)

        builder.setPositiveButton("UPDATE", { dialog, which ->
            task.task = input.text.toString()
            dbHelper?.updateTodo(task)
            dialog.dismiss()
            refreshList()

        })
        builder.setNegativeButton(android.R.string.cancel, { dialog, which -> dialog.cancel() })

        builder.show()
    }

    fun deleteTodo(task: Task) {
        dbHelper?.deleteTask(task.id)
        refreshList()
    }

    fun completeTodo(task: Task) {
        dbHelper?.completeTodo(task)
        refreshList()
    }

    fun refreshList() {
        taskList.clear()
        taskList.addAll(dbHelper?.list!!)
        adapter?.notifyDataSetChanged()
        updateHint()
    }

    private fun updateHint() {
        if (taskList.isEmpty())
            tv_hint.setText("No Items... please create one")
        else
            tv_hint.setText("Click on item for more options")
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        var position = -1
        try {
            position = adapter?.holderId!!
        } catch (e: Exception) {
            return super.onContextItemSelected(item)
        }
        if (position != -1 && position < taskList.size) {
            when (item.itemId) {
                TodoRecyclerAdapter.UPDATE -> {
                    updateTodo(taskList.get(position))
                }
                TodoRecyclerAdapter.DELETE -> {
                    deleteTodo(taskList.get(position))
                }
                TodoRecyclerAdapter.COMPLETE -> {
                    completeTodo(taskList.get(position))
                    //toast("id " + position + " - 13")
                }
            }

        }
        return super.onContextItemSelected(item)
    }
}
