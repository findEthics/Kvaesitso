package de.mm20.launcher2.settings

import android.content.Context
import androidx.datastore.core.DataMigration
import androidx.datastore.core.Serializer
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.dataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

abstract class BaseSettings<T>(
    internal val context: Context,
    private val fileName: String,
    private val serializer: Serializer<T>,
    migrations: List<DataMigration<T>>,
    corruptionHandler: ReplaceFileCorruptionHandler<T>? = null,
) {

    protected val scope = CoroutineScope(Job() + Dispatchers.Default)

    protected val Context.dataStore by dataStore(
        fileName = fileName,
        serializer = serializer,
        produceMigrations = {
            migrations
        },
        corruptionHandler = corruptionHandler
    )

    protected fun updateData(block: suspend (T) -> T) {
        scope.launch {
            context.dataStore.updateData(block)
        }
    }

}
