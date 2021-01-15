package ua.pkk.wetravel.fragments.showVideoOnMap;

import android.animation.Animator;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
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

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import ua.pkk.wetravel.R;
import ua.pkk.wetravel.databinding.FragmentVideoMapsBinding;
import ua.pkk.wetravel.utils.Keys;
import ua.pkk.wetravel.utils.Video;

public class VideoMapsFragment extends Fragment {
    private VideoMapsViewModel viewModel;
    private GoogleMap map;
    private FragmentVideoMapsBinding binding;
    private ArrayList<Pair<MarkerOptions, Video>> videos;

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
        viewModel = new ViewModelProvider(this).get(VideoMapsViewModel.class);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_video_maps, container, false);
        videos = new ArrayList<>();

        viewModel.markers.observe(getViewLifecycleOwner(), markerOptionsVideoPair -> {
            if (map == null) return;
            binding.mapLoadPb.setVisibility(View.GONE);
            binding.mapLoadTv.setVisibility(View.GONE);
            setMarkers(markerOptionsVideoPair);
            videos.add(markerOptionsVideoPair);
        });
        viewModel.getMarkers();
        initSearchLayout();
        return binding.getRoot();
    }

    private void initSearchLayout() {
        //TODO AtomicBoolean
        AtomicBoolean isSearchLayoutOnScreen = new AtomicBoolean(false);
        binding.searchFab.animate().setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (isSearchLayoutOnScreen.get()) binding.searchFab.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        binding.searchFab.setOnClickListener(v -> {
            binding.filterLayout.setVisibility(View.VISIBLE);
            binding.filterLayout.animate().translationY(binding.filterLayout.getHeight() * -1).setDuration(500);
            isSearchLayoutOnScreen.set(true);
            binding.searchFab.animate().scaleX(0).scaleY(0).setDuration(500);

        });
        binding.closeFilterBtn.setOnClickListener(v -> {
            binding.searchFab.setVisibility(View.VISIBLE);
            binding.filterLayout.animate().translationY(binding.filterLayout.getHeight()).setDuration(500);
            isSearchLayoutOnScreen.set(false);
            binding.searchFab.animate().scaleY(1).scaleX(1).setDuration(500);
        });
        //TEST
        binding.testBtn.setOnClickListener(v -> {
            for (Pair<MarkerOptions, Video> i : videos) {
                if (i.second.getName().equals(binding.testEd.getText().toString())) {
                    map.moveCamera(CameraUpdateFactory.newLatLng(i.first.getPosition()));
                }
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

    private void setMarkers(Pair<MarkerOptions, Video> markerVideoHashMap) {
        Marker marker = map.addMarker(markerVideoHashMap.first);
        marker.setTag(markerVideoHashMap.second);
    }

}