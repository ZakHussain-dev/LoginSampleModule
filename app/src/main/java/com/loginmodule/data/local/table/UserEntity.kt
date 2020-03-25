package com.loginmodule.data.local.table

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Table of saving location into the table pinned_location
 */
@Entity(tableName = "tb_user", primaryKeys = ["user_id", "email"])
@Parcelize
data class UserEntity(

    @Expose
    @SerializedName("user_id")
    @ColumnInfo(name = "user_id")
    var userId: String,

    @Expose
    @SerializedName("email")
    @ColumnInfo(name = "email")
    var email: String,

    @Expose
    @SerializedName("password")
    @ColumnInfo(name = "password")
    var password: String,
    @Expose
    @SerializedName("avatar_url")
    @ColumnInfo(name = "avatar_url")
    var avatarUrl: String
) : Parcelable