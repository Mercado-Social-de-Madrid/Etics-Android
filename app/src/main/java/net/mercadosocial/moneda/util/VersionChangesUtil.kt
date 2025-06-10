package net.mercadosocial.moneda.util

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import net.mercadosocial.moneda.BuildConfig
import net.mercadosocial.moneda.R
import androidx.core.content.edit

class VersionChangesUtil(val context: Context) {

    val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    companion object {
        private const val LAST_VERSION_CHANGES_SEEN = "last_version_changes_seen"
    }

    class VersionChanges(
        val versionCode: Int,
        val versionName: String,
        val changesStringId: Int,
    )

    private val versionChanges = listOf(
        VersionChanges(468, "4.6.8", R.string.changes_4_6_8),
    )

    fun mustShowLastVersionChanges() =
        BuildConfig.VERSION_CODE > prefs.getInt(LAST_VERSION_CHANGES_SEEN, 0)
                && versionChanges.any { it.versionCode == BuildConfig.VERSION_CODE }

    fun getCurrentVersionChanges(): String {
        val currentVersionChanges =
            versionChanges.first { it.versionCode == BuildConfig.VERSION_CODE }
        val versionNameTitle =
            context.getString(R.string.version_x, currentVersionChanges.versionName)
        val versionChanges = context.getString(currentVersionChanges.changesStringId)
        return "$versionNameTitle\n\n$versionChanges"
    }

    fun getAllVersionChanges(): String {
        var fullText = ""
        versionChanges.forEach {
            val versionNameTitle = context.getString(R.string.version_x, it.versionName)
            val versionChanges = context.getString(it.changesStringId)
            fullText += "$versionNameTitle\n\n$versionChanges\n\n\n"
        }
        return fullText
    }

    fun setCurrentVersionChangesSeen() = prefs.edit {
        putInt(LAST_VERSION_CHANGES_SEEN, BuildConfig.VERSION_CODE)
    }

}