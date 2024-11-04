package com.thoughtworks.voiceassistant.app.ui.views.permission

import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PermissionRequestScreen(
    requiredPermissions: Array<String>,
    onAllPermissionsGranted: () -> Unit,
) {
    val context = LocalContext.current

    // State to track permissions status
    var deniedPermissions by remember { mutableStateOf<List<String>>(emptyList()) }

    // Register the permission launcher
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        // Update deniedPermissions with permissions that are not granted
        deniedPermissions = permissions.filter { !it.value }.map { it.key }

        // If no permissions are denied, call the callback
        if (deniedPermissions.isEmpty()) {
            onAllPermissionsGranted()
        }
    }

    // Check initial permissions
    LaunchedEffect(Unit) {
        deniedPermissions = requiredPermissions.filter {
            ContextCompat.checkSelfPermission(context, it) != PackageManager.PERMISSION_GRANTED
        }

        // If no permissions are denied initially, call the callback
        if (deniedPermissions.isEmpty()) {
            onAllPermissionsGranted()
        } else {
            permissionLauncher.launch(deniedPermissions.toTypedArray())
        }
    }

    // UI
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Permission Request") })
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(top = 84.dp)
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                if (deniedPermissions.isEmpty()) {
                    Text("All permissions granted!")
                } else {
                    Text("Denied Permissions:")
                    deniedPermissions.forEach { permission ->
                        val displayPermission = permission.replace("android.permission.", "")
                        Text(displayPermission)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(onClick = {
                        permissionLauncher.launch(deniedPermissions.toTypedArray())
                    }) {
                        Text("Request Permissions")
                    }
                }
            }
        }
    )
}