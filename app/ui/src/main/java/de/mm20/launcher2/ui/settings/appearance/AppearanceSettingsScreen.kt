package de.mm20.launcher2.ui.settings.appearance

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavKey
import de.mm20.launcher2.ktx.isAtLeastApiLevel
import de.mm20.launcher2.preferences.ColorScheme
import de.mm20.launcher2.ui.R
import de.mm20.launcher2.ui.component.preferences.ListPreference
import de.mm20.launcher2.ui.component.preferences.Preference
import de.mm20.launcher2.ui.component.preferences.PreferenceCategory
import de.mm20.launcher2.ui.component.preferences.PreferenceScreen
import de.mm20.launcher2.ui.locals.LocalBackStack
import de.mm20.launcher2.ui.settings.colorscheme.ColorSchemesSettingsRoute
import de.mm20.launcher2.ui.settings.shapes.ShapeSchemesSettingsRoute
import de.mm20.launcher2.ui.settings.transparencies.TransparencySchemesSettingsRoute
import kotlinx.serialization.Serializable

@Serializable
data object AppearanceSettingsRoute: NavKey

@Composable
fun AppearanceSettingsScreen() {
    val viewModel: AppearanceSettingsScreenVM = viewModel()
    val backStack = LocalBackStack.current
    val colorThemeName by viewModel.colorThemeName.collectAsStateWithLifecycle(null)
    val shapeThemeName by viewModel.shapeThemeName.collectAsStateWithLifecycle(null)
    val transparencyThemeName by viewModel.transparencyThemeName.collectAsStateWithLifecycle(null)
    val compatModeColors by viewModel.compatModeColors.collectAsState()

    PreferenceScreen(title = stringResource(id = R.string.preference_screen_appearance)) {
        item {
            PreferenceCategory {
                val theme by viewModel.colorScheme.collectAsState()
                ListPreference(
                    title = stringResource(id = R.string.preference_theme),
                    items = listOf(
                        stringResource(id = R.string.preference_theme_system) to ColorScheme.System,
                        stringResource(id = R.string.preference_theme_light) to ColorScheme.Light,
                        stringResource(id = R.string.preference_theme_dark) to ColorScheme.Dark,
                    ),
                    value = theme,
                    onValueChanged = { newValue ->
                        if (newValue == null) return@ListPreference
                        viewModel.setColorScheme(newValue)
                    }
                )
            }
        }
        item {
            PreferenceCategory {
                Preference(
                    title = stringResource(id = R.string.preference_screen_colors),
                    summary = colorThemeName,
                    onClick = {
                        backStack.add(ColorSchemesSettingsRoute)
                    },
                    icon = R.drawable.palette_24px,
                )
                Preference(
                    title = stringResource(id = R.string.preference_screen_shapes),
                    summary = shapeThemeName,
                    onClick = {
                        backStack.add(ShapeSchemesSettingsRoute)
                    },
                    icon = R.drawable.crop_square_24px,
                )
                Preference(
                    title = stringResource(id = R.string.preference_screen_transparencies),
                    summary = transparencyThemeName,
                    onClick = {
                        backStack.add(TransparencySchemesSettingsRoute)
                    },
                    icon = R.drawable.opacity_24px,
                )
            }
        }

        if (isAtLeastApiLevel(31)) {
            item {
                PreferenceCategory(stringResource(R.string.preference_category_advanced)) {
                    ListPreference(
                        title = stringResource(R.string.preference_mdy_color_source),
                        items = listOf(
                            stringResource(R.string.preference_mdy_color_source_system) to false,
                            stringResource(R.string.preference_mdy_color_source_wallpaper) to true,
                        ),
                        value = compatModeColors,
                        onValueChanged = {
                            viewModel.setCompatModeColors(it)
                        }
                    )
                }
            }
        }
    }
}
