package ua.pkk.wetravel.utils;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import com.google.firebase.storage.StorageReference;

import java.util.Objects;

public class Video implements Parcelable {
    private StorageReference reference;
    private String name;

    public Video(StorageReference reference, String name) {
        this.reference = reference;
        this.name = name;
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

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        return Objects.equals(name,((Video) obj).name) && Objects.equals(reference,((Video) obj).reference);  //TODO more fields
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
    }
}
