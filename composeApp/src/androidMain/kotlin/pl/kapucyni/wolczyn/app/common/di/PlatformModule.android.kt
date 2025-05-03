package pl.kapucyni.wolczyn.app.common.di

import androidx.room.Room
import androidx.room.RoomDatabase
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module
import pl.kapucyni.wolczyn.app.breviary.data.database.BreviaryDatabase
import pl.kapucyni.wolczyn.app.common.presentation.AndroidLifecycleManager
import pl.kapucyni.wolczyn.app.common.presentation.LifecycleManager
import pl.kapucyni.wolczyn.app.common.utils.dataStore

actual val platformModule: Module = module {
    single { dataStore(get()) }

    single<RoomDatabase.Builder<BreviaryDatabase>> {
        val context = androidContext().applicationContext
        val dbFile = context.getDatabasePath(BreviaryDatabase.DATABASE_NAME)
        Room.databaseBuilder<BreviaryDatabase>(
            context = context,
            name = dbFile.absolutePath,
        )
    }

    factory<LifecycleManager> { AndroidLifecycleManager(androidApplication()) }
}