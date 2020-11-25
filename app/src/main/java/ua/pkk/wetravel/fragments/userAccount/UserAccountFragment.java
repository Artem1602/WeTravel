package ua.pkk.wetravel.fragments.userAccount;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;

import ua.pkk.wetravel.R;
import ua.pkk.wetravel.databinding.FragmentUserAccountBinding;
import ua.pkk.wetravel.utils.User;


public class UserAccountFragment extends Fragment {
    private FragmentUserAccountBinding binding;
    private AlertDialog dialog;
    private final int IMAGE_FILE_REQUEST_CODE = 2;
    private UserAccountViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_account, container, false);
        viewModel = new ViewModelProvider(this).get(UserAccountViewModel.class);

        if (User.getInstance().getUser_img() != null)
            binding.userImg.setImageBitmap(User.getInstance().getUser_img());

        binding.aboutUser.setText(User.getInstance().getInfo());
        binding.userName.setText(User.getInstance().getName());

        binding.aboutUser.setOnLongClickListener(v -> {
            onLongAboutUserClick(v);
            return true;
        });
        binding.userName.setOnLongClickListener(v -> {
            onLongAboutUserClick(v);
            return true;
        });
        binding.userImg.setOnLongClickListener(v -> {
            addUserImg();
            return true;
        });
        binding.setUser(User.getInstance());

        load_img();

        return binding.getRoot();
    }

    private void load_img() {
        File user_img = new File(getContext().getFilesDir(), "profile_img");
        if (user_img.length() > 0)
        binding.userImg.setImageBitmap(BitmapFactory.decodeFile(user_img.getAbsolutePath()));
    }

    private void addUserImg() {
        startActivityForResult(Intent.createChooser(new Intent(Intent.ACTION_GET_CONTENT).setType("image/*"), "Choose image"), IMAGE_FILE_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode != 2 || data == null) {
            super.onActivityResult(requestCode, resultCode, data);
        }
        binding.userImg.setImageURI(data.getData());
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        storageReference.child(User.getInstance().getId()).child("profile_img").putFile(data.getData());
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void onLongAboutUserClick(View v) {
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_change_user_data, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        EditText editableData = dialogView.findViewById(R.id.editableData);
        Button cancel = dialogView.findViewById(R.id.cancel_btn);
        Button save = dialogView.findViewById(R.id.save_btn);
        editableData.setText(binding.aboutUser.getText());
        builder.setView(dialogView);
        dialog = builder.create();
        cancel.setOnClickListener(v13 -> dialog.dismiss());
        if (v.getId() == binding.aboutUser.getId()) {
            save.setOnClickListener(v1 -> {
                binding.aboutUser.setText(editableData.getText());
                dialog.dismiss();
            });
        } else {
            save.setOnClickListener(v12 -> {
                binding.userName.setText(editableData.getText());
                dialog.dismiss();
            });
        }
        dialog.show();
    }

    @Override
    public void onStop() {
        viewModel.uploadUserData(binding.userName.getText().toString(), binding.aboutUser.getText().toString());
        super.onStop();
    }
}