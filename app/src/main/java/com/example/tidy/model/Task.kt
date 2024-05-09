package com.example.tidy.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Entity(tableName = "tasks")
@Parcelize
data class Task (
    @PrimaryKey
    val id : Int,
    val noteTitle:String,
    val noteDesc : String

):Parcelable
