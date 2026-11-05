package com.tecsup.tecsupcerca

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState

data class Punto(
    val nombre: String,
    val tipo: String,
    val lat: Double,
    val lng: Double,
    val hue: Float
)

val puntos = listOf(
    Punto("Tecsup", "Campus", -16.429175, -71.5196449, BitmapDescriptorFactory.HUE_RED),
    Punto("El muchacho alegre", "Comida", -16.4290505, -71.5193563, BitmapDescriptorFactory.HUE_ORANGE),
    Punto("Minimarket Mau", "Tienda", -16.4281569, -71.5205612, BitmapDescriptorFactory.HUE_GREEN),
    Punto("Librería Regalería", "Estudio", -16.4286202, -71.5228283, BitmapDescriptorFactory.HUE_AZURE),
    Punto("Boticas Rosetfarma", "Salud", -16.4295633, -71.523743, BitmapDescriptorFactory.HUE_VIOLET),
    Punto("Paradero Strio de Chapi", "Transporte", -16.4252523, -71.5301501, BitmapDescriptorFactory.HUE_YELLOW)
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MapaScreen()
        }
    }
}

@Composable
fun MapaScreen() {
    val context = LocalContext.current

    var tienePermiso by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { concedido -> tienePermiso = concedido }

    LaunchedEffect(Unit) {
        if (!tienePermiso) launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    val tecsup = LatLng(puntos[0].lat, puntos[0].lng)
    val cameraState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(tecsup, 15f)
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraState,
        properties = MapProperties(isMyLocationEnabled = tienePermiso)
    ) {
        puntos.forEach { p ->
            Marker(
                state = rememberMarkerState(position = LatLng(p.lat, p.lng)),
                title = p.nombre,
                snippet = p.tipo,
                icon = BitmapDescriptorFactory.defaultMarker(p.hue)
            )
        }
    }
}