package com.ronik.todoapplication

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

// Table model for todoapp

/*
    CREATE DATABASE 'todoapp';

    CREATE TABLE 'Tasks'(
        'id' INTEGER PRIMARY KEY,
        'task_title' VARCHAR(63) NOT NULL,
        'task_text' VARCHAR(127) NOT NULL,
        'timestamp_x' DATETIME NOT NULL DEFAULT (datetime(CURRENT_TIMESTAMP, 'localtime'))
    )
*/

class Database(val context: Context, val factory: SQLiteDatabase.CursorFactory?):
    SQLiteOpenHelper(context, "todoapp", factory, 1) {

    override fun onCreate(db: SQLiteDatabase?) {
        val query = "CREATE TABLE 'tasks'('id' INTEGER PRIMARY KEY,'task_title' VARCHAR(63) NOT NULL,'task_text' VARCHAR(127) NOT NULL, 'timestamp_x' DATETIME NOT NULL DEFAULT (datetime(CURRENT_TIMESTAMP, 'localtime')))"
        db!!.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS 'tasks'")
        onCreate(db)
    }

    // Custom functions
    fun addTask(task: Task) {
        // Adding a task to the list
        val values = ContentValues()
        values.put("task_title", task.taskTitle)
        values.put("task_text", task.taskText)

        val db = this.writableDatabase
        db.insert("tasks", null, values)
        db.close()
    }

    fun deleteTask(task: Task) {
        // Removing a task from the list
        val db = this.writableDatabase
        db.delete("tasks", "task_title=? and task_text=?", arrayOf(
            task.taskTitle, task.taskText
        ))
        db.close()
    }

    @SuppressLint("Recycle", "Range")
    fun getTasks(): ArrayList<String> {
        // Getting all tasks from the database (for a list -> see ListView)
        val db = this.readableDatabase
        val query = "SELECT task_title, task_text FROM 'tasks'"
        val tasksList = ArrayList<String>()
        val cursor: Cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            do {
                val title = cursor.getString(cursor.getColumnIndex("task_title"))
                val text = cursor.getString(cursor.getColumnIndex("task_text"))
                tasksList.add("$title\n\t$text")
            } while (cursor.moveToNext())
        }
        return tasksList
    }
}