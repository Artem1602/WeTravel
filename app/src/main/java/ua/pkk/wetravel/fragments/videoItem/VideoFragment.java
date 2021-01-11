package ua.pkk.wetravel.fragments.videoItem;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ua.pkk.wetravel.R;
import ua.pkk.wetravel.databinding.FragmentVideoBinding;
import ua.pkk.wetravel.retrofit.Comment;
import ua.pkk.wetravel.retrofit.UserAPI;
import ua.pkk.wetravel.retrofit.UserData;
import ua.pkk.wetravel.utils.Keys;
import ua.pkk.wetravel.utils.User;
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

        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            changeUIbbyKey(args.getSourceKey());
            loadCurrentUserImg();
            binding.addCommentBtn.setOnClickListener(this::createComment);
        }

        viewModel.successDelete.observe(getViewLifecycleOwner(), aBoolean -> {
            if (aBoolean) {
                closeFragment();
            }
        });
        //TODO Deprecated
        binding.getRoot().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        return binding.getRoot();
    }

    private void loadCurrentUserImg() {
        Bitmap bitmap = BitmapFactory.decodeFile(new File(getContext().getFilesDir(), "profile_img").getAbsolutePath());
        binding.userImg.setImageBitmap(bitmap);
    }

    private void initPlayer() {
        player = new SimpleExoPlayer.Builder(getContext()).build();
        playerView = binding.videoView;
        playerView.setPlayer(player);
    }

    private void closeFragment() {
        Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(VideoFragmentDirections.actionVideoFragmentToMainFragment());
    }


    private void changeUIbbyKey(int source) {
        if (source == Keys.VIDEO_FROM_MAP.getValue()) {
            binding.deleteBtn.setVisibility(View.GONE);
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
            viewModel.loadBitmapToTempFile(uri.toString(), getContext().getFilesDir());

            Glide.with(getContext()).load(uri.toString()).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    binding.uploadedUserImg.setOnClickListener(v -> goToRootUser());
                    return false;
                }
            }).apply(new RequestOptions().placeholder(R.drawable.progress_bar_animation)).into(binding.uploadedUserImg);
        });
        new Thread(
                () -> UserAPI.INSTANCE.getRETROFIT_SERVICE().getUserData(video.getUpload_user_id()).enqueue(new Callback<UserData>() {
                    @Override
                    public void onResponse(Call<UserData> call, Response<UserData> response) {
                        if (response.isSuccessful()) {
                            name = response.body().getUserName();
                            info = response.body().getUserInfo();
                            binding.userName.setText(name);
                        }
                    }

                    @Override
                    public void onFailure(Call<UserData> call, Throwable t) {
                        //TODO
                    }
                })).start();
    }

    private void createComment(View v) {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM", Locale.getDefault());
        Comment commentBody = new Comment(
                binding.videoComment.getText().toString(),
                formatter.format(date),
                User.getInstance().getId(),
                User.getInstance().getImgUri(),
                User.getInstance().getName()
        );

        //owner id -> video name -> comment id:coment body

        new Thread(() -> {
            UserAPI.INSTANCE.getRETROFIT_SERVICE().createComment(video.getUpload_user_id(), video.getName(), UUID.randomUUID().toString(), commentBody).enqueue(new Callback<Comment>() {
                @Override
                public void onResponse(Call<Comment> call, Response<Comment> response) {
                    //TODO
                }

                @Override
                public void onFailure(Call<Comment> call, Throwable t) {

                }
            });
        }).start();

    }

    @Override
    public void onStop() {
        viewModel.previousDuration = player.getCurrentPosition();
        player.stop();
        player.clearMediaItems();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}