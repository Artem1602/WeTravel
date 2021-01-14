package ua.pkk.wetravel.fragments.userAccount;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import ua.pkk.wetravel.R;
import ua.pkk.wetravel.databinding.FragmentUserAccountBinding;
import ua.pkk.wetravel.utils.Keys;


public class UserAccountFragment extends Fragment {
    private FragmentUserAccountBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_account, container, false);
        initUI(UserAccountFragmentArgs.fromBundle(getArguments()));
        return binding.getRoot();
    }

    //TODO Use Handler to wait name and info or use if(no info) -> ask to server Priority -> CRITICAL (partially fix it)
    private void initUI(UserAccountFragmentArgs args) {
        if (args.getUserImg() != null)
            binding.userImg.setImageBitmap(BitmapFactory.decodeFile(args.getUserImg()));
        binding.userName.setText(args.getUserName());
        binding.aboutUser.setText(args.getUserInfo());
        binding.userStatusTv.setText(args.getStatus());

        if (args.getSourceKey() == Keys.OWNER_ACCOUNT.getValue()) {
            binding.editBtn.setOnClickListener(v ->
                    Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(UserAccountFragmentDirections
                    .actionUserAccountFragmentToEditUserAccountFragment(args.getUserImg(), args.getUserName(), args.getUserInfo(), args.getStatus()))
            );

        } else {
            binding.editBtn.setVisibility(View.GONE);
            if (args.getUserImg() != null)
                binding.userImg.setImageBitmap(BitmapFactory.decodeFile(args.getUserImg()));
        }
    }
}