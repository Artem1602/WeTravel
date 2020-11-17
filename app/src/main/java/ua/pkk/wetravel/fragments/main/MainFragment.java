package ua.pkk.wetravel.fragments.main;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import ua.pkk.wetravel.R;
import ua.pkk.wetravel.databinding.FragmentMainBinding;
import ua.pkk.wetravel.utils.User;

//TODO Add data into Markers https://stackoverflow.com/questions/16997130/adding-custom-property-to-marker-google-map-android-api-v2
public class MainFragment extends Fragment {
    private FragmentMainBinding binding;

    //TODO sign up anonymously
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false);
        binding.showMap.setOnClickListener(v -> goToLoadVideoMap());
        binding.showMyVideo.setOnClickListener(v -> goToShowVideo());
        binding.logout.setOnClickListener(v -> logOut());
        return binding.getRoot();
    }

    private void logOut() {
        //TODO logOut
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("user_data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userID", "");
        editor.apply();
    }

    private void goToLoadVideoMap() {
        Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(MainFragmentDirections.actionMainFragmentToLoadVideomapsFragment());
    }

    private void goToShowVideo() {
        Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(MainFragmentDirections.actionMainFragmentToShowVideoFragment());
    }
}