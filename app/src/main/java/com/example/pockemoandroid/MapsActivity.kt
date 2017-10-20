package com.example.pockemoandroid

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager


import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.widget.Toast

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.util.concurrent.ExecutorService

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        checkPermission()
    }


    var CheckPERMITIONVARIABLE = 123
     fun checkPermission() {
         if (Build.VERSION.SDK_INT >= 23) {
             if (ActivityCompat.
                     checkSelfPermission(this,
                             android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                 requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), CheckPERMITIONVARIABLE)
                 return
             }


         }
         getLocatoin()
     }


    @SuppressLint("ServiceCast")
    fun getLocatoin(){

        var myLocation = MyLocationLisner()
        var locationManager =getSystemService(Context.LOCATION_SERVICE) as LocationManager

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,3,3f,myLocation)

        var mythread = myThread()
        mythread.start()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode){
            CheckPERMITIONVARIABLE -> {
                if (grantResults[0]==PackageManager.PERMISSION_GRANTED) {
                    getLocatoin()
                }else{
                    Toast.makeText(this,"do not find location",Toast.LENGTH_LONG).show()
                }
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }



    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(50.4, 30.5)
        mMap.addMarker(MarkerOptions().
                position(sydney).
                title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }
    var location:Location?=null
    var oldlocation:Location?=null

    inner class MyLocationLisner:LocationListener {


        constructor(){
            oldlocation= Location("start")
            oldlocation!!.latitude=0.0
            oldlocation!!.longitude=0.0
        }
        override fun onLocationChanged(p0: Location?) {
            location=p0
        }

        override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onProviderEnabled(p0: String?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onProviderDisabled(p0: String?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }

    inner class myThread:Thread{
        constructor():super(){

        }

        override fun run() {
            while (true){
                try {
                    if (oldlocation!!.distanceTo(location)==50f){
                        oldlocation=location
                        continue
                    }
                    oldlocation=location
                    runOnUiThread(){
                    val sydney = LatLng(location!!.latitude,location!!.longitude)
                    mMap!!.addMarker(MarkerOptions().
                            position(sydney).
                            title("Marker"))
                    mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,14f))
                }
                    Thread.sleep(5000)
                }catch (e:Exception){}
            }
        }
    }

}
