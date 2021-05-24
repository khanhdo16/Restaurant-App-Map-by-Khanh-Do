package com.example.restaurantmapapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.util.Arrays;
import java.util.Map;

public class PickFragment extends Fragment {
    public Place mPlace;

    public PickFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Places.initialize(getContext(), getResources().getString(R.string.google_maps_key));
        PlacesClient placesClient = Places.createClient(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pick, container, false);
        Button doneButton = (Button) view.findViewById(R.id.doneButton);
        EditText addressEdit = (EditText) getActivity().findViewById(R.id.addressEdit);

        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
            getChildFragmentManager().findFragmentById(R.id.pickFragment);

        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG));

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                mPlace = place;
            }


            @Override
            public void onError(@NonNull Status status) {
                Toast.makeText(getContext(),"Something went wrong! Please try again.", Toast.LENGTH_SHORT).show();
            }
        });

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addressEdit.setText(mPlace.getAddress());
                ((AddActivity) getActivity()).mLatLng = mPlace.getLatLng();
                getActivity().onBackPressed();
            }
        });
        return view;
    }

    @Override
    public void onDetach() {
        FrameLayout addFrameLayout = (FrameLayout) getActivity().findViewById(R.id.addFrameLayout);
        addFrameLayout.setVisibility(View.GONE);
        super.onDetach();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
