package ua.pkk.wetravel.utils;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import com.google.firebase.storage.StorageReference;

import java.math.BigDecimal;
import java.util.Objects;

public class Video implements Parcelable {
    private Uri reference;
    private String name;
    private String uploadingTime;
    private String upload_user_id;
    private String uri;
    private Drawable thumbNail;

    public Video(Uri reference, String name, String uploadingTime, String upload_user_id) {
        this.reference = reference;
        this.name = name;
        this.uploadingTime = uploadingTime;
        this.upload_user_id = upload_user_id;
    }

    protected Video(Parcel in) {
        name = in.readString();
    }

    public Uri getReference() {
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

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Drawable getThumbNail() {
        return thumbNail;
    }

    public void setThumbNail(Drawable thumbNail) {
        this.thumbNail = thumbNail;
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
        return Objects.equals(name, buf.name)
                && Objects.equals(reference, buf.reference)
                && Objects.equals(uploadingTime, buf.uploadingTime)
                && Objects.equals(upload_user_id,buf.upload_user_id)
                && Objects.equals(uri,buf.uri);  //TODO more fields
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
        dest.writeString(upload_user_id);
        dest.writeString(uploadingTime);
    }
}
