package ua.pkk.wetravel.fragments.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import ua.pkk.wetravel.R;
import ua.pkk.wetravel.databinding.FragmentMainBinding;
//TODO Add data into Markers https://stackoverflow.com/questions/16997130/adding-custom-property-to-marker-google-map-android-api-v2
public class MainFragment extends Fragment {
    private FragmentMainBinding binding;
    private Uri path;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false);
        binding.showMap.setOnClickListener(v -> goToLoadVideoMap());
        return binding.getRoot();
    }

    private void goToLoadVideoMap() {
        Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(MainFragmentDirections.actionMainFragmentToLoadVideomapsFragment());
    }

    //TODO sign up anonymously
}