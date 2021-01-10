package ua.pkk.wetravel.fragments.userAccount;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ua.pkk.wetravel.retrofit.UserAPI;
import ua.pkk.wetravel.retrofit.UserData;
import ua.pkk.wetravel.utils.User;

public class UserAccountViewModel extends ViewModel {
    private MutableLiveData<Boolean> _isDataChange = new MutableLiveData<>(false);
    public LiveData<Boolean> isDataChange = _isDataChange;

    public void uploadUserData(String name, String info) {
        new Thread(() -> {
            HashMap<String, UserData> map = new HashMap<>();
            map.put(User.getInstance().getId(),new UserData(name,info));

            UserAPI.INSTANCE.getRETROFIT_SERVICE().createNewUserData(map).enqueue(new Callback<Map<String, UserData>>() {
                @Override
                public void onResponse(Call<Map<String, UserData>> call, Response<Map<String, UserData>> response) {
                    Log.d("TAG",response.body().toString());
                }

                @Override
                public void onFailure(Call<Map<String, UserData>> call, Throwable t) {
                    Log.d("TAG",t.getMessage());
                }
            });
        }).start();

    }

    public void onDataChange(){
        _isDataChange.setValue(true);
    }
}
