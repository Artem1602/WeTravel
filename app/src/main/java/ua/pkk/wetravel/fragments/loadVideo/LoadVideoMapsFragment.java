package ua.pkk.wetravel.fragments.loadVideo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import ua.pkk.wetravel.R;
import ua.pkk.wetravel.databinding.FragmentLoadvideoMapsBinding;


public class LoadVideoMapsFragment extends Fragment{
    //TODO Permissions

    private FragmentLoadvideoMapsBinding binding;
    private LoadVideoMapsFragmentViewModel viewModel;
    private LatLng marker;
    public int VIDEO_FILE_REQUEST_CODE = 1;
    private View builderView;
    private EditText loadVideoName;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {
        private GoogleMap map;
        @Override
        public void onMapReady(GoogleMap googleMap) {
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                googleMap.setMyLocationEnabled(true);
            }
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(50.33, 30.53)));
            googleMap.setOnMyLocationButtonClickListener(this::onMyLocationButtonClick);
            googleMap.setOnMarkerClickListener(this::onMarkerClick);
            googleMap.setOnInfoWindowClickListener(this::onInfoWindowClick);
            googleMap.setOnMapLongClickListener(this::onMapLongClick);
            map = googleMap;
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
        public void onMapLongClick(LatLng latLng) {
            marker = latLng;
            map.clear();
            map.addMarker(new MarkerOptions().position(latLng));
            map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_loadvideo_maps,container,false);
        binding.addVideo.setOnClickListener(this::onAddVideo);
        viewModel = new ViewModelProvider(this).get(LoadVideoMapsFragmentViewModel.class);

        viewModel.progress.observe(getViewLifecycleOwner(), this::changeProgressBar);
        return binding.getRoot();
    }

    private void changeProgressBar(Double aLong) {
        if (builderView == null) return;
        ProgressBar progressBar = builderView.findViewById(R.id.progressBarLoad);
        Log.d("TAG", Integer.toString(aLong.intValue()));
        progressBar.setProgress(aLong.intValue());
    }

    private void onAddVideo(View view){
        if(marker == null){
            Toast.makeText(getContext(),getContext().getText(R.string.addMarker),Toast.LENGTH_LONG).show();
            return;
        }
        LayoutInflater inflater = getLayoutInflater();
        builderView = inflater.inflate(R.layout.dialog_custom_add_video,null);
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());
        Button selectVideoButton = builderView.findViewById(R.id.selectVideo_btn);
        selectVideoButton.setOnClickListener(v -> {
            startActivityForResult(Intent.createChooser(new Intent(Intent.ACTION_GET_CONTENT).setType("video/*"), "Choose Video"), VIDEO_FILE_REQUEST_CODE);
        });
        loadVideoName = builderView.findViewById(R.id.videoName);
        builder.setView(builderView);
        builder.create();
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode != 1 || marker == null) {
            super.onActivityResult(requestCode, resultCode, data);
        }
        viewModel.uploadSelectedVideo(data,marker,loadVideoName.getText().toString());
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(callback);
    }

}