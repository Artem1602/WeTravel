package ua.pkk.wetravel.fragments.allUserVideo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ua.pkk.wetravel.retrofit.UserAPI;
import ua.pkk.wetravel.utils.User;
import ua.pkk.wetravel.utils.Video;

public class ShowVideoFragmentViewModel extends ViewModel {
    private MutableLiveData<Video> _videos = new MutableLiveData<>();
    public LiveData<Video> videos = _videos;

    public void loadVideo() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference reference = storage.getReference(User.getInstance().getId());
        reference.listAll().addOnSuccessListener(listResult -> {
            addVideo(listResult.getItems());
        });
    }

    private void addVideo(List<StorageReference> items) {
        for (StorageReference reference : items) {
            if (reference.getName().equals("profile_img")) continue;
            reference.getMetadata().addOnSuccessListener(storageMetadata ->
                    //TODO constructors
                    reference.getDownloadUrl().addOnCompleteListener(task -> {
                        Video video = new Video(task.getResult(), reference.getName(), storageMetadata.getCustomMetadata("uploadingTime"), User.getInstance().getId());
                        video.setUri(task.getResult().toString());
                        _videos.setValue(video);
                    }));
        }
    }

    public void cleanVideo() {
        _videos.setValue(null);
    }
}
