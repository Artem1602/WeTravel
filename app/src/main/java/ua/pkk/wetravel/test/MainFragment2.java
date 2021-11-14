package ua.pkk.wetravel.test;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.navigation.NavigationBarView;

import java.io.File;

import ua.pkk.wetravel.R;
import ua.pkk.wetravel.databinding.FragmentMain2Binding;
import ua.pkk.wetravel.fragments.main.MainFragmentDirections;
import ua.pkk.wetravel.fragments.userAccount.UserAccountFragment;
import ua.pkk.wetravel.fragments.userAccount.UserAccountFragmentArgs;
import ua.pkk.wetravel.utils.Keys;
import ua.pkk.wetravel.utils.User;

public class MainFragment2 extends Fragment {
    private FragmentMain2Binding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main2, container, false);
        binding.bottomNavigationView.setOnItemReselectedListener(navListener());

        initNav();

        return binding.getRoot();
    }

    private void initNav() {
        String userName = User.getInstance().getName();
        String userInfo = User.getInstance().getInfo();
        String userStatus = User.getInstance().getStatus();
        File user_img = new File(getContext().getFilesDir(), "profile_img");
        String path = null;
        if (user_img.length() != 0) {
            path = user_img.getAbsolutePath();
        }
        getParentFragmentManager().beginTransaction()
                .replace(binding.mainFragmentsContainer.getId(), UserAccountFragment.newInstance(userName, userInfo, path ,userStatus,Keys.OWNER_ACCOUNT.getValue())).commit();
    }

    private NavigationBarView.OnItemReselectedListener navListener() {
        switch (binding.bottomNavigationView.getSelectedItemId()) {

        }
        return null;
    }


}