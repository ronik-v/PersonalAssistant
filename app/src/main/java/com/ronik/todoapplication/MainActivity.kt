package com.ronik.todoapplication

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat


class MainActivity : AppCompatActivity() {
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    // Switch button to toggle application color scheme state
    private lateinit var switchTheme: Switch
    private lateinit var title: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // /* Application components (UI)
        switchTheme = findViewById(R.id.switchTheme)
        title = findViewById(R.id.title)

        val listView = findViewById<ListView>(R.id.listView)
        val taskTitle: EditText = findViewById(R.id.taskTitle)
        val taskText: EditText = findViewById(R.id.taskText)
        val button: Button = findViewById(R.id.button)

        // */ Application components (UI)

        val db = Database(this, null)
        val todoTasks: ArrayList<String> = db.getTasks()
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, todoTasks)
        listView.adapter = adapter

        switchTheme.isChecked = getSavedThemeState()
        switchTheme.setOnCheckedChangeListener {_, isChecked ->
            if (isChecked) { setAppNightTheme() } else { setAppLightTheme() }

            saveThemeState(isChecked)

            if (switchTheme.isChecked) { setAppNightTheme() } else { setAppLightTheme() }
        }

        listView.setOnItemClickListener { adapterView, view, i, l ->
            val text = listView.getItemAtPosition(i).toString()
            val userTaskTitle = text.substringBefore("\n\t")
            val userTaskText = text.substringAfter("\n\t")
            val task = Task(userTaskTitle, userTaskText)
            val db = Database(this, null)

            db.deleteTask(task)
            // Updating the task list to reflect dynamically on the view
            updateTodoList(todoTasks, db, adapter)
            Toast.makeText(this, "Задача $userTaskText удаленна", Toast.LENGTH_LONG).show()
        }

        button.setOnClickListener {
            val userTaskTitle = taskTitle.text.toString().trim()
            val userTaskText = taskText.text.toString().trim()
            val task = Task(userTaskTitle, userTaskText)
            val db = Database(this, null)

            db.addTask(task)
            // Clearing the add task form
            taskTitle.text.clear()
            taskText.text.clear()
            // Updating the task list to reflect dynamically on the view
            updateTodoList(todoTasks, db, adapter)
            Toast.makeText(this, "Задача $userTaskTitle добавленна", Toast.LENGTH_LONG).show()
        }
    }

    private fun updateTodoList(todoTasks: ArrayList<String>, db: Database, adapter: ArrayAdapter<String>) {
        // Update todoTasks on adapter
        todoTasks.clear()
        todoTasks.addAll(db.getTasks())
        adapter.notifyDataSetChanged()
    }

    private fun setAppLightTheme() {
        // Enable a light theme for an app
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

    private fun setAppNightTheme() {
        // Enable a dark theme for an app
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    }

    private fun getSavedThemeState(): Boolean {
        val sharedPreferences: SharedPreferences = getSharedPreferences("ThemePrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean("isNightTheme", false)
    }

    @SuppressLint("CommitPrefEdits")
    private fun saveThemeState(isNightTheme: Boolean) {
        // Saving the current state of the application theme
        val sharedPreferences: SharedPreferences = getSharedPreferences("ThemePrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("isNightTheme", isNightTheme)
        editor.apply()
    }

    override fun onResume() {
        super.onResume()
        val textColor = if(switchTheme.isChecked) R.color.white else R.color.black
        title.setTextColor(ContextCompat.getColor(this,textColor))
    }
}