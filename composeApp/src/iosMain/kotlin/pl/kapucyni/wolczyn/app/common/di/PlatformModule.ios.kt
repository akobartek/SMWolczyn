package pl.kapucyni.wolczyn.app.common.di

import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.cinterop.ExperimentalForeignApi
import org.koin.core.module.Module
import org.koin.dsl.module
import pl.kapucyni.wolczyn.app.breviary.data.database.BreviaryDatabase
import pl.kapucyni.wolczyn.app.common.presentation.AppleLifecycleManager
import pl.kapucyni.wolczyn.app.common.presentation.LifecycleManager
import pl.kapucyni.wolczyn.app.common.utils.dataStore
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSHomeDirectory
import platform.Foundation.NSUserDomainMask

@OptIn(ExperimentalForeignApi::class)
actual val platformModule: Module = module {
    single { dataStore() }

    single<RoomDatabase.Builder<BreviaryDatabase>> {
        val documentsDirectory = NSFileManager.defaultManager.URLForDirectory(
            directory = NSDocumentDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = true,
            error = null,
        )?.path ?: NSHomeDirectory()
        val dbFilePath = "$documentsDirectory/${BreviaryDatabase.DATABASE_NAME}"

        Room.databaseBuilder<BreviaryDatabase>(name = dbFilePath)
    }

    factory<LifecycleManager> { AppleLifecycleManager() }
}