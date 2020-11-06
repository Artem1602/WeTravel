package ua.pkk.wetravel.fragments.login;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class LoginFragmentViewModel extends ViewModel {
    private MutableLiveData<Boolean> _eventIsLogin = new MutableLiveData<>();
    public MutableLiveData<Boolean> eventIsLogin;

    {
        _eventIsLogin.setValue(false);
        eventIsLogin = _eventIsLogin;
        Log.d("TAG", "LoginFragmentViewModel CREATED");
    }

    private void onLogin() {
        _eventIsLogin.setValue(true);
    }

    public void sign_in(final String email, final String password) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot i : snapshot.getChildren()) {
                    Map<String, String> user = (Map<String, String>) i.getValue();
                    if(user.get("email") == null){
                        //TODO Wrong data
                        break;
                    }
                    if (user.get("email").equals(email) && user.get("password").equals(password)) {
                        user.put("id", i.getKey());
                        onLogin();
                        Log.d("TAG", user.toString());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //TODO maybe Toast or something else...
            }
        });
    }

    public boolean isValidEmail(CharSequence target) {
        return target != null && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

}
