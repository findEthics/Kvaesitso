package de.mm20.launcher2.database.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

class Migration_33_34 : Migration(33, 34) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("DROP TABLE IF EXISTS `Plugins`")
        database.execSQL("DELETE FROM `Widget` WHERE `type` IN ('weather', 'music')")
        database.execSQL("DELETE FROM `Searchable` WHERE `type` IN ('weather', 'music', 'nextcloud', 'owncloud', 'tasks.org', 'wikipedia') OR `type` LIKE 'plugin.%'")
    }
}
