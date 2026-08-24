package de.mm20.launcher2.ui.launcher.search.filters

import android.content.Context
import de.mm20.launcher2.preferences.KeyboardFilterBarItem
import de.mm20.launcher2.search.SearchFilters
import de.mm20.launcher2.ui.R

val KeyboardFilterBarItem.iconMedium
    get() = when (this) {
        KeyboardFilterBarItem.Apps -> R.drawable.apps_24px
        KeyboardFilterBarItem.Events -> R.drawable.today_24px
        KeyboardFilterBarItem.Contacts -> R.drawable.person_24px
        KeyboardFilterBarItem.Files -> R.drawable.description_24px
        KeyboardFilterBarItem.Tools -> R.drawable.handyman_24px
        KeyboardFilterBarItem.LegacyRemoved -> error("Legacy filter item")
        KeyboardFilterBarItem.Shortcuts -> R.drawable.mobile_arrow_up_right_24px
        KeyboardFilterBarItem.HiddenResults -> R.drawable.visibility_off_24px
        KeyboardFilterBarItem.OnlineResults -> R.drawable.language_24px
    }

val KeyboardFilterBarItem.iconSmall
    get() = when (this) {
        KeyboardFilterBarItem.Apps -> R.drawable.apps_20px
        KeyboardFilterBarItem.Events -> R.drawable.today_20px
        KeyboardFilterBarItem.Contacts -> R.drawable.person_20px
        KeyboardFilterBarItem.Files -> R.drawable.description_20px
        KeyboardFilterBarItem.Tools -> R.drawable.handyman_20px
        KeyboardFilterBarItem.LegacyRemoved -> error("Legacy filter item")
        KeyboardFilterBarItem.Shortcuts -> R.drawable.mobile_arrow_up_right_20px
        KeyboardFilterBarItem.HiddenResults -> R.drawable.visibility_off_20px
        KeyboardFilterBarItem.OnlineResults -> R.drawable.language_20px
    }

fun KeyboardFilterBarItem.getLabel(context: Context): String {
    return when (this) {
        KeyboardFilterBarItem.Apps -> context.getString(R.string.search_filter_apps)
        KeyboardFilterBarItem.Events -> context.getString(R.string.preference_search_calendar)
        KeyboardFilterBarItem.Contacts -> context.getString(R.string.preference_search_contacts)
        KeyboardFilterBarItem.Files -> context.getString(R.string.preference_search_files)
        KeyboardFilterBarItem.Tools -> context.getString(R.string.search_filter_tools)
        KeyboardFilterBarItem.LegacyRemoved -> error("Legacy filter item")
        KeyboardFilterBarItem.Shortcuts -> context.getString(R.string.preference_search_appshortcuts)
        KeyboardFilterBarItem.HiddenResults -> context.getString(R.string.preference_hidden_items)
        KeyboardFilterBarItem.OnlineResults -> context.getString(R.string.search_filter_online)
    }
}

val KeyboardFilterBarItem.isCategory
    get() = when (this) {
        KeyboardFilterBarItem.OnlineResults, KeyboardFilterBarItem.HiddenResults -> false
        else -> true
    }

fun SearchFilters.isSelected(item: KeyboardFilterBarItem): Boolean {
    if (item.isCategory && allCategoriesEnabled) return false
    return when (item) {
        KeyboardFilterBarItem.Apps -> apps
        KeyboardFilterBarItem.Events -> events
        KeyboardFilterBarItem.Contacts -> contacts
        KeyboardFilterBarItem.Files -> files
        KeyboardFilterBarItem.Tools -> tools
        KeyboardFilterBarItem.LegacyRemoved -> false
        KeyboardFilterBarItem.Shortcuts -> shortcuts
        KeyboardFilterBarItem.HiddenResults -> hiddenItems
        KeyboardFilterBarItem.OnlineResults -> allowNetwork
    }
}

fun SearchFilters.toggle(item: KeyboardFilterBarItem): SearchFilters {
    return when (item) {
        KeyboardFilterBarItem.Apps -> return toggleApps()
        KeyboardFilterBarItem.Events -> return toggleEvents()
        KeyboardFilterBarItem.Contacts -> return toggleContacts()
        KeyboardFilterBarItem.Files -> return toggleFiles()
        KeyboardFilterBarItem.Tools -> return toggleTools()
        KeyboardFilterBarItem.LegacyRemoved -> return this
        KeyboardFilterBarItem.Shortcuts -> return toggleShortcuts()
        KeyboardFilterBarItem.HiddenResults -> return copy(hiddenItems = !hiddenItems)
        KeyboardFilterBarItem.OnlineResults -> return copy(allowNetwork = !allowNetwork)
    }
}
