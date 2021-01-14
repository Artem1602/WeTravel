package ua.pkk.wetravel.fragments.editUserAccount;

import androidx.lifecycle.ViewModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ua.pkk.wetravel.retrofit.UserAPI;
import ua.pkk.wetravel.retrofit.UserData;
import ua.pkk.wetravel.utils.User;

public class EditUserAccountFragmentViewModel extends ViewModel {
    public void uploadUserData(String name, String info, String status) {
        new Thread(() -> {
            UserAPI.INSTANCE.getRETROFIT_SERVICE().createNewUserData(User.getInstance().getId(), new UserData(name, info, status)).enqueue(new Callback<UserData>() {
                @Override
                public void onResponse(Call<UserData> call, Response<UserData> response) {
                    //TODO
                }

                @Override
                public void onFailure(Call<UserData> call, Throwable t) {

                }
            });
        }).start();
    }
}
