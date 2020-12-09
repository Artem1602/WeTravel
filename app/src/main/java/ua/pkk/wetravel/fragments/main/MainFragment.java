package ua.pkk.wetravel.fragments.main;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;

import ua.pkk.wetravel.R;
import ua.pkk.wetravel.databinding.FragmentMainBinding;
import ua.pkk.wetravel.fragments.userAccount.UserAccountViewModel;
import ua.pkk.wetravel.utils.Keys;
import ua.pkk.wetravel.utils.User;
import ua.pkk.wetravel.utils.Video;


public class MainFragment extends Fragment {
    private FragmentMainBinding binding;
    private NavController navController;
    private MainFragmentViewModel viewModel;
    private ArrayList<Video> userVideo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false);
        viewModel = new ViewModelProvider(this).get(MainFragmentViewModel.class);
        viewModel.load_user_info(getContext().getFilesDir());
        viewModel.preLoadUserVideo();

        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        binding.loadVideo.setOnClickListener(v -> goToLoadVideoMap());
        binding.showMyVideo.setOnClickListener(v -> goToShowVideo());
        binding.logout.setOnClickListener(v -> logOut());
        binding.showMap.setOnClickListener(v -> goToShowMap());
        binding.myAccountBtn.setOnClickListener(v -> goToMyAccount());

        FirebaseAuth.getInstance().signInAnonymously();


        userVideo = new ArrayList<>();
        viewModel.preLoadVideo.observe(getViewLifecycleOwner(), video -> {
            if (video != null){
                userVideo.add(video);
            }
        });


        return binding.getRoot();
    }

    private void logOut() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("user_data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userID", "");
        editor.apply();
        File user_img = new File(getContext().getFilesDir(), "profile_img");
        user_img.delete();
        User.getInstance().cleanData();
        navController.navigate(MainFragmentDirections.actionMainFragmentToLoginPageFragment());
    }

    private void goToMyAccount() {
        File user_img = new File(getContext().getFilesDir(),"profile_img");
        String path = null;
        if (user_img.length() != 0){
            path = user_img.getAbsolutePath();
        }
        navController.navigate(MainFragmentDirections
                .actionMainFragmentToUserAccountFragment(path,
                        User.getInstance().getName(),User.getInstance().getInfo(), Keys.OWNER_ACCOUNT.getValue()));
    }

    private void goToShowMap() {
        navController.navigate(MainFragmentDirections.actionMainFragmentToVideoMapsFragment());
    }

    private void goToLoadVideoMap() {
        navController.navigate(MainFragmentDirections.actionMainFragmentToLoadVideomapsFragment());
    }

    private void goToShowVideo() {
        navController.navigate(MainFragmentDirections.actionMainFragmentToShowVideoFragment(userVideo.toArray(new Video[userVideo.size()])));
    }

}