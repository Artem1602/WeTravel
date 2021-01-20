package ua.pkk.wetravel.fragments.loadVideoMap;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import ua.pkk.wetravel.utils.User;

public class LoadVideoMapViewModel extends ViewModel {
    private MutableLiveData<Boolean> _hasSameNames = new MutableLiveData<>();
    public LiveData<Boolean> hasSameNames = _hasSameNames;

    public boolean is_add_video_show;

    {is_add_video_show = false;}

    public void checkNames(String video_name) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        storage.getReference().child(User.getInstance().getId()).listAll().addOnCompleteListener(new OnCompleteListener<ListResult>() {
            @Override
            public void onComplete(@NonNull Task<ListResult> task) {
                for (StorageReference i : task.getResult().getItems()) {
                    if (i.getName().equals(video_name)) {
                        _hasSameNames.setValue(true);
                        return;
                    }
                }
                _hasSameNames.setValue(false);
            }
        });

    }
}
