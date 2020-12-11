package ua.pkk.wetravel.fragments.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import java.io.File;

import ua.pkk.wetravel.R;
import ua.pkk.wetravel.databinding.FragmentSettingsBinding;
import ua.pkk.wetravel.utils.User;

public class SettingsFragment extends Fragment {
    private FragmentSettingsBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_settings,container,false);
        binding.logout.setOnClickListener(v -> onLogOut());
        return binding.getRoot();
    }

    private void onLogOut(){
          SharedPreferences sharedPreferences = getContext().getSharedPreferences("user_data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userID", "");
        editor.apply();
        File user_img = new File(getContext().getFilesDir(), "profile_img");
        user_img.delete();
        User.getInstance().cleanData();
        Navigation.findNavController(getActivity(),R.id.nav_host_fragment).navigate(SettingsFragmentDirections.actionSettingsFragmentToLoginPageFragment());
    }
}
