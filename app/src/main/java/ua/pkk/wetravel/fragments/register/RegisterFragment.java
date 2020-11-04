package ua.pkk.wetravel.fragments.register;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import ua.pkk.wetravel.R;
import ua.pkk.wetravel.databinding.FragmentRegistrationBinding;

public class RegisterFragment extends Fragment {
    private FragmentRegistrationBinding binding;
    private RegisterFragmentViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_registration, container, false);
        viewModel = new ViewModelProvider(this).get(RegisterFragmentViewModel.class);
        viewModel.context = getContext();
        binding.acceptBtnReg.setOnClickListener(v -> onAccept());
        return binding.getRoot();
    }

    private void onAccept() {
        if (!binding.passwEdReg.getText().toString().equals(binding.passwAgainEdReg.getText().toString())) {
            binding.passwEdReg.setError(getContext().getResources().getString(R.string.password_dont_match));
            binding.passwAgainEdReg.setError(getContext().getResources().getString(R.string.password_dont_match));
            return;
        }
        viewModel.create_account(binding.mailEdReg.getText().toString(),
                binding.passwEdReg.getText().toString(),
                binding.passwAgainEdReg.getText().toString());
    }

}
