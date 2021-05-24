package com.example.restaurantmapapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.LinkedHashMap;

public class MainActivity extends AppCompatActivity {
    FrameLayout mainFrameLayout;
    public static final int ADD_RESULT_CODE = 1;
    LinkedHashMap <String, LatLng> mLocations = new LinkedHashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainFrameLayout = findViewById(R.id.mainFrameLayout);
    }

    public void buttonAction (View view) {
        switch (view.getId()) {
            case R.id.addButton:
                startActivityForResult(
                    new Intent(MainActivity.this, AddActivity.class),
                    ADD_RESULT_CODE
                );
                break;
            case R.id.showButton:
                if (mLocations.size() > 0) {
                    Fragment fragment = MapsFragment.newInstance(mLocations);
                    MainActivity.this.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.mainFrameLayout, fragment)
                        .addToBackStack(null)
                        .commit();

                    mainFrameLayout.setVisibility(View.VISIBLE);
                }
                else {
                    Toast.makeText(this, "No location found! Please add new location.", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                throw new IllegalStateException("Something went wrong! Please try again.");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_RESULT_CODE) {
            if(resultCode == Activity.RESULT_OK){
                mLocations.put(
                    data.getStringExtra("name"),
                    data.getParcelableExtra("location")
                );

                Toast.makeText(this, "Location saved successfully!", Toast.LENGTH_SHORT).show();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
            }
        }
    }
}
