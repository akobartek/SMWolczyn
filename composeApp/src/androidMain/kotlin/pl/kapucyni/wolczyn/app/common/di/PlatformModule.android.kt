package pl.kapucyni.wolczyn.app.common.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.Module
import org.koin.dsl.module
import pl.kapucyni.wolczyn.app.breviary.data.database.BreviaryDatabase
import pl.kapucyni.wolczyn.app.common.utils.dataStore

actual val platformModule: Module = module {
    single { dataStore(get()) }

    single<BreviaryDatabase> {
        getDatabase(androidApplication(), BreviaryDatabase.DATABASE_NAME)
    }
}

inline fun <reified T : RoomDatabase> getDatabase(context: Context, dbName: String): T {
    val dbFile = context.getDatabasePath(dbName)
    return Room.databaseBuilder<T>(
        context = context,
        name = dbFile.absolutePath
    )
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
}