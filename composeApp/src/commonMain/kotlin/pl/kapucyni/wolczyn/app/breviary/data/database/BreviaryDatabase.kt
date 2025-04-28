package pl.kapucyni.wolczyn.app.breviary.data.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [BreviaryEntity::class],
    version = 1,
    exportSchema = false,
)
@ConstructedBy(BreviaryDatabaseCtor::class)
abstract class BreviaryDatabase : RoomDatabase() {
    abstract fun breviaryDao(): BreviaryDao

    companion object {
        const val DATABASE_NAME = "breviary.db"
    }
}