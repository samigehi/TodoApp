package com.kumar.sumeet.ziro.model

import java.util.*

data class Task(val id : Int = 0, var task : String, val date : String = Date().toString(), var isComplete : Boolean = false) {
}