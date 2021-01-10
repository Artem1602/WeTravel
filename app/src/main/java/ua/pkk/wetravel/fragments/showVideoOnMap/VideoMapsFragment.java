package ua.pkk.wetravel.fragments.showVideoOnMap;

import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.jetbrains.annotations.NotNull;

import ua.pkk.wetravel.R;
import ua.pkk.wetravel.utils.Keys;
import ua.pkk.wetravel.utils.Video;

public class VideoMapsFragment extends Fragment {
    private VideoMapsViewModel viewModel;
    private GoogleMap map;


    private OnMapReadyCallback callback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            map = googleMap;
            map.setOnInfoWindowClickListener(this::onInfoWindowClick);
            LatLng ukraine = new LatLng(33, 32);
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(ukraine));
        }

        public void onInfoWindowClick(@NotNull Marker marker) {
            Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(VideoMapsFragmentDirections.actionVideoMapsFragmentToVideoFragment((Video) marker.getTag(), Keys.VIDEO_FROM_MAP.getValue()));
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_video_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(VideoMapsViewModel.class);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
        viewModel.markers.observe(getViewLifecycleOwner(), markerOptionsVideoPair -> {
            if (map != null) {
                view.findViewById(R.id.map_load_pb).setVisibility(View.GONE);
                view.findViewById(R.id.map_load_tv).setVisibility(View.GONE);
                setMarkers(markerOptionsVideoPair);
            }
        });
        viewModel.getMarkers();
    }

    private void setMarkers(Pair<MarkerOptions, Video> markerVideoHashMap) {
        Marker marker = map.addMarker(markerVideoHashMap.first);
        marker.setTag(markerVideoHashMap.second);
    }

}