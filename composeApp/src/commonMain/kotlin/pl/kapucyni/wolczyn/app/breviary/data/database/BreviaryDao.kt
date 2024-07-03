package pl.kapucyni.wolczyn.app.breviary.data.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface BreviaryDao {
    @Upsert
    suspend fun upsertBreviary(entity: BreviaryEntity): Long

    @Query("SELECT * FROM breviary WHERE date = :date")
    suspend fun loadBreviary(date: Long): BreviaryEntity?

    @Query("DELETE FROM breviary WHERE date <= :date")
    suspend fun clearBreviary(date: Long)
}