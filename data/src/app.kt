import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import androidx.room.OnConflictStrategy
import androidx.room.Insert
import androidx.room.Delete

data class AppInfo(
    val packageName: String,
    val label: String,
    val iconPath: String,
    val launchCount: Int
)

@Entity(tableName = "applications")
data class AppEntity(
    @PrimaryKey val packageName: String,
    val originalLabel: String,
    val customLabel: String? = null,
    val originalIconPath: String,
    val customIconPath: String? = null,
    val launchCount: Int = 0,
    val isHidden: Boolean = false,
    val lastUpdateTime: Long = 0L
) {
    val displayLabel: String get() = customLabel ?: originalLabel
    val displayIconPath: String get() = customIconPath ?: originalIconPath
}

@Dao
interface AppDao {
    @Query("SELECT * FROM applications WHERE isHidden = 0 ORDER BY COALESCE(customLabel, originalLabel) ASC")
    fun getAllVisibleApps(): Flow<List<AppEntity>>

    @Query("SELECT * FROM applications WHERE isHidden = 0 AND launchCount > 0 ORDER BY launchCount DESC LIMIT :limit")
    fun getFrequentApps(limit: Int): Flow<List<AppEntity>>

    @Query("SELECT * FROM applications")
    suspend fun getAllAppsSnapshot(): List<AppEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateApps(apps: List<AppEntity>)

    @Delete
    suspend fun deleteApps(apps: List<AppEntity>)

    @Query("UPDATE applications SET launchCount = launchCount + 1 WHERE packageName = :packageName")
    suspend fun incrementLaunchCount(packageName: String)
}