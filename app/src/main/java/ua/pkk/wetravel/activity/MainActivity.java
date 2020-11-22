package ua.pkk.wetravel.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.Navigation;

import ua.pkk.wetravel.R;
import ua.pkk.wetravel.fragments.loginPage.LoginPageFragmentDirections;
import ua.pkk.wetravel.fragments.main.MainFragmentDirections;
import ua.pkk.wetravel.utils.User;

public class MainActivity extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkSharedPreferences(savedInstanceState);
    }
    private void checkSharedPreferences(Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.getBoolean("isCreated")) return;
        SharedPreferences sharedPreferences = this.getSharedPreferences("user_data", Context.MODE_PRIVATE);
        String id = sharedPreferences.getString("userID", "");
        if (id.isEmpty()) {
            return;
        }
        User.getInstance().setId(id);
        Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.mainFragment);
    }
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putBoolean("isCreated", true);
        super.onSaveInstanceState(outState);
    }
}