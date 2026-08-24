package de.mm20.launcher2.preferences.migrations

import androidx.datastore.core.DataMigration
import de.mm20.launcher2.preferences.GestureAction
import de.mm20.launcher2.preferences.LauncherSettingsData

class Migration7 : DataMigration<LauncherSettingsData> {
    override suspend fun cleanUp() = Unit

    override suspend fun shouldMigrate(currentData: LauncherSettingsData) = currentData.schemaVersion < 7

    override suspend fun migrate(currentData: LauncherSettingsData) = currentData.normalized()
}

internal fun LauncherSettingsData.normalized(): LauncherSettingsData {
    fun GestureAction.withoutFeed() = if (this is GestureAction.Feed) GestureAction.NoAction else this

    return copy(
        schemaVersion = 7,
        fileSearchProviders = fileSearchProviders.intersect(setOf("local")),
        contactSearchProviders = contactSearchProviders.intersect(setOf("local")),
        calendarSearchProviders = calendarSearchProviders.intersect(setOf("local")),
        locationSearchProviders = locationSearchProviders.intersect(setOf("openstreetmaps")),
        weatherProvider = weatherProvider.takeIf { it in setOf("metno", "owm", "dwd", "breezy") } ?: "metno",
        weatherProviderSettings = weatherProviderSettings.filterKeys { it in setOf("metno", "owm", "dwd", "breezy") },
        badgesCloudFiles = false,
        badgesPlugins = false,
        clockWidgetMusicPart = false,
        clockWidgetSmartspacer = false,
        gesturesSwipeDown = gesturesSwipeDown.withoutFeed(),
        gesturesSwipeLeft = gesturesSwipeLeft.withoutFeed(),
        gesturesSwipeRight = gesturesSwipeRight.withoutFeed(),
        gesturesSwipeUp = gesturesSwipeUp.withoutFeed(),
        gesturesDoubleTap = gesturesDoubleTap.withoutFeed(),
        gesturesLongPress = gesturesLongPress.withoutFeed(),
        gesturesHomeButton = gesturesHomeButton.withoutFeed(),
        feedProviderPackage = null,
    )
}
