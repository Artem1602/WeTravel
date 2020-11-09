package ua.pkk.wetravel.fragments.register;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class RegisterFragmentViewModel extends ViewModel {
    private MutableLiveData<Boolean> _isSuccessRegister = new MutableLiveData<>();
    public MutableLiveData<Boolean> isSuccessRegister;

    {
        _isSuccessRegister.setValue(false);
        isSuccessRegister = _isSuccessRegister;
    }

    private void successRegister() {
        _isSuccessRegister.setValue(true);
    }

    public void successRegisterComplete() {
        _isSuccessRegister.setValue(false);
    }

    public void create_account(String email, String password, String password_again) {
        Map<String, String> new_user = new HashMap<>();
        new_user.put("email", email);
        new_user.put("password", password);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String id = database.getReference("users").push().getKey();
        database.getReference().child("users").child(id).setValue(new_user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                successRegister();
            }
        });
    }
}
