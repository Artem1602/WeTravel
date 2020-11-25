package ua.pkk.wetravel.fragments.userAccount;

import androidx.lifecycle.ViewModel;

import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import ua.pkk.wetravel.utils.User;

public class UserAccountViewModel extends ViewModel {

    public void uploadUserData(String name, String info) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        Map<String, String> user_data = new HashMap<>();
        user_data.put("user_name", name);
        user_data.put("user_info", info);
        database.getReference().child("user_data").child(User.getInstance().getId()).setValue(user_data);
    }
}