package ua.pkk.wetravel.fragments.register;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.exoplayer2.C;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.xml.transform.sax.TemplatesHandler;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ua.pkk.wetravel.retrofit.UserAPI;
import ua.pkk.wetravel.retrofit.UserData;
import ua.pkk.wetravel.retrofit.UserProperty;

import static com.google.common.collect.ComparisonChain.start;

public class RegisterFragmentViewModel extends ViewModel {

    private MutableLiveData<Boolean> _isSuccessRegister = new MutableLiveData<>();
    public LiveData<Boolean> isSuccessRegister = _isSuccessRegister;

    public void create_account(String email, String password) {
        final String id = UUID.randomUUID().toString();

        new Thread(new Runnable() {
            @Override
            public void run() {
                HashMap map = new HashMap<String, UserProperty>();
                map.put(id,new UserProperty(email, password));
                UserAPI.INSTANCE.getRETROFIT_SERVICE().createNewUser(id,new UserProperty(email, password)).enqueue(new Callback<UserProperty>() {
                    @Override
                    public void onResponse(Call<UserProperty> call, Response<UserProperty> response) {
                        _isSuccessRegister.setValue(response.isSuccessful());
                    }

                    @Override
                    public void onFailure(Call<UserProperty> call, Throwable t) {
                        _isSuccessRegister.setValue(false);
                    }
                });
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                UserAPI.INSTANCE.getRETROFIT_SERVICE().createNewUserData(id,new UserData("User", "Empty","no status")).enqueue(new Callback<UserData>() {
                    @Override
                    public void onResponse(Call<UserData> call, Response<UserData> response) {
                    }

                    @Override
                    public void onFailure(Call<UserData> call, Throwable t) {
                        int i = 55;
                    }
                });
            }
        }).start();
    }
}