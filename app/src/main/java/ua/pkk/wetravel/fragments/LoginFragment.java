package ua.pkk.wetravel.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

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
        //TODO go to reset activity
    }

    private void sign_in(final String email, final String password) {
        if (!isValidEmail(email)) {
            user_login.setError(getContext().getResources().getString(R.string.isnt_valid_mail));
            return;
        } else if (password.isEmpty()) {
            user_password.setError(getContext().getResources().getString(R.string.empty_password));
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot i : snapshot.getChildren()) {
                    Map<String, String> user;
                    user = (Map<String, String>) i.getValue();
                    if (user.get("mail").equals(email) && user.get("password").equals(password)){
                        user.put("id", i.getKey());
                        Log.d("TAG",user.toString());
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //TODO maybe Toast or something else...
            }
        });
    }

    public static boolean isValidEmail(CharSequence target) {
        return target != null && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

}
