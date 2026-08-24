package de.mm20.launcher2.crashreporter

import android.content.Context
import android.util.Log
import com.balsikandar.crashreporter.CrashReporter
import com.balsikandar.crashreporter.utils.AppUtils
import com.balsikandar.crashreporter.utils.CrashUtil
import kotlinx.coroutines.CancellationException
import java.io.File

object CrashReporter {
    fun logException(e: Exception) {
        if (e !is CancellationException) {
            CrashReporter.logException(e)
        }
        Log.e("MM20", Log.getStackTraceString(e))
    }

    suspend fun getCrashReport(filePath: String): CrashReport {
        return CrashReport.fromFile(File(filePath))
    }

    fun getDeviceInformation(context: Context): String {
        return AppUtils.getDeviceDetails(context)
    }
}
