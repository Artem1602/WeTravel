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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.net.URI;

import ua.pkk.wetravel.R;
import ua.pkk.wetravel.databinding.FragmentMainBinding;
import ua.pkk.wetravel.fragments.userAccount.UserAccountViewModel;
import ua.pkk.wetravel.utils.Keys;
import ua.pkk.wetravel.utils.User;


public class MainFragment extends Fragment {
    private FragmentMainBinding binding;
    private NavController navController;
    private MainFragmentViewModel viewModel;

    //TODO sign up anonymously
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("TAG","onCreateView");
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false);
        viewModel = new ViewModelProvider(this).get(MainFragmentViewModel.class);

        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        binding.loadVideo.setOnClickListener(v -> goToLoadVideoMap());
        binding.showMyVideo.setOnClickListener(v -> goToShowVideo());
        binding.logout.setOnClickListener(v -> logOut());
        binding.showMap.setOnClickListener(v -> goToShowMap());

        binding.myAccountBtn.setOnClickListener(v -> goToMyAccount());

        //TODO DO SOMETHING WITH IT
        FirebaseAuth.getInstance().signInAnonymously();

        viewModel.load_user_info(getContext().getFilesDir());

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
        navController.navigate(MainFragmentDirections.actionMainFragmentToShowVideoFragment());
    }

    @Override
    public void onPause() {
        Log.d("TAG","onPause");
        super.onPause();
    }

    @Override
    public void onResume() {
        Log.d("TAG","onResume");
        super.onResume();
    }

    @Override
    public void onStop() {
        Log.d("TAG","onStop");
        super.onStop();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d("TAG","onViewCreated");
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d("TAG","onCreate");
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        Log.d("TAG","onAttach");
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        Log.d("TAG","onDetach");
        super.onDetach();
    }
}