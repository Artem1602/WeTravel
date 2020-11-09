package ua.pkk.wetravel.fragments.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import ua.pkk.wetravel.R;
import ua.pkk.wetravel.databinding.FragmentMainBinding;

public class MainFragment extends Fragment {
    private FragmentMainBinding binding;

    //TODO Put setOnClickListener into view model
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false);
        binding.showMap.setOnClickListener(v -> goToMap());
        return binding.getRoot();
    }

    private void goToMap() {
        Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(MainFragmentDirections.actionMainFragmentToMapsFragment());
    }

}