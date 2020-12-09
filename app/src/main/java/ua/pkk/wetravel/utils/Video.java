package ua.pkk.wetravel.utils;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Objects;

public class Video implements Parcelable {
    private StorageReference reference;
    private Bitmap thumbnail;
    private String name;
    private String uploadingTime;
    private String upload_user_id;

    public Video(StorageReference reference, String name, String uploadingTime, String upload_user_id) {
        this.reference = reference;
        this.name = name;
        this.uploadingTime = uploadingTime;
        this.upload_user_id = upload_user_id;
        load_thumbnail();
    }

    protected Video(Parcel in) {
        name = in.readString();
    }

    public StorageReference getReference() {
        return reference;
    }

    public String getName() {
        return name;
    }

    public String getUploadingTime() {
        return uploadingTime;
    }

    public String getUpload_user_id() {
        return upload_user_id;
    }

    public Bitmap getThumbnail() {
        return thumbnail;
    }

    private void load_thumbnail() {
        new Thread(() -> reference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                try {
                    thumbnail = retrieveThumbnail(task.getResult().toString());
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        })).start();
    }

    private Bitmap retrieveThumbnail(String videoPath) throws Throwable {
        Bitmap bitmap;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            mediaMetadataRetriever.setDataSource(videoPath, new HashMap<>());
            bitmap = mediaMetadataRetriever.getFrameAtTime(1, MediaMetadataRetriever.OPTION_CLOSEST);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Throwable("Exception in retrieveVideoFrame()" + e.getMessage());
        } finally {
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release();
            }
        }
        return bitmap;
    }


    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Video buf = (Video) obj;
        return Objects.equals(name, buf.name) && Objects.equals(reference, buf.reference) && Objects.equals(uploadingTime, buf.uploadingTime);  //TODO more fields
    }


    public static final DiffUtil.ItemCallback<Video> DIFF_CALLBACK = new DiffUtil.ItemCallback<Video>() {
        @Override
        public boolean areItemsTheSame(@NonNull Video oldItem, @NonNull Video newItem) {
            return oldItem.getName().equals(newItem.getName());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Video oldItem, @NonNull Video newItem) {
            return oldItem.equals(newItem);
        }
    };

    public static final Creator<Video> CREATOR = new Creator<Video>() {
        @Override
        public Video createFromParcel(Parcel in) {
            return new Video(in);
        }

        @Override
        public Video[] newArray(int size) {
            return new Video[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeValue(reference);
        dest.writeValue(thumbnail);
        dest.writeString(upload_user_id);
        dest.writeString(uploadingTime);
    }
}
