package ua.pkk.wetravel.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;

import ua.pkk.wetravel.R;

public class RegisterFragment extends Fragment implements View.OnClickListener {

    private Button accept_btn;

    private TextInputEditText new_user_login;
    private TextInputEditText new_user_password;
    private TextInputEditText new_user_password_again;

    private TextView restore_password;

    private View view;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_registration, container, false);
        accept_btn = view.findViewById(R.id.accept_btn_reg);

        accept_btn.setOnClickListener(this);

        new_user_login = view.findViewById(R.id.mail_ed_reg);
        new_user_password = view.findViewById(R.id.passw_ed_reg);
        new_user_password_again = view.findViewById(R.id.passw_again_ed_reg);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.accept_btn_log:
                create_account(new_user_login.getText().toString(),new_user_password.getText().toString(),new_user_password_again.getText().toString());
                break;
        }
    }


    private void create_account(String email, String password, String password_again) {

    }

}
