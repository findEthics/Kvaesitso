package de.mm20.launcher2.data.plugins

import de.mm20.launcher2.plugin.Plugin
import de.mm20.launcher2.plugin.PluginRepository
import de.mm20.launcher2.plugin.PluginType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

internal class PluginRepositoryImpl : PluginRepository {

    private val scope = CoroutineScope(Job() + Dispatchers.IO)
    private val plugins = MutableStateFlow<Map<String, Plugin>>(emptyMap())

    override fun findMany(
        type: PluginType?,
        enabled: Boolean?,
        packageName: String?
    ): Flow<List<Plugin>> {
        return plugins.map {
            it.values.filter { plugin ->
                (type == null || plugin.type == type) &&
                    (enabled == null || plugin.enabled == enabled) &&
                    (packageName == null || plugin.packageName == packageName)
            }
        }
    }

    override fun get(authority: String): Flow<Plugin?> {
        return plugins.map { it[authority] }
    }

    override fun insertMany(plugins: List<Plugin>): Job {
        return scope.launch {
            this@PluginRepositoryImpl.plugins.value = plugins.associateBy { it.authority }
        }
    }

    override fun insert(plugin: Plugin): Job {
        return scope.launch {
            plugins.value += plugin.authority to plugin
        }
    }

    override fun update(plugin: Plugin): Job {
        return scope.launch {
            plugins.value += plugin.authority to plugin
        }
    }

    override fun updateMany(plugins: List<Plugin>): Job {
        return scope.launch {
            this@PluginRepositoryImpl.plugins.value += plugins.associateBy { it.authority }
        }
    }

    override fun deleteMany(): Job {
        return scope.launch {
            plugins.value = emptyMap()
        }
    }
}
