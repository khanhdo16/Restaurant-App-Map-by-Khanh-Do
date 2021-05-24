package com.example.restaurantmapapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

public class MapsFragment extends Fragment {
    private static final String NAME = "name";
    private static final String LOCATION = "location";
    private static final String LOCATIONS = "locations";

    private String mName = null;
    private LatLng mLocation = null;
    private LinkedHashMap<String, LatLng> mLocations = null;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            if (mName != null && mLocation != null) {
                googleMap.addMarker(new MarkerOptions().position(mLocation).title(mName));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLocation, 19));
            }
            else if (mLocations != null) {
                for (Map.Entry<String, LatLng> entry : mLocations.entrySet()) {
                    googleMap.addMarker(new MarkerOptions().position(entry.getValue()).title(entry.getKey())
                        .icon(BitmapDescriptorFactory.defaultMarker(new Random().nextInt(360))));
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(entry.getValue(), 5));
                }
            }
        }
    };

    public static MapsFragment newInstance(String name, LatLng location) {
        MapsFragment fragment = new MapsFragment();
        Bundle args = new Bundle();
        args.putString(NAME, name);
        args.putParcelable(LOCATION, location);
        fragment.setArguments(args);
        return fragment;
    }

    public static MapsFragment newInstance(LinkedHashMap<String, LatLng> locations) {
        MapsFragment fragment = new MapsFragment();
        Bundle args = new Bundle();
        args.putSerializable(LOCATIONS, locations);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mName = getArguments().getString(NAME);
            mLocation = getArguments().getParcelable(LOCATION);
            mLocations = (LinkedHashMap<String, LatLng>) getArguments().getSerializable(LOCATIONS);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps, container, false);
        SupportMapFragment mapFragment = new SupportMapFragment();
        getChildFragmentManager().beginTransaction()
            .replace(R.id.map, mapFragment)
            .commit();
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
        return view;
    }

    @Override
    public void onDetach() {
        FrameLayout mainFrameLayout = (FrameLayout) getActivity().findViewById(R.id.mainFrameLayout);
        FrameLayout addFrameLayout = (FrameLayout) getActivity().findViewById(R.id.addFrameLayout);
        if (mainFrameLayout != null) {
            mainFrameLayout.setVisibility(View.GONE);
        }
        if (addFrameLayout != null) {
            addFrameLayout.setVisibility(View.GONE);
        }
        super.onDetach();
    }
}
