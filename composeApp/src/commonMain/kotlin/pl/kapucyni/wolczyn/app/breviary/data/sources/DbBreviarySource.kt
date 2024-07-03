package pl.kapucyni.wolczyn.app.breviary.data.sources

import pl.kapucyni.wolczyn.app.breviary.data.database.BreviaryDao
import pl.kapucyni.wolczyn.app.breviary.data.database.BreviaryEntity
import pl.kapucyni.wolczyn.app.breviary.domain.model.BreviaryType

class DbBreviarySource(private val breviaryDao: BreviaryDao) {
    suspend fun upsertBreviary(entity: BreviaryEntity): Long =
        breviaryDao.insertBreviary(entity)

    suspend fun loadBreviary(date: Long, type: BreviaryType): String? =
        breviaryDao.loadBreviary(date)?.let { entity ->
            when (type) {
                BreviaryType.INVITATORY -> entity.invitatory
                BreviaryType.OFFICE_OF_READINGS -> entity.officeOfReadings
                BreviaryType.LAUDS -> entity.lauds
                BreviaryType.MIDMORNING_PRAYER -> entity.prayer1
                BreviaryType.MIDDAY_PRAYER -> entity.prayer2
                BreviaryType.MIDAFTERNOON_PRAYER -> entity.prayer3
                BreviaryType.VESPERS -> entity.vespers
                BreviaryType.COMPLINE -> entity.compline
            }
        }

    suspend fun checkIfExists(date: Long): Boolean = breviaryDao.checkIfExists(date)

    suspend fun clearBreviary(date: Long) = breviaryDao.clearBreviary(date)
}