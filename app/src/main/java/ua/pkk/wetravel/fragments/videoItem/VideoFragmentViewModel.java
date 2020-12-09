package ua.pkk.wetravel.fragments.videoItem;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import ua.pkk.wetravel.utils.Video;

public class VideoFragmentViewModel extends ViewModel {
    private final Video video;

    //TODO encapsulate it
    public MutableLiveData<Boolean> successDelete = new MutableLiveData<>();
    public MutableLiveData<Bitmap> img = new MutableLiveData<>();

    private SimpleExoPlayer player;

    public VideoFragmentViewModel(Video video, SimpleExoPlayer player) {
        this.video = video;
        this.player = player;
    }

    public void getVideoUri() {
        video.getReference().getDownloadUrl().addOnSuccessListener(uri -> {
            MediaItem mediaItem = MediaItem.fromUri(uri);
            player.setMediaItem(mediaItem);
            player.prepare();
            player.play();
        });
    }

    public void deleteVideo() {
        video.getReference().delete().addOnSuccessListener(aVoid -> onDelete());
        onDelete();
    }

    public void renameVideo() {
        //TODO Rename
    }

    private void onDelete() {
        successDelete.setValue(true);
    }

    public void getBitmapFromURL(String src, File filesDir) {
        new Thread(() -> {
            try {
                URL url = new URL(src);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();

                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                input.close();

                File temp_img = new File(filesDir, "temp_img");
                FileOutputStream stream = new FileOutputStream(temp_img);
                myBitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
                stream.close();

                img.postValue(myBitmap);
            } catch (IOException e) {
                e.printStackTrace();
                // Log exception
            }
        }).start();
    }
}
