package ua.pkk.wetravel.fragments.loadVideoMap;

import android.Manifest;
import android.animation.Animator;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import ua.pkk.wetravel.R;
import ua.pkk.wetravel.databinding.FragmentLoadVideoMapsBinding;
import ua.pkk.wetravel.utils.User;

//TODO Fix problem with keyboard Priority -> CRITICAL
public class LoadVideoMapFragment extends Fragment {
    private FragmentLoadVideoMapsBinding binding;
    private LatLng marker;
    public int VIDEO_FILE_REQUEST_CODE = 1;
    private String loadVideoName;
    private LoadVideoMapViewModel viewModel;
    private boolean is_add_video_show;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {
        private GoogleMap map;

        @Override
        public void onMapReady(GoogleMap googleMap) {
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                googleMap.setMyLocationEnabled(true);
            }
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(50.33, 30.53)));
            googleMap.setOnMapLongClickListener(this::onMapLongClick);
            map = googleMap;
        }


        public void onMapLongClick(LatLng latLng) {
            marker = latLng;
            map.clear();
            map.addMarker(new MarkerOptions().position(latLng));
            map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        }
    };

    private void checkMapPermissions() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
        }
    }

    private void checkVideoPermissions() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_load_video_maps, container, false);
        binding.addVideoFab.setOnClickListener(this::onAddVideoFab);
        binding.selectVideoBtn.setOnClickListener(this::onSelectVideo);
        checkMapPermissions();
        checkVideoPermissions();
        viewModel = new ViewModelProvider(this).get(LoadVideoMapViewModel.class);
        is_add_video_show = false;
        //TODO Put in another place
        binding.cancelBtn.setOnClickListener(v -> {
            is_add_video_show = false;
            binding.TEST.animate().scaleX(0).scaleY(0).setDuration(800);
            binding.addVideoLayout.animate().y(binding.loadVideoMapLayout.getHeight()).setDuration(800);
            binding.addVideoFab.setVisibility(View.VISIBLE);
            binding.addVideoFab.animate().scaleX(1).scaleY(1).setDuration(1000);
        });
        return binding.getRoot();
    }

    private void showNotification(Intent data, LatLng marker, String s) {
        String CHANNEL_ID = "SuccessUpload";
        int NOTIFICATION_ID = new Random().nextInt(256);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getContext());
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), CHANNEL_ID);
        builder.setContentTitle(getString(R.string.video_upload))
                .setContentText(getString(R.string.upload_in_process))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSmallIcon(R.drawable.ic_notification_icon)
                .setOnlyAlertOnce(true);

        createNotificationChannel(CHANNEL_ID);

        new Thread(() -> {
            Context context = getContext();
            builder.setProgress(100, 0, false);
            notificationManager.notify(NOTIFICATION_ID, builder.build());

            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(User.getInstance().getId()).child(s);
            Date date = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());

            StorageMetadata.Builder metadata = new StorageMetadata.Builder()
                    .setCustomMetadata("position", marker.latitude + "/" + marker.longitude);
            metadata.setCustomMetadata("uploadingTime", formatter.format(date));
            metadata.setCustomMetadata("user_id", User.getInstance().getId());

            UploadTask uploadTask = storageReference.putFile(data.getData(), metadata.build());
            uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    if (snapshot.getBytesTransferred() == snapshot.getTotalByteCount()) {
                        builder.setProgress(0, 0, false);
                        builder.setContentText(context.getString(R.string.upload_complete));
                        notificationManager.notify(NOTIFICATION_ID, builder.build());
                        return;
                    }
                    Double progress = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                    builder.setProgress(100, progress.intValue(), false);
                    notificationManager.notify(NOTIFICATION_ID, builder.build());
                }
            });
        }).start();

        //Navigate to MainFragment
        Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(LoadVideoMapFragmentDirections.actionLoadVideoMapsFragmentToMainFragment());
    }

    private void createNotificationChannel(String CHANNEL_ID) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.notification_chanel_name);
            String description = getString(R.string.notification_chanel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void onAddVideoFab(View view) {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getContext(), getContext().getString(R.string.storage_permission), Toast.LENGTH_LONG).show();
            checkVideoPermissions();
            return;
        }
        if (marker == null) {
            Toast.makeText(getContext(), getContext().getText(R.string.addMarker), Toast.LENGTH_LONG).show();
            return;
        }
        //TODO Replace it
        binding.addVideoFab.animate().setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (is_add_video_show) binding.addVideoFab.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        if (!is_add_video_show) {
            is_add_video_show = true;
            binding.TEST.animate().scaleX(30).scaleY(30).setDuration(800);
            binding.addVideoFab.animate().scaleX(0).scaleY(0).setDuration(1000);
            binding.addVideoLayout.animate().y(0).setDuration(800);
        }
    }

    public void onSelectVideo(View v) {
        viewModel.checkNames(binding.videoName.getText().toString());
        viewModel.hasSameNames.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    binding.videoName.setError(getContext().getString(R.string.enter_another_name));
                } else {
                    startActivityForResult(Intent.createChooser(new Intent(Intent.ACTION_GET_CONTENT).setType("video/*"), "Choose Video"), VIDEO_FILE_REQUEST_CODE);
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode != 1 || marker == null) {
            super.onActivityResult(requestCode, resultCode, data);
        }
        showNotification(data, marker, binding.videoName.getText().toString());
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(callback);
    }
}
