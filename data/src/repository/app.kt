package data.repository

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

import AppDao
import AppInfo
import AppEntity
import data.utils.IconCacheManager

class AppRepository(
    private val context: Context,
    private val appDao: AppDao,
    private val iconCacheManager: IconCacheManager
) {
    val allApps: Flow<List<AppInfo>> =
        appDao.getAllVisibleApps().map { list -> list.map { it.toModel() } }

    val frequentApps: Flow<List<AppInfo>> =
        // TODO : change this from 4 to a setting variable
        appDao.getFrequentApps(4).map { list -> list.map { it.toModel() } }

    suspend fun syncAppsToDatabase() = withContext(Dispatchers.IO) {
        val pm = context.packageManager
        val dbApps = appDao.getAllAppsSnapshot().associateBy { it.packageName }

        val intent = Intent(Intent.ACTION_MAIN, null).apply { addCategory(Intent.CATEGORY_LAUNCHER) }
        val resolveInfos = pm.queryIntentActivities(intent, 0)

        val installedPackages = mutableSetOf<String>()
        val appsToUpsert = mutableListOf<AppEntity>()

        for (resolveInfo in resolveInfos) {
            val packageName = resolveInfo.activityInfo.packageName
            installedPackages.add(packageName)

            val packageInfo = try {
                pm.getPackageInfo(packageName, 0)
            } catch (e: PackageManager.NameNotFoundException) {
                continue
            }

            val systemUpdateTime = packageInfo.lastUpdateTime
            val dbApp = dbApps[packageName]

            if (dbApp != null && dbApp.lastUpdateTime == systemUpdateTime) continue

            val label = resolveInfo.loadLabel(pm).toString()
            val iconDrawable = resolveInfo.loadIcon(pm)
            val iconPath = iconCacheManager.saveIconAndGetPath(packageName, iconDrawable)

            appsToUpsert.add(
                AppEntity(
                    packageName = packageName,
                    originalLabel = label,
                    originalIconPath = iconPath,
                    lastUpdateTime = systemUpdateTime,
                    customLabel = dbApp?.customLabel,
                    customIconPath = dbApp?.customIconPath,
                    launchCount = dbApp?.launchCount ?: 0,
                    isHidden = dbApp?.isHidden ?: false
                )
            )
        }

        val uninstalledApps = dbApps.values.filter { it.packageName !in installedPackages }
        uninstalledApps.forEach { iconCacheManager.deleteIcon(it.packageName) }

        if (appsToUpsert.isNotEmpty()) appDao.insertOrUpdateApps(appsToUpsert)
        if (uninstalledApps.isNotEmpty()) appDao.deleteApps(uninstalledApps)
    }

    suspend fun recordLaunch(packageName: String) = withContext(Dispatchers.IO) {
        appDao.incrementLaunchCount(packageName)
    }

    private fun AppEntity.toModel() = AppInfo(
        packageName = packageName,
        label = displayLabel,
        iconPath = displayIconPath,
        launchCount = launchCount
    )
}