package ua.pkk.wetravel.fragments.main;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.io.File;
import java.util.HashMap;

import ua.pkk.wetravel.utils.User;

public class MainFragmentViewModel extends ViewModel {
    public void load_user_info(File fileDir) {
        FirebaseDatabase.getInstance().getReference().child("user_data").child(User.getInstance().getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HashMap<String, String> user = (HashMap<String, String>) snapshot.getValue();
                if (user == null) return;
                User.getInstance().setName(user.get("user_name"));
                User.getInstance().setInfo(user.get("user_info"));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        File user_img = new File(fileDir, "profile_img");
        if (user_img.length() == 0)
            FirebaseStorage.getInstance().getReference().child(User.getInstance().getId()).child("profile_img").getFile(user_img);
    }
}
