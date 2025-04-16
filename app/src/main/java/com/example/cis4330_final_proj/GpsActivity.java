package com.example.cis4330_final_proj;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class GpsActivity extends AppCompatActivity {

    private FusedLocationProviderClient fusedLocationClient;
    private static final int LOCATION_PERMISSION_REQUEST = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST);
        } else {
            getLocation();
        }
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            double lat = location.getLatitude();
                            double lon = location.getLongitude();

                            Log.d("GPS", "Lat: " + lat + ", Lon: " + lon);

                            String building = determineBuilding(lat, lon);
                            Toast.makeText(GpsActivity.this, "You're in " + building, Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private String determineBuilding(double lat, double lon) {
        if (lat > 39.980 && lat < 39.981 && lon > -75.158 && lon < -75.157) {
            return "Building A";
        } else if (lat > 39.982 && lat < 39.983 && lon > -75.156 && lon < -75.155) {
            return "Building B";
        } else {
            return "Unknown Area";
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_REQUEST &&
                grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getLocation();
        } else {
            Toast.makeText(this, "Location permission is required.", Toast.LENGTH_SHORT).show();
        }
    }
}
