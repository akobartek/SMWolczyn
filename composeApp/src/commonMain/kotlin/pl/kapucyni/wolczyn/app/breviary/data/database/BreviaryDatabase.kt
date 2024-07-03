package pl.kapucyni.wolczyn.app.breviary.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [BreviaryEntity::class],
    version = 1,
)
abstract class BreviaryDatabase : RoomDatabase(), DB {
    abstract fun breviaryDao(): BreviaryDao

    override fun clearAllTables() {
        super.clearAllTables()
    }

    companion object {
        const val DATABASE_NAME = "breviary.db"
    }
}

interface DB {
    fun clearAllTables() {}
}