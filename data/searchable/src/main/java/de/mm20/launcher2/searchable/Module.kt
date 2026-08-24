package de.mm20.launcher2.searchable

import org.koin.dsl.module

val searchableModule = module {
    factory <SavableSearchableRepository> { SavableSearchableRepositoryImpl(get(), get()) }
}
