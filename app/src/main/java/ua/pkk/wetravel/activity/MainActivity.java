package ua.pkk.wetravel.activity;

import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import ua.pkk.wetravel.R;

public class MainActivity extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

}