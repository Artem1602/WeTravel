package ua.pkk.wetravel.fragments.allUserVideo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import ua.pkk.wetravel.utils.User;
import ua.pkk.wetravel.utils.Video;

public class ShowVideoFragmentViewModel extends ViewModel {
    //TODO read about incapsulation in LiveDATA

    public LiveData<Boolean> is_loaded;
    private MutableLiveData<Boolean> _is_loaded = new MutableLiveData<>();
    public MutableLiveData<List<Video>> videos = new MutableLiveData<>();

    {
        _is_loaded.setValue(false);
        is_loaded = _is_loaded;
    }

    public void loadVideo() {
        String id = User.getInstance().getId();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference reference = storage.getReference(id);
        reference.listAll().addOnSuccessListener(listResult -> {
           addVideo(listResult.getItems());
           onLoad();
        });
    }

    private void addVideo(List<StorageReference> items) {
        ArrayList<Video> video = new ArrayList<>();
        for (StorageReference reference : items){
            reference.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
                @Override
                public void onSuccess(StorageMetadata storageMetadata) {
                    video.add(new Video(reference,reference.getName(),storageMetadata.getCustomMetadata("uploadingTime"),User.getInstance().getId()));
                    videos.setValue(video);
                }
            });
        }
    }

    public void onLoad() {
        _is_loaded.setValue(true);
    }
}
