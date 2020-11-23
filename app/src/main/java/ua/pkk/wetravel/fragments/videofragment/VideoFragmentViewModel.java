package ua.pkk.wetravel.fragments.videofragment;


import android.net.Uri;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnSuccessListener;

import ua.pkk.wetravel.utils.Video;

public class VideoFragmentViewModel extends ViewModel {
    private Video video;
    //TODO encapsulate it
    public MutableLiveData<Uri> videoUri = new MutableLiveData<>();
    public MutableLiveData<Boolean> successDelete = new MutableLiveData<>();


    {
        successDelete.setValue(false);
        videoUri.setValue(null);

    }

    public VideoFragmentViewModel(Video video) {
        this.video = video;
    }

    public void getVideoUri() {
        video.getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                videoUri.setValue(uri);
            }
        });
    }

    public void deleteVideo() {
        video.getReference().delete().addOnSuccessListener(aVoid -> onDelete());
        onDelete();
    }

    private void onDelete() {
        successDelete.setValue(true);
    }
}
