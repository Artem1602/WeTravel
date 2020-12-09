package ua.pkk.wetravel.fragments.showVideoOnMap;

import android.util.Pair;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import ua.pkk.wetravel.utils.Video;

public class VideoMapsViewModel extends ViewModel {
    //TODO incapsulate
    public MutableLiveData<Pair<MarkerOptions, Video>> markers = new MutableLiveData<>();
    private final FirebaseStorage storage = FirebaseStorage.getInstance();

    private void markers(List<StorageReference> id) {
        for (StorageReference i : id) {
            storage.getReference(i.getName()).listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
                @Override
                public void onSuccess(ListResult listResult) {
                    for (StorageReference reference : listResult.getItems()) {
                        reference.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
                            @Override
                            public void onSuccess(StorageMetadata storageMetadata) {
                                if (reference.getName().equals("profile_img")) return;
                                String[] i = storageMetadata.getCustomMetadata("position").split("/");
                                LatLng latLng = new LatLng(Double.parseDouble(i[0]), Double.parseDouble(i[1]));
                                MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(reference.getName());
                                markers.setValue(new Pair<>(markerOptions,
                                        new Video(reference,
                                                reference.getName()
                                                ,storageMetadata.getCustomMetadata("uploadingTime")
                                                ,storageMetadata.getCustomMetadata("user_id"))));
                            }
                        });
                    }
                }
            });
        }
    }

    public void getMarkers() {
        storage.getReference().listAll().addOnSuccessListener(listResult -> markers(listResult.getPrefixes()));
    }
}
