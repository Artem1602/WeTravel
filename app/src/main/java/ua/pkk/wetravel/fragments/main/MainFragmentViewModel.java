package ua.pkk.wetravel.fragments.main;

import androidx.lifecycle.ViewModel;

import com.google.firebase.storage.FirebaseStorage;

import java.io.File;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ua.pkk.wetravel.retrofit.UserAPI;
import ua.pkk.wetravel.retrofit.UserData;
import ua.pkk.wetravel.utils.User;

public class MainFragmentViewModel extends ViewModel {
    public void load_user_info(File fileDir) {

        new Thread(
                () -> UserAPI.INSTANCE.getRETROFIT_SERVICE().getUserData(User.getInstance().getId()).enqueue(new Callback<UserData>() {
            @Override
            public void onResponse(Call<UserData> call, Response<UserData> response) {
                if (response.isSuccessful()) {
                    User.getInstance().setName(response.body().getUserName());
                    User.getInstance().setInfo(response.body().getUserInfo());
                }
            }

            @Override
            public void onFailure(Call<UserData> call, Throwable t) {
                //TODO
            }
        })).start();

        File user_img = new File(fileDir, "profile_img");
        if (user_img.length() == 0)
            FirebaseStorage.getInstance().getReference().child(User.getInstance().getId()).child("profile_img").getFile(user_img);
    }
}
