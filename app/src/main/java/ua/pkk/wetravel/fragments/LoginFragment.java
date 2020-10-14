package ua.pkk.wetravel.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;

import ua.pkk.wetravel.R;

public class LoginFragment extends Fragment implements View.OnClickListener {

    private Button accept_btn;
    private TextInputEditText user_login;
    private TextInputEditText user_password;

    private TextView restore_password;

    private View view;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login, container, false);
        accept_btn = view.findViewById(R.id.accept_btn_log);
        accept_btn.setOnClickListener(this);
        user_login = view.findViewById(R.id.mail_ed_log);
        user_password = view.findViewById(R.id.passw_ed_log);
        restore_password = view.findViewById(R.id.restore_password_log);
        restore_password.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.accept_btn_log:
                sign_in(user_login.getText().toString(), user_password.getText().toString());
                break;
            case R.id.restore_password_log:
                reset_password();
                break;

        }
    }

    private void reset_password() {
    Toast.makeText(getActivity(),"hello",Toast.LENGTH_LONG).show();
    }

    private void sign_in(String email, String password) {
        Toast.makeText(getActivity(),"hello",Toast.LENGTH_LONG).show();
    }

}
