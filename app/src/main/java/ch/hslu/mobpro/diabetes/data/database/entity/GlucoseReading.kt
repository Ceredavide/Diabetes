package ch.hslu.mobpro.diabetes.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class GlucoseReading(

    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    @ColumnInfo(name = "user_index") val userIndex: Int,
    @ColumnInfo(name = "glucose_level") val glucoseLevel: Float,
    @ColumnInfo(name = "timestamp") val time: Date
) {
    constructor(userIndex: Int, glucoseLevel: Float, time: Date) : this(0, userIndex, glucoseLevel, time)
}
