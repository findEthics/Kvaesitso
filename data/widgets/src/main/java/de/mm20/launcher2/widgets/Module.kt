package de.mm20.launcher2.widgets

import org.koin.dsl.module

val widgetsModule = module {
    factory<WidgetRepository> { WidgetRepositoryImpl(get()) }
}
