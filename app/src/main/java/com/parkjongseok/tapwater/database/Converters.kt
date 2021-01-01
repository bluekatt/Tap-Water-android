package com.parkjongseok.tapwater.database

import androidx.room.TypeConverter
import com.google.gson.Gson

class Converters {
    @TypeConverter
    fun listToJson(value: List<DrinkLogItem>?) = Gson().toJson(value)

    @TypeConverter
    fun jsonToList(value: String?): List<DrinkLogItem> {
        var strValue = value
        if(value=="NULL"){
            strValue = null
        }
        return Gson().fromJson(strValue?:"[]", Array<DrinkLogItem>::class.java).toList()
    }

}