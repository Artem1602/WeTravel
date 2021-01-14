package ua.pkk.wetravel.fragments.editUserAccount;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.FileUtils;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

import ua.pkk.wetravel.R;
import ua.pkk.wetravel.databinding.FragmentEditUserAccountBinding;
import ua.pkk.wetravel.utils.Keys;
import ua.pkk.wetravel.utils.User;

public class EditUserAccountFragment extends Fragment {
    private FragmentEditUserAccountBinding binding;
    private EditUserAccountFragmentViewModel viewModel;
    private final int IMAGE_FILE_REQUEST_CODE = 2;
    private String imgUri;
    private EditUserAccountFragmentArgs args;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_user_account, container, false);
        viewModel = new ViewModelProvider(this).get(EditUserAccountFragmentViewModel.class);

        args = EditUserAccountFragmentArgs.fromBundle(getArguments());
        initUI(args);

        binding.saveBtn.setOnClickListener(this::onSave);
        binding.cancelBtn.setOnClickListener(this::onCancel);
        binding.changePhotoBtn.setOnClickListener(this::onImgChange);
        return binding.getRoot();
    }

    private void initUI(EditUserAccountFragmentArgs args) {
        if (args.getUserImg() != null){
            binding.userImg.setImageBitmap(BitmapFactory.decodeFile(args.getUserImg()));
            imgUri = args.getUserImg();
        } else {imgUri = null;}

        binding.editedUserAbout.setText(args.getUserInfo());
        binding.editedUserName.setText(args.getUserName());
        binding.editedUserStatus.setText(args.getStatus());

    }

    public void onImgChange(View v){
        startActivityForResult(Intent.createChooser(new Intent(Intent.ACTION_GET_CONTENT).setType("image/*"), "Choose image"), IMAGE_FILE_REQUEST_CODE);
    }

    public void onSave(View v) {
        String name = binding.editedUserName.getText().toString();
        String about_me = binding.editedUserAbout.getText().toString();
        String status = binding.editedUserStatus.getText().toString();

        viewModel.uploadUserData(name, about_me, status);
        Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(EditUserAccountFragmentDirections.actionEditUserAccountFragmentToUserAccountFragment(imgUri,name,about_me,status, Keys.OWNER_ACCOUNT.getValue()));
    }

    public void onCancel(View v) {
        Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(EditUserAccountFragmentDirections.actionEditUserAccountFragmentToUserAccountFragment(imgUri,args.getUserName(),args.getUserInfo(),args.getStatus(), Keys.OWNER_ACCOUNT.getValue()));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode != 2 || data == null) {
            super.onActivityResult(requestCode, resultCode, data);
        }
        binding.userImg.setImageURI(data.getData());
        File user_img = new File(getContext().getFilesDir(), "profile_img");
        imgUri = user_img.getAbsolutePath();
        StorageReference reference = FirebaseStorage.getInstance().getReference().child(User.getInstance().getId()).child("profile_img");

        new Thread(() -> {
            try (ByteArrayOutputStream stream = new ByteArrayOutputStream()) {
                Bitmap bmp = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), data.getData());
                bmp.compress(Bitmap.CompressFormat.JPEG, 25, stream);

                try(OutputStream outputStream = new FileOutputStream(user_img.getAbsolutePath())) {
                    stream.writeTo(outputStream);
                }

                byte[] output = stream.toByteArray();
                reference.putBytes(output); //TODO addOnSuccessListener
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
        super.onActivityResult(requestCode, resultCode, data);
    }
}
