package pl.kapucyni.wolczyn.app.breviary.data.database

import androidx.room.RoomDatabaseConstructor

// The Room compiler generates the `actual` implementations.
@Suppress("NO_ACTUAL_FOR_EXPECT", "EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect object BreviaryDatabaseCtor : RoomDatabaseConstructor<BreviaryDatabase> {
    override fun initialize(): BreviaryDatabase
}