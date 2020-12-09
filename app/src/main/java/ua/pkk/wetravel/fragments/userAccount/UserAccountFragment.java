package ua.pkk.wetravel.fragments.userAccount;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
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

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import ua.pkk.wetravel.R;
import ua.pkk.wetravel.databinding.FragmentUserAccountBinding;
import ua.pkk.wetravel.utils.Keys;
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

        initUI(UserAccountFragmentArgs.fromBundle(getArguments()));

        return binding.getRoot();
    }

    private void initUI(UserAccountFragmentArgs args) {
        if (args.getUserImg() != null)
            binding.userImg.setImageBitmap(BitmapFactory.decodeFile(args.getUserImg()));
        binding.userName.setText(args.getUserName());
        binding.aboutUser.setText(args.getUserInfo());

        if (args.getSourceKey() == Keys.OWNER_ACCOUNT.getValue()) {
            binding.aboutUser.setOnLongClickListener(v -> {
                viewModel.onDataChange();
                onLongAboutUserClick(v);
                return true;
            });
            binding.userName.setOnLongClickListener(v -> {
                viewModel.onDataChange();
                onLongAboutUserClick(v);
                return true;
            });
            binding.userImg.setOnLongClickListener(v -> {
                viewModel.onDataChange();
                startActivityForResult(Intent.createChooser(new Intent(Intent.ACTION_GET_CONTENT).setType("image/*"), "Choose image"), IMAGE_FILE_REQUEST_CODE);
                return true;
            });
        } else {

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode != 2 || data == null) {
            super.onActivityResult(requestCode, resultCode, data);
        }
        binding.userImg.setImageURI(data.getData());
        StorageReference reference = FirebaseStorage.getInstance().getReference().child(User.getInstance().getId()).child("profile_img");

        new Thread(() -> {
            try (ByteArrayOutputStream stream = new ByteArrayOutputStream()) {
                Bitmap bmp = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), data.getData());
                bmp.compress(Bitmap.CompressFormat.JPEG, 25, stream);
                byte[] output = stream.toByteArray();

                reference.putBytes(output); //TODO addOnSuccessListener
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
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
        if (viewModel.isDataChange.getValue())
            viewModel.uploadUserData(binding.userName.getText().toString(), binding.aboutUser.getText().toString());
        super.onStop();
    }
}