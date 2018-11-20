    package com.tec.studentapp;

    import android.Manifest;
    import android.content.pm.PackageManager;
    import android.location.Location;
    import android.os.Bundle;
    import android.support.annotation.NonNull;
    import android.support.annotation.Nullable;
    import android.support.v4.app.ActivityCompat;
    import android.support.v4.app.FragmentActivity;
    import android.widget.Toast;

    import com.google.android.gms.common.ConnectionResult;
    import com.google.android.gms.common.api.GoogleApiClient;
    import com.google.android.gms.location.LocationRequest;
    import com.google.android.gms.location.LocationServices;
    import com.google.android.gms.maps.CameraUpdateFactory;
    import com.google.android.gms.maps.GoogleMap;
    import com.google.android.gms.maps.OnMapReadyCallback;
    import com.google.android.gms.maps.SupportMapFragment;
    import com.google.android.gms.maps.model.LatLng;


    public class StudentMapActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

        private GoogleMap mMap;
        GoogleApiClient googleApiClient;
        Location lastLocation;
        LocationRequest locationRequest;
        SupportMapFragment mapFragment;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            //setContentView(R.layout.activity_student_map);
            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
           // mapFragment = (SupportMapFragment) getSupportFragmentManager()
         //           .findFragmentById(R.id.map);

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(StudentMapActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
                }else{
                    mapFragment.getMapAsync(this);
                }
            }

        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            buildGoogleApiClient();
       //     mMap.setMyLocationEnabled(true);
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

        @Override
        public void onConnected(@Nullable Bundle bundle) {
            locationRequest = new LocationRequest();
            locationRequest.setInterval(2000);
            locationRequest.setFastestInterval(2000);
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(StudentMapActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
            }

         //   LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        }

        @Override
        public void onConnectionSuspended(int i) {

        }

        @Override
        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

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