package com.webgurus.attendanceportal.listeners

interface CheckBoxChangedSelectedListeners {
    fun onChecked(position: Int,id:Int,tittle:String)
    fun onUnChecked(position: Int,id: Int,tittle: String)
}