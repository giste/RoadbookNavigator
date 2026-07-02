package org.giste.roadbooknavigator.core.permission.data

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.giste.roadbooknavigator.core.permission.domain.AppPermission
import org.giste.roadbooknavigator.core.permission.domain.PermissionState
import org.giste.roadbooknavigator.core.permission.domain.PermissionRepository
import org.giste.roadbooknavigator.core.util.AppLogger
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AndroidPermissionRepository @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val logger: AppLogger
) : PermissionRepository {

    private val _states = MutableStateFlow(calculatePermissionStates())

    override fun observeAllPermissions(): Flow<Map<AppPermission, PermissionState>> = _states.asStateFlow()

    override fun refreshPermissionStates() {
        logger.d("Refreshing permission states in repository")
        _states.value = calculatePermissionStates()
    }

    private fun calculatePermissionStates(): Map<AppPermission, PermissionState> {
        logger.v("Calculating permission states")
        val states = AppPermission.entries.associateWith { permission ->
            val androidPermission = permission.toAndroidPermission ?: return@associateWith PermissionState.Granted

            val isGranted = ContextCompat.checkSelfPermission(
                context,
                androidPermission
            ) == PackageManager.PERMISSION_GRANTED

            if (isGranted) {
                PermissionState.Granted
            } else {
                PermissionState.Denied
            }
        }
        logger.d("New permission states calculated: %s", states)
        return states
    }

    private val AppPermission.toAndroidPermission: String?
        get() = when (this) {
            AppPermission.FINE_LOCATION -> Manifest.permission.ACCESS_FINE_LOCATION
            AppPermission.COARSE_LOCATION -> Manifest.permission.ACCESS_COARSE_LOCATION
        }
}
