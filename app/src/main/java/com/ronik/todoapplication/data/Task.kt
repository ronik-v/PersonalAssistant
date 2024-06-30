package com.ronik.todoapplication.data

// Task class model
class Task(val taskTitle: String, val taskText: String) {
    init {
        require((taskTitle.length <= 63) and (taskText.length <= 127)) { "Size argument error" }
    }
}