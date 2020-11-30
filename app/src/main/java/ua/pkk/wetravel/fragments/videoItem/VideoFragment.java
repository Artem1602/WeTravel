package ua.pkk.wetravel.fragments.videoItem;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import ua.pkk.wetravel.R;
import ua.pkk.wetravel.databinding.FragmentVideoBinding;
import ua.pkk.wetravel.utils.Keys;
import ua.pkk.wetravel.utils.Video;

//TODO Load video into videoView. Try to stream it. (maybe byte[])...
public class VideoFragment extends Fragment {
    private FragmentVideoBinding binding;
    private VideoFragmentViewModel viewModel;
    private Video video;

    private String name;
    private String info;
    private String file;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_video, container, false);
        VideoFragmentArgs args = VideoFragmentArgs.fromBundle(getArguments());
        binding.setVideo(args.getVideo());

        video = args.getVideo();

        VideoFragmentViewModelFactory factory = new VideoFragmentViewModelFactory(video);
        viewModel = new ViewModelProvider(this, factory).get(VideoFragmentViewModel.class);
        binding.setVideoViewModel(viewModel);

        changeUIbbyKey(args.getSourceKey());

        //TODO Add play btn or else...
        viewModel.getVideoUri();

        viewModel.videoUri.observe(getViewLifecycleOwner(), uri -> {
            if (uri != null) {
                showVideo(uri);
            }
        });

        viewModel.successDelete.observe(getViewLifecycleOwner(), aBoolean -> {
            if (aBoolean) {
                closeFragment();
            }
        });

        return binding.getRoot();
    }

    private void closeFragment() {
        Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(VideoFragmentDirections.actionVideoFragmentToShowVideoFragment());
    }

    private void showVideo(Uri uri) {
        MediaController mediaController = new MediaController(getContext());
        mediaController.setAnchorView(binding.videoView);
        binding.videoView.setMediaController(mediaController);
        binding.videoView.setVideoURI(uri);
        binding.videoView.requestFocus();
        binding.videoView.start();
    }

    private void changeUIbbyKey(int source) {
        if (source == Keys.VIDEO_FROM_MAP.getValue()) {
            binding.videoActions.setVisibility(View.GONE);
            viewModel.img.observe(getViewLifecycleOwner(), bitmap -> {
                if (bitmap != null) {
                    binding.userImagePb.setVisibility(View.GONE);
                    binding.uplodetUserImg.setImageBitmap(bitmap);
                    binding.uplodetUserImg.setOnClickListener(v -> goToRootUser());
                }
            });
            loadUserData();
        } else {
            //Do nothing
        }
    }

    private void goToRootUser() {
        //TODO refactor it
        File file = new File(getContext().getFilesDir(), "temp_img");
        Navigation.findNavController(getActivity(),R.id.nav_host_fragment).navigate(VideoFragmentDirections
                .actionVideoFragmentToUserAccountFragment(file.getAbsolutePath(),name,info,Keys.LOADER_ACCOUNT.getValue()));
    }

    private void loadUserData() {
        StorageReference reference = FirebaseStorage.getInstance().getReference();

        reference.child(video.getUpload_user_id()).child("profile_img").getDownloadUrl().addOnSuccessListener(uri -> {
            viewModel.getBitmapFromURL(uri.toString(),getContext().getFilesDir());
        });
        FirebaseDatabase.getInstance().getReference().child("user_data").child(video.getUpload_user_id()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HashMap<String, String> map = (HashMap<String, String>) snapshot.getValue();
                if (map == null) return;
                name = map.get("user_name");
                info = map.get("user_info");
                binding.userName.setText(name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}