package com.tec.studentapp;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.location.Location;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tec.entities.Conductor;

import java.io.IOException;

public class StudentMapActivity extends FragmentActivity implements OnMapReadyCallback , GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener{

    private GoogleMap mMap;
    GoogleApiClient googleApiClient;
    Location lastLocation;
    LocationRequest locationRequest;
    SupportMapFragment mapFragment;
    Marker ubicacion;
    Conductor conductor;
    Marker markerConductor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_map);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(StudentMapActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
        }else{
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //Se pone marcador del TEC (Nodo final)
        final LatLng  posTEC = new LatLng(9.857191, -83.912284);
        Marker TEC = mMap.addMarker(new MarkerOptions().position(posTEC).title("TEC").icon(BitmapDescriptorFactory.fromResource(R.drawable.tec)));

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        buildGoogleApiClient();

        //se quita la ubicacion real del estudiate
        mMap.setMyLocationEnabled(false);

        //Se obtiene la ubicacion del driver y se coloca en el mapa
       // LatLng locationChofer = new LatLng(conductor.getPosicionHogar().getLat(), conductor.getPosicionHogar().getLon()); //cambiar por la ruta no pos HOGAR
       // markerConductor = mMap.addMarker(new MarkerOptions().position(locationChofer).title("Ride").icon(BitmapDescriptorFactory.fromResource(R.drawable.car_left)));
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (ubicacion == null) {
                    //marcador de la ubicacion simulada del estudiante
                    ubicacion = mMap.addMarker(new MarkerOptions().position(latLng).title("Tu ubicacion").icon(BitmapDescriptorFactory.fromResource(R.drawable.student)));
                    Dialog dialog = alertDialog();
                    dialog.show();
                }
            }});
    }

    //animacion del movimiento del driver
    public void animateMarker(final Marker marker, final LatLng toPosition,
                              final boolean hideMarker) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = mMap.getProjection();
        Point startPoint = proj.toScreenLocation(marker.getPosition());
        final LatLng startLatLng = proj.fromScreenLocation(startPoint);
        final long duration = 20000;
        final Interpolator interpolator = new LinearInterpolator();

        handler.post(new Runnable() {
            @Override
            public void run() {

                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / duration);
                double lng = t * toPosition.longitude + (1 - t)
                        * startLatLng.longitude;
                double lat = t * toPosition.latitude + (1 - t)
                        * startLatLng.latitude;
                marker.setPosition(new LatLng(lat, lng));

                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 30);
                } else {
                    if (hideMarker) {
                        marker.setVisible(false);
                    } else {
                        marker.setVisible(true);
                    }
                }
            }
        });
    }

    //dialogo de alerta por para empezar el viaje
    public Dialog alertDialog() {
        AlertDialog.Builder mbuilder = new AlertDialog.Builder(StudentMapActivity.this);
        mbuilder.setMessage("Iniciar viaje?")
                .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                    //si respuesta igual a SI se registra la informacion en el server
                    public void onClick(DialogInterface dialog, int id) {
                        Double lat = ubicacion.getPosition().latitude;
                        Double lon = ubicacion.getPosition().longitude;
                        if (RegistrationActivity.estudiante != null) {
                            RegistrationActivity.estudiante.setPosicionHogar(new Posicion(lat, lon));

                     /*   NuevoEstudiante test = new NuevoEstudiante();
                        try {
                            test.registrar(RegistrationActivity.estudiante);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }*/
                        }

                        if (MainActivity.estudiante != null) {
                            MainActivity.estudiante.setPosicionHogar(new Posicion(lat, lon));
                      /*  NuevoEstudiante test = new NuevoEstudiante();
                        try {
                            test.registrar(RegistrationActivity.estudiante);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }*/
                        }

                        //cuando la ubicacion del chofer y el estudiante coinciden, se borra el marcador del estudiante y se dirijen al TEC
                        //animateMarker(ubicacion,locationChofer,false);
                        //if(latLng.equals(locationChofer)){
                        //  ubicacion.remove();
                        // animateMarker(locationChofer,posTEC,false);
                        //  }
                    }
                })

                //si la respuesta es NO, se puede volver a  escoger ubicacion
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ubicacion.remove();
                        ubicacion = null;
                        Toast.makeText(getApplicationContext(),"Volver a seleccionar ubicacion",Toast.LENGTH_SHORT).show();

                        onMapReady(mMap);

                    }
                });
        return mbuilder.create();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(StudentMapActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
        }

        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    protected synchronized void buildGoogleApiClient(){
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        lastLocation = location;
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
    }

    final int LOCATION_REQUEST_CODE = 1;
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case LOCATION_REQUEST_CODE:{
                if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    mapFragment.getMapAsync(this);
                }else{
                    Toast.makeText(getApplicationContext(), "Please provide the permission", Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
    }
}
