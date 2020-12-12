package ua.pkk.wetravel.fragments.videoItem;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.lifecycle.LiveData;
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

    private MutableLiveData<Boolean> _successDelete = new MutableLiveData<>();
    public LiveData<Boolean> successDelete = _successDelete;

    private MutableLiveData<Bitmap> _img = new MutableLiveData<>();
    public LiveData<Bitmap> img = _img;

    private SimpleExoPlayer player;

    public long previousDuration;

    public VideoFragmentViewModel(Video video, SimpleExoPlayer player) {
        this.video = video;
        this.player = player;
    }

    public void playVideoFromUri() {
        video.getReference().getDownloadUrl().addOnSuccessListener(uri -> {
            MediaItem mediaItem = MediaItem.fromUri(uri);
            player.setMediaItem(mediaItem);
            player.prepare();
            player.play();
        });
    }

    public void reCreatePlayerAndPlay(SimpleExoPlayer player) {
        this.player = player;
        video.getReference().getDownloadUrl().addOnSuccessListener(uri -> {
            MediaItem mediaItem = MediaItem.fromUri(uri);
            player.setMediaItem(mediaItem);
            player.seekTo(previousDuration);
            player.prepare();
            player.setPlayWhenReady(true);
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
        _successDelete.setValue(true);
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
                myBitmap.compress(Bitmap.CompressFormat.PNG, 40, stream);
                stream.close();

                _img.postValue(myBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
