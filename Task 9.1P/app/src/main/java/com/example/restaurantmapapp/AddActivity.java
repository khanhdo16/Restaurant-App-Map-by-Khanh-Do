package com.example.restaurantmapapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.io.IOException;
import java.util.List;

public class AddActivity extends AppCompatActivity {
    public static final int PERMISSION_RESULT_CODE = 2;
    FrameLayout addFrameLayout;
    EditText nameEdit;
    EditText addressEdit;
    LatLng mLatLng;
    Location mLocation;
    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        addFrameLayout = findViewById(R.id.addFrameLayout);
        nameEdit = findViewById(R.id.nameEdit);
        addressEdit = findViewById(R.id.addressEdit);

        addressEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonAction(v);
            }
        });
    }

    public void buttonAction(View view) {
        Fragment fragment;
        FragmentTransaction transaction = AddActivity.this.getSupportFragmentManager().beginTransaction();

        switch (view.getId()) {
            case R.id.addressEdit:
                fragment = new PickFragment();
                transaction
                    .replace(R.id.addFrameLayout, fragment)
                    .addToBackStack(null)
                    .commit();

                addFrameLayout.setVisibility(View.VISIBLE);
                break;
            case R.id.getButton:
                getLocation();
                break;
            case R.id.showThisButton:
                if (!TextUtils.isEmpty(nameEdit.getText()) && mLatLng != null) {
                    fragment = MapsFragment.newInstance(nameEdit.getText().toString(), mLatLng);
                    transaction
                        .replace(R.id.addFrameLayout, fragment)
                        .addToBackStack(null)
                        .commit();

                    addFrameLayout.setVisibility(View.VISIBLE);
                }
                else {
                    Toast.makeText(this, "Please select a name/location!", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.saveButton:
                if (!TextUtils.isEmpty(nameEdit.getText()) && mLatLng != null) {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("name", nameEdit.getText().toString());
                    returnIntent.putExtra("location", mLatLng);
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }
                else {
                    Toast.makeText(this, "Please select a name/location!", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                throw new IllegalStateException("Something went wrong! Please try again.");
        }
    }

    public void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_RESULT_CODE);
        }
        else {
            locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
            mLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, location -> mLocation = location);
            if (mLocation != null) {
                mLatLng = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());
                Geocoder geocoder = new Geocoder(this);
                try {
                    List<Address> address = geocoder.getFromLocation(mLocation.getLatitude(), mLocation.getLongitude(), 1);
                    addressEdit.setText(address.get(0).getAddressLine(0));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                getLocation();
            }
        }
        else {
            Toast.makeText(this, "Location permission denied! Please grant location permission.", Toast.LENGTH_SHORT).show();
        }
    }
}
