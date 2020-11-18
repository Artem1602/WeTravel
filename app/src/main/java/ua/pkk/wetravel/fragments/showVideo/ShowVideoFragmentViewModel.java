package ua.pkk.wetravel.fragments.showVideo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
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


    private MutableLiveData<ArrayList<Video>> videoData = new MutableLiveData<>();

    {
        _is_loaded.setValue(false);
        is_loaded = _is_loaded;
    }

    public void loadVideo() {
        String id = User.getInstance().getId();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference reference = storage.getReference(id);
        reference.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
               addVideo(listResult.getItems());
               int i = 55;
               onLoad();
            }
        });

    }

    private void addVideo(List<StorageReference> items) {
        ArrayList<Video> video = new ArrayList<>();
        for (StorageReference reference : items){
            video.add(new Video(reference,reference.getName()));
        }
        videos.setValue(video);
    }

    public void onLoad() {
        _is_loaded.setValue(true);
    }


}
