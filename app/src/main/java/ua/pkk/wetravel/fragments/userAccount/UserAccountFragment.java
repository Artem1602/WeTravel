package ua.pkk.wetravel.fragments.userAccount;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import ua.pkk.wetravel.R;
import ua.pkk.wetravel.databinding.FragmentUserAccountBinding;
import ua.pkk.wetravel.utils.Keys;
import ua.pkk.wetravel.utils.User;


public class UserAccountFragment extends Fragment {
    private FragmentUserAccountBinding binding;
    private Handler userInfoHandler;
    private boolean is_data_ready;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_account, container, false);
        is_data_ready = false;
        userInfoHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                binding.userName.setText(User.getInstance().getName());
                binding.aboutUser.setText(User.getInstance().getInfo());
                binding.userStatusTv.setText(User.getInstance().getStatus());
                is_data_ready = true;
            }
        };
        initUI(UserAccountFragmentArgs.fromBundle(getArguments()));
        return binding.getRoot();
    }

    private void initUI(UserAccountFragmentArgs args) {
        if (args.getUserImg() != null)
            binding.userImg.setImageBitmap(BitmapFactory.decodeFile(args.getUserImg()));
        if (args.getStatus() != null) {
            is_data_ready = true;
            binding.userName.setText(args.getUserName());
            binding.aboutUser.setText(args.getUserInfo());
            binding.userStatusTv.setText(args.getStatus());
        } else {  //possibly only for owner account
            new Thread(() -> {
                //Wait for info
                while (User.getInstance().getStatus() == null) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                userInfoHandler.sendEmptyMessage(1);
            }).start();
        }
        if (args.getSourceKey() == Keys.OWNER_ACCOUNT.getValue()) {
            binding.editBtn.setOnClickListener(v -> {
                        if (is_data_ready)
                            Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(
                                    //TODO Go to right animation
                                    UserAccountFragmentDirections.actionUserAccountFragmentToEditUserAccountFragment(
                                            args.getUserImg(),
                                            User.getInstance().getName(),
                                            User.getInstance().getInfo(),
                                            User.getInstance().getStatus()
                                    )
                            );
                    }
            );
        } else {
            binding.editBtn.setVisibility(View.GONE);
        }
    }
}