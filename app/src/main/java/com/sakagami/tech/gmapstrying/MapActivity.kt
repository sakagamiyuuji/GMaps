package com.sakagami.tech.gmapstrying

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mapFragment: SupportMapFragment
    private lateinit var mMap: GoogleMap

    private val mapType by lazy { intent.getIntExtra(KEY_TYPE_MAP, -1) }
    private val mapLocation by lazy { intent.getParcelableExtra<MapLocation>(KEY_MAP_LOCATION) }
    private val fullMapLocation by lazy { intent.getParcelableArrayListExtra<MapLocation>(KEY_SHOW_FULL_MAP)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        mapFragment =
            supportFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        when (mapType) {
            SINGLE_MAP -> {
                mapLocation?.let { mapLoc ->
                    if (mapLoc.langitude != null && mapLoc.longitude != null) {
                        val location = LatLng(mapLoc.langitude, mapLoc.longitude)
                        mMap.addMarker(
                            MarkerOptions().position(location).title("Marker in ${mapLoc.markerName}")
                        )
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))
                    } else {
                        showAlertError("Location is null", false) { dialog: DialogInterface, _: Int ->
                            dialog.dismiss()
                        }
                    }
                }
            }
            MULTIPLE_MAP -> {
                enableMyLocation()
                fullMapLocation?.forEach { mapLocation->
                    mapLocation?.let { mapLoc ->
                        if (mapLoc.langitude != null && mapLoc.longitude != null) {
                            val location = LatLng(mapLoc.langitude, mapLoc.longitude)
                            mMap.addMarker(
                                MarkerOptions().position(location).title("Marker in ${mapLoc.markerName}")
                            )
                        }
                    }
                }

                val firstLang = fullMapLocation?.get(0)?.langitude
                val firstLong = fullMapLocation?.get(0)?.longitude
                if (firstLang != null && firstLong != null) {
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(firstLang, firstLong), 10f))
                }

            }
        }

    }

    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    private fun enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
        } else {
            // Permission to access the location is missing. Show rationale and request permission
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1
            )
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        when (requestCode) {
            1 -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED) {
                    if ((ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
                        Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
                    finish()
                }
                return
            }
        }
    }

    fun showAlertError(
        message: String,
        cancelable: Boolean,
        positiveListener: DialogInterface.OnClickListener
    ) {

        val builder = AlertDialog.Builder(this)
        builder.setPositiveButton("Keluar", positiveListener)
        builder.setCancelable(cancelable)
        builder.setMessage(message)
        builder.show()

    }

    companion object {
        private const val KEY_MAP_LOCATION = "KEY_MAP_LOCATION"
        private const val KEY_SHOW_FULL_MAP = "KEY_SHOW_FULL_MAP"
        private const val KEY_TYPE_MAP = "KEY_TYPE_MAP"

        private const val SINGLE_MAP = 1
        private const val MULTIPLE_MAP = 2

        fun launchIntentSingleMap(context: Context, mapLocation: MapLocation) {
            val intent = Intent(context, MapActivity::class.java)
            intent.putExtra(KEY_MAP_LOCATION, mapLocation)
            intent.putExtra(KEY_TYPE_MAP, SINGLE_MAP)
            context.startActivity(intent)
        }

        fun launchIntentFullMap(context: Context, listMap: ArrayList<MapLocation>) {
            val intent = Intent(context, MapActivity::class.java)
            intent.putParcelableArrayListExtra(KEY_SHOW_FULL_MAP, listMap)
            intent.putExtra(KEY_TYPE_MAP, MULTIPLE_MAP)
            context.startActivity(intent)
        }

    }
}