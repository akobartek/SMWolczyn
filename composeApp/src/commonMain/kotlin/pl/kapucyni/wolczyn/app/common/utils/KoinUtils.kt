package pl.kapucyni.wolczyn.app.common.utils

import org.koin.core.context.startKoin
import org.koin.core.module.Module
import pl.kapucyni.wolczyn.app.admin.di.adminModule
import pl.kapucyni.wolczyn.app.archive.di.archiveModule
import pl.kapucyni.wolczyn.app.breviary.di.breviaryModule
import pl.kapucyni.wolczyn.app.common.di.platformModule
import pl.kapucyni.wolczyn.app.core.di.coreModule
import pl.kapucyni.wolczyn.app.kitchen.di.kitchenModule
import pl.kapucyni.wolczyn.app.schedule.di.scheduleModule
import pl.kapucyni.wolczyn.app.shop.di.shopModule
import pl.kapucyni.wolczyn.app.songbook.di.songBookModule
import pl.kapucyni.wolczyn.app.weather.di.weatherModule

fun getBaseModules() = listOf(
    platformModule,
    coreModule,
    scheduleModule,
    songBookModule,
    kitchenModule,
    shopModule,
    weatherModule,
    breviaryModule,
    archiveModule,
    adminModule,
)

fun initKoinAndroid(androidModule: Module) {
    startKoin {
        modules(androidModule + getBaseModules())
    }
}

fun initKoin() {
    startKoin {
        modules(getBaseModules())
    }
}