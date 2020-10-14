package ua.pkk.wetravel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private TextInputEditText mail;
    private TextInputEditText password;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        init(mAuth.getCurrentUser());
        mail = findViewById(R.id.mail_ed);
        password = findViewById(R.id.passw_ed);
    }

    private void init(FirebaseUser currentUser) {
        if (currentUser == null) return;
        startActivity(new Intent(getBaseContext(),MainActivity.class));
    }


    public void onLoginBtnClick(View view) {
        if (!isValidEmail(mail.getText().toString())){
            mail.setError(getApplicationContext().getResources().getString(R.string.isnt_valid_mail));
            return;
        }
        if (password.getText().toString().isEmpty()) {
            password.setError(getApplicationContext().getResources().getString(R.string.empty_password));
        }
        mAuth.signInWithEmailAndPassword(mail.getText().toString(), password.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            startActivity(new Intent(getBaseContext(),MainActivity.class));
                        } else {
                            //TODO Show errorq

                        }
                    }
                });
    }


    public void onRegisterBtnClick(View view) {
        //TODO go to register activity
    }

    public static boolean isValidEmail(CharSequence target) {
        return target != null && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
}
