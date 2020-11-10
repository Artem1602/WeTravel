package ua.pkk.wetravel.fragments.map;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import ua.pkk.wetravel.R;


public class MapsFragment extends Fragment{
    //TODO Add data into Markers https://stackoverflow.com/questions/16997130/adding-custom-property-to-marker-google-map-android-api-v2
    //TODO Permissions
    private OnMapReadyCallback callback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                googleMap.setMyLocationEnabled(true);
            }
            LatLng sydney = new LatLng(50.33, 30.53);
            googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Ukraine"));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

            googleMap.setOnMyLocationButtonClickListener(this::onMyLocationButtonClick);
            googleMap.setOnMarkerClickListener(this::onMarkerClick);
            googleMap.setOnInfoWindowClickListener(this::onInfoWindowClick);
        }
        public void onInfoWindowClick(Marker marker) {
            Toast.makeText(getContext(), "onInfoWindowClick", Toast.LENGTH_LONG).show();
        }
        public boolean onMarkerClick(Marker marker) {
            Toast.makeText(getContext(), "onMarkerClick", Toast.LENGTH_LONG).show();
            return false;
        }
        public boolean onMyLocationButtonClick() {
            Toast.makeText(getContext(), "onMyLocationButtonClick", Toast.LENGTH_LONG).show();
            return false;
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(callback);
    }

}