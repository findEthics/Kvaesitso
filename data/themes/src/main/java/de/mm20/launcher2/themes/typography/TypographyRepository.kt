package de.mm20.launcher2.themes.typography

import de.mm20.launcher2.database.AppDatabase
import de.mm20.launcher2.themes.DefaultThemeId
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.UUID

class TypographyRepository(
    private val database: AppDatabase,
) {
    private val scope = CoroutineScope(Dispatchers.IO + Job())

    fun getAll(): Flow<List<Typography>> {
        return database.themeDao().getAllTypographies().map {
            getBuiltIn() + it.map { Typography(it) }
        }
    }

    fun get(id: UUID): Flow<Typography?> {
        if (id == DefaultThemeId) return flowOf(default)
        return database.themeDao().getTypography(id).map { it?.let { Typography(it) } }
    }

    fun create(typography: Typography) {
        scope.launch {
            database.themeDao().insertTypography(typography.toEntity())
        }
    }

    fun update(typography: Typography) {
        scope.launch {
            database.themeDao().updateTypography(typography.toEntity())
        }
    }


    fun delete(typography: Typography) {
        scope.launch {
            database.themeDao().deleteTypography(typography.id)
        }
    }

    fun getOrDefault(id: UUID?): Flow<Typography> {
        if (id == null) return flowOf(default)
        return get(id).map { it ?: default }
    }

    private fun getBuiltIn(): List<Typography> {
        return listOf(
            default,
        )
    }

    private val default: Typography
        get() = Typography(
            id = DefaultThemeId,
            builtIn = true,
            name = "Google Sans (Rounded)",
            fonts = mapOf(
                "brand" to FontFamily.LauncherDefault(mapOf("ROND" to 100f)),
                "plain" to FontFamily.LauncherDefault(mapOf("ROND" to 100f)),
            ),
            styles = DefaultTextStyles,
            emphasizedStyles = DefaultEmphasizedTextStyles,
        )

}
