package ua.pkk.wetravel.fragments.main;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;

import java.io.File;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ua.pkk.wetravel.retrofit.UserAPI;
import ua.pkk.wetravel.retrofit.UserData;
import ua.pkk.wetravel.utils.User;

public class MainFragmentViewModel extends ViewModel {

    public void load_user_img(File fileDir) {
        //TODO Error handling
        new Thread(() -> {
            FirebaseStorage.getInstance().getReference().child(User.getInstance().getId()).child("profile_img").getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        User.getInstance().setImgUri(task.getResult().toString());
                    }
                }
            });

            UserAPI.INSTANCE.getRETROFIT_SERVICE().getUserData(User.getInstance().getId()).enqueue(new Callback<UserData>() {
                @Override
                public void onResponse(Call<UserData> call, Response<UserData> response) {
                    User.getInstance().setName(response.body().getUserName());
                    User.getInstance().setInfo(response.body().getUserInfo());
                    User.getInstance().setStatus(response.body().getStatus());
                    Log.d("TAG",response.body().getStatus());
                }

                @Override
                public void onFailure(Call<UserData> call, Throwable t) {

                }
            });
        }).start();

        File user_img = new File(fileDir, "profile_img");
        if (user_img.length() == 0)
            FirebaseStorage.getInstance().getReference().child(User.getInstance().getId()).child("profile_img").getFile(user_img);
    }
}
