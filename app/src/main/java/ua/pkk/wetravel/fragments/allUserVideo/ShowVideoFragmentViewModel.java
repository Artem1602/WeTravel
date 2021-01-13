package ua.pkk.wetravel.fragments.allUserVideo;

import android.net.Uri;
import android.util.Log;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

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
        for (StorageReference reference : items){
            if (reference.getName().equals("profile_img")) continue;
            reference.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
                @Override
                public void onSuccess(StorageMetadata storageMetadata) {
                    reference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            Video video = new Video(task.getResult(),reference.getName(),storageMetadata.getCustomMetadata("uploadingTime"),User.getInstance().getId());
                            video.setUri(task.getResult().toString());
                            Log.d("TAG",video.getName());
                            _videos.setValue(video);
                        }
                    });

                }
            });
        }
    }

    public void cleanVideo(){
        _videos.setValue(null);
    }

}
