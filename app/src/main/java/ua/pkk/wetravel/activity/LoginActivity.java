package ua.pkk.wetravel.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;

import ua.pkk.wetravel.MainActivity;
import ua.pkk.wetravel.R;
import ua.pkk.wetravel.fragments.LoginFragment;
import ua.pkk.wetravel.fragments.RegisterFragment;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button register;
    private Button login;
    private Fragment loginFragment;
    private Fragment registerFragment;
    private FragmentManager fragmentManager;
    private FragmentTransaction tr;
    private boolean check_fragment = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

       //startActivity(new Intent(getBaseContext(), MainActivity.class));

        register = findViewById(R.id.register_btn_log);
        register.setOnClickListener(this);
        login = findViewById(R.id.login_btn_log);
        login.setOnClickListener(this);

        loginFragment = new LoginFragment();
        registerFragment = new RegisterFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.frame, loginFragment).commit();
    }

    @Override
    public void onClick(View v)  {
        switch (v.getId()){
            case R.id.register_btn_log:
                if (check_fragment){
                    fragmentManager = getSupportFragmentManager();
                    tr = fragmentManager.beginTransaction();
                    tr.setCustomAnimations(R.anim.go_from_right, R.anim.go_to_left);
                    tr.replace(R.id.frame, registerFragment).commit();
                    check_fragment = false;
                }
                break;

            case R.id.login_btn_log:
                if (!check_fragment){
                    fragmentManager = getSupportFragmentManager();
                    tr = fragmentManager.beginTransaction();
                    tr.setCustomAnimations(R.anim.go_from_left, R.anim.go_to_right);
                    tr.replace(R.id.frame, loginFragment).commit();
                    check_fragment = true;
                }
                break;
        }
    }

}
