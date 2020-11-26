package ua.pkk.wetravel.fragments.videoItem;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import ua.pkk.wetravel.utils.Video;

public class VideoFragmentViewModel extends ViewModel {
    private Video video;
    //TODO encapsulate it
    public MutableLiveData<Uri> videoUri = new MutableLiveData<>();
    public MutableLiveData<Boolean> successDelete = new MutableLiveData<>();
    public MutableLiveData<Bitmap> img = new MutableLiveData<>();


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

    public void renameVideo(){
        //TODO Rename
    }

    private void onDelete() {
        successDelete.setValue(true);
    }

    public void getBitmapFromURL(String src) {
        new Thread(() -> {
            try {
                URL url = new URL(src);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                img.postValue(myBitmap);
            } catch (IOException e) {
                // Log exception
            }
        }).start();
    }
}
