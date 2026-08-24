package de.mm20.launcher2.preferences.migrations

import androidx.datastore.core.DataMigration
import de.mm20.launcher2.preferences.KeyboardFilterBarItem
import de.mm20.launcher2.preferences.LauncherSettingsData

class Migration8 : DataMigration<LauncherSettingsData> {
    override suspend fun cleanUp() = Unit

    override suspend fun shouldMigrate(currentData: LauncherSettingsData) = currentData.schemaVersion < 8

    override suspend fun migrate(currentData: LauncherSettingsData) = currentData.copy(
        schemaVersion = 8,
        searchFilterBarItems = currentData.searchFilterBarItems.filterNot {
            it == KeyboardFilterBarItem.LegacyRemoved
        },
    )
}
