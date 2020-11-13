package ua.pkk.wetravel.fragments.map;

import android.content.Intent;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class LoadVideoMapsFragmentViewModel extends ViewModel {
    public MutableLiveData<Double> progress = new MutableLiveData<>();

    {
        progress.setValue((double) 0);
    }

    public void uploadSelectedVideo(Intent data, LatLng position) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("video/" + data.getData().getLastPathSegment());
        StorageMetadata.Builder metadata = new StorageMetadata.Builder().setCustomMetadata("position", position.latitude + "/" + position.longitude);
        UploadTask uploadTask = storageReference.putFile(data.getData(),metadata.build());
        uploadTask.addOnProgressListener(snapshot -> progress.setValue((100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount())).addOnCompleteListener(task -> onSuccessDownload());
    }

    private void onSuccessDownload() {
        progress.setValue((double) 0);
    }
}