package ua.pkk.wetravel.login;

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
import ua.pkk.wetravel.databinding.FragmentLoginBinding;

public class LoginFragment extends Fragment {
    private FragmentLoginBinding binding;
    private LoginFragmentViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(LoginFragmentViewModel.class);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false);
        binding.acceptBtnLog.setOnClickListener(v -> onAccept());
        binding.restorePasswordLog.setOnClickListener(v -> onRestore());
        return binding.getRoot();
    }

    private void onRestore() {
        reset_password();
    }

    private void onAccept() {
        if (!viewModel.isValidEmail(binding.mailEdLog.getText())) {
            ;
            binding.mailEdLog.setError(getContext().getResources().getString(R.string.isnt_valid_mail));
            return;
        } else if (binding.passwEdLog.getText().toString().isEmpty()) {
            binding.passwEdLog.setError(getContext().getResources().getString(R.string.empty_password));
        }
        viewModel.sign_in(binding.mailEdLog.getText().toString(), binding.passwEdLog.getText().toString());
    }

    private void reset_password() {
        //TODO go to reset activity
    }

}

//    @Override
//    public void onStop() {
//        viewModel.email = user_login.getText().toString();
//        viewModel.password = user_password.getText().toString();
//        super.onStop();
//    }
//
//    @Override
//    public void onResume() {
//        Log.d("TAG", viewModel.email + " " + viewModel.password);
//        user_login.getText().append(viewModel.email);
//        user_password.getText().append(viewModel.password);
//        super.onResume();
//    }
