package ua.pkk.wetravel.fragments.register;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ua.pkk.wetravel.retrofit.UserData;
import ua.pkk.wetravel.retrofit.UserProperty;
import ua.pkk.wetravel.utils.CreateNewUserDataTask;
import ua.pkk.wetravel.utils.CreateNewUserTask;

public class RegisterFragmentViewModel extends ViewModel {

    private MutableLiveData<Boolean> _isSuccessRegister = new MutableLiveData<>();
    public LiveData<Boolean> isSuccessRegister = _isSuccessRegister;

    private ExecutorService executorService = Executors.newCachedThreadPool();

    public void create_account(String email, String password) {
        final String id = UUID.randomUUID().toString();
        UserProperty userProperty = new UserProperty(email, password);
        CreateNewUserTask createNewUserTask = new CreateNewUserTask(_isSuccessRegister, userProperty, id);
        UserData userData = new UserData("User", "Empty","no status");
        CreateNewUserDataTask createNewUserDataTask = new CreateNewUserDataTask(id, userData);
        executorService.execute(createNewUserTask);
        executorService.execute(createNewUserDataTask);
    }
}