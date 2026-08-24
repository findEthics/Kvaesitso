package de.mm20.launcher2.preferences.migrations

import androidx.datastore.core.DataMigration
import de.mm20.launcher2.preferences.LauncherSettingsData

class Migration11 : DataMigration<LauncherSettingsData> {
    override suspend fun cleanUp() = Unit

    override suspend fun shouldMigrate(currentData: LauncherSettingsData) = currentData.schemaVersion < 11

    override suspend fun migrate(currentData: LauncherSettingsData) = currentData.copy(
        schemaVersion = 11,
        fileSearchProviders = currentData.fileSearchProviders - "nextcloud" - "owncloud",
    )
}
