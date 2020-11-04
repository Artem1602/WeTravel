package ua.pkk.wetravel.fragments.register;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import ua.pkk.wetravel.R;
import ua.pkk.wetravel.activity.MainActivity;
import ua.pkk.wetravel.databinding.FragmentRegistrationBinding;

public class RegisterFragment extends Fragment {
    private FragmentRegistrationBinding binding;
    private RegisterFragmentViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_registration, container, false);
        viewModel = new ViewModelProvider(this).get(RegisterFragmentViewModel.class);
        binding.acceptBtnReg.setOnClickListener(v -> onAccept());

        viewModel.isSuccessRegister.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean){
                    successRegistration();
                }
            }
        });
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

    //TODO do something. It maybe incorrect
    private void successRegistration(){
        startActivity(new Intent(this.getContext(), MainActivity.class));
        this.getActivity().finish();
    }

}
