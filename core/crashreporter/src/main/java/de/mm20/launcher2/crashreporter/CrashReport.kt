package de.mm20.launcher2.crashreporter

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class CrashReport(
    val type: CrashReportType,
    val stacktrace: String?,
    val filePath: String
) {
    companion object {
        suspend fun fromFile(file: File): CrashReport {
            val content = withContext(Dispatchers.IO) {
                file.inputStream().bufferedReader().use {
                    it.readText()
                }
            }
            return CrashReport(
                type = if (file.name.endsWith("_crash.txt")) CrashReportType.Crash else CrashReportType.Exception,
                stacktrace = content,
                filePath = file.absolutePath
            )
        }
    }
}

enum class CrashReportType {
    Exception,
    Crash
}
