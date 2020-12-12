package ua.pkk.wetravel.fragments.videoItem;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.HashMap;

import ua.pkk.wetravel.R;
import ua.pkk.wetravel.databinding.FragmentVideoBinding;
import ua.pkk.wetravel.utils.Keys;
import ua.pkk.wetravel.utils.Video;

public class VideoFragment extends Fragment {
    private FragmentVideoBinding binding;
    private VideoFragmentViewModel viewModel;
    private Video video;

    private String name;
    private String info;

    private SimpleExoPlayer player;
    private PlayerView playerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_video, container, false);
        VideoFragmentArgs args = VideoFragmentArgs.fromBundle(getArguments());
        binding.setVideo(args.getVideo());

        video = args.getVideo();
        initPlayer();

        VideoFragmentViewModelFactory factory = new VideoFragmentViewModelFactory(video, player);
        viewModel = new ViewModelProvider(this, factory).get(VideoFragmentViewModel.class);
        binding.setVideoViewModel(viewModel);

        if (viewModel.previousDuration <= 0) {
            viewModel.playVideoFromUri();
        } else viewModel.reCreatePlayerAndPlay(player);

        if(getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            changeUIbbyKey(args.getSourceKey());
        }

        viewModel.successDelete.observe(getViewLifecycleOwner(), aBoolean -> {
            if (aBoolean) {
                closeFragment();
            }
        });

        binding.getRoot().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        return binding.getRoot();
    }

    private void initPlayer() {
        player = new SimpleExoPlayer.Builder(getContext()).build();
        playerView = (PlayerView) binding.videoView;
        playerView.setPlayer(player);
    }

    private void closeFragment() {
        Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(VideoFragmentDirections.actionVideoFragmentToMainFragment());
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
            binding.loaderInfo.setVisibility(View.GONE);
        }
    }

    private void goToRootUser() {
        //TODO refactor it
        File file = new File(getContext().getFilesDir(), "temp_img");
        Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(VideoFragmentDirections
                .actionVideoFragmentToUserAccountFragment(file.getAbsolutePath(), name, info, Keys.LOADER_ACCOUNT.getValue()));
    }

    private void loadUserData() {
        StorageReference reference = FirebaseStorage.getInstance().getReference();

        reference.child(video.getUpload_user_id()).child("profile_img").getDownloadUrl().addOnSuccessListener(uri -> {
            viewModel.getBitmapFromURL(uri.toString(), getContext().getFilesDir());
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

    @Override
    public void onStop() {
        //TODO
        viewModel.previousDuration = player.getCurrentPosition();
        player.stop();
        player.clearMediaItems();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        //player.release();
        super.onDestroy();
    }
}