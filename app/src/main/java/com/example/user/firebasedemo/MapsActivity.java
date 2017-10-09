package com.example.user.firebasedemo;

import android.app.Dialog;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (GSA())
        {
            //Toast.makeText(MapsActivity.this,"SERVIce perfect",Toast.LENGTH_LONG).show();
            setContentView(R.layout.activity_maps);
            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            initMap();
        }
        else
        {

        }



    }
    private void initMap()
    {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public boolean GSA()
    {
        GoogleApiAvailability googleApiAvailability=GoogleApiAvailability.getInstance();
        int isAvailable=googleApiAvailability.isGooglePlayServicesAvailable(this);
        if(isAvailable== ConnectionResult.SUCCESS)
            return  true;
        else
        if (googleApiAvailability.isUserResolvableError(isAvailable))
        {
            Dialog dialog=googleApiAvailability.getErrorDialog(this,isAvailable,0);
        }
        else
        {
            Toast.makeText(MapsActivity.this,"Cant connect",Toast.LENGTH_LONG).show();
        }
        return  false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //gotolocation(23.803169, 90.360704,20);
        // Add a marker in Sydney and move the camera
        // LatLng sydney = new LatLng(-34, 151);
        // mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        // mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
    private void gotolocation(double lat,double lng)
    {
        LatLng latLng=new LatLng(lat,lng);
        CameraUpdate cameraUpdate=CameraUpdateFactory.newLatLng(latLng);
        mMap.animateCamera(cameraUpdate);
    }
    private void gotolocationzoom(double lat,double lng,float zoom)
    {
        LatLng latLng=new LatLng(lat,lng);
        CameraUpdate cameraUpdate=CameraUpdateFactory.newLatLngZoom(latLng,zoom);
        mMap.animateCamera(cameraUpdate);
    }
    public void search(View view) throws IOException {
        EditText et=(EditText) findViewById(R.id.editText);
        String location=et.getText().toString();

        Geocoder geocoder= new Geocoder(this);
        List<Address> list=geocoder.getFromLocationName(location,1);
        Address address=list.get(0);
        String locality= address.getLocality();
//        Toast.makeText(MapsActivity.this,locality,Toast.LENGTH_LONG).show();

        double lat=address.getLatitude();
        double lng=address.getLongitude();

        gotolocationzoom(lat,lng,15);
        LatLng latLng=new LatLng(lat,lng);
        mMap.addMarker(new MarkerOptions().position(latLng).title(location));
    }
}
