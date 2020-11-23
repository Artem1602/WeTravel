package ua.pkk.wetravel.fragments.userAccount;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import ua.pkk.wetravel.R;
import ua.pkk.wetravel.databinding.FragmentUserAccountBinding;
import ua.pkk.wetravel.utils.User;

public class UserAccountFragment extends Fragment {
    private FragmentUserAccountBinding binding;
    private AlertDialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_user_account ,container, false);
        binding.aboutUser.setOnLongClickListener(v -> {
            onLongAboutUserClick(v);
            return true;
        });


        binding.setUser(User.getInstance());

        return binding.getRoot();
    }

    public void onLongAboutUserClick(View v){
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_change_user_data,null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        EditText editableData = dialogView.findViewById(R.id.editableData);
        Button cancel =  dialogView.findViewById(R.id.cancel_btn);
        Button save =  dialogView.findViewById(R.id.save_btn);
        editableData.setText(binding.aboutUser.getText());
        builder.setView(dialogView);
        dialog = builder.create();
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO Add info to DataBase
                binding.aboutUser.setText(editableData.getText());
                dialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
        dialog.show();
    }

}

//    private void onAddVideo(View view){
//        if(marker == null){
//            Toast.makeText(getContext(),getContext().getText(R.string.addMarker),Toast.LENGTH_LONG).show();
//            return;
//        }
//        LayoutInflater inflater = getLayoutInflater();
//        builderView = inflater.inflate(R.layout.dialog_custom_add_video,null);
//        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());
//        Button selectVideoButton = builderView.findViewById(R.id.selectVideo_btn);
//        selectVideoButton.setOnClickListener(v -> {
//            startActivityForResult(Intent.createChooser(new Intent(Intent.ACTION_GET_CONTENT).setType("video/*"), "Choose Video"), VIDEO_FILE_REQUEST_CODE);
//        });
//        loadVideoName = builderView.findViewById(R.id.videoName);
//        builder.setView(builderView);
//        builder.create();
//        builder.show();
//    }