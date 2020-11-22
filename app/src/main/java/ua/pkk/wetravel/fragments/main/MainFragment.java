package ua.pkk.wetravel.fragments.main;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.firebase.auth.FirebaseAuth;

import ua.pkk.wetravel.R;
import ua.pkk.wetravel.databinding.FragmentMainBinding;
import ua.pkk.wetravel.utils.User;


public class MainFragment extends Fragment {
    private FragmentMainBinding binding;
    private NavController navController;
    //TODO sign up anonymously
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false);
        navController =  Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        binding.loadVideo.setOnClickListener(v -> goToLoadVideoMap());
        binding.showMyVideo.setOnClickListener(v -> goToShowVideo());
        binding.logout.setOnClickListener(v -> logOut());
        binding.showMap.setOnClickListener(v -> goToShowMap());

        //TODO DO SOMETHING WITH IT
        FirebaseAuth.getInstance().signInAnonymously();

        return binding.getRoot();
    }

    private void logOut() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("user_data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userID", "");
        editor.apply();
        navController.navigate(MainFragmentDirections.actionMainFragmentToLoginPageFragment());
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
}