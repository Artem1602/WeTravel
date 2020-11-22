package ua.pkk.wetravel.fragments.showMap;

import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import ua.pkk.wetravel.utils.Video;

public class VideoMapsViewModel extends ViewModel {
    public MutableLiveData<Pair<MarkerOptions, Video>> markers = new MutableLiveData<>();


    private void markers(List<String> id) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        for (String i : id) {
            storage.getReference(i).listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
                @Override
                public void onSuccess(ListResult listResult) {
                    for (StorageReference reference : listResult.getItems()) {
                        reference.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
                            @Override
                            public void onSuccess(StorageMetadata storageMetadata) {
                                String[] i = storageMetadata.getCustomMetadata("position").split("/");
                                LatLng latLng = new LatLng(Double.parseDouble(i[0]), Double.parseDouble(i[1]));
                                MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(reference.getName());
                                markers.setValue(new Pair<>(markerOptions, new Video(reference, reference.getName())));
                            }
                        });
                    }
                }
            });
        }
    }

    public void getMarkers() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("users");
        List<String> buf = new ArrayList<>();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot i : snapshot.getChildren()) {
                    buf.add(i.getKey());
                }
                markers(buf);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //TODO maybe Toast or something else...
            }
        });
    }
}
