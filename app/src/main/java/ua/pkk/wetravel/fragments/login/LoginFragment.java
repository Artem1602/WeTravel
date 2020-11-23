package ua.pkk.wetravel.fragments.login;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import ua.pkk.wetravel.R;
import ua.pkk.wetravel.databinding.FragmentLoginBinding;
import ua.pkk.wetravel.fragments.loginPage.LoginPageFragmentDirections;
import ua.pkk.wetravel.utils.User;

public class LoginFragment extends Fragment {
    private FragmentLoginBinding binding;
    private LoginFragmentViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(LoginFragmentViewModel.class);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false);
        binding.acceptBtnLog.setOnClickListener(v -> onAccept());
        binding.restorePasswordLog.setOnClickListener(v -> onRestore());

        viewModel.eventIsLogin.observe(getViewLifecycleOwner(), aBoolean -> {
            if (aBoolean) {
                userIsLogIn();
            }
        });
        return binding.getRoot();
    }

    private void onRestore() {
        reset_password();
    }

    private void onAccept() {
        if (!viewModel.isValidEmail(binding.mailEdLog.getText())) {
            binding.mailEdLog.setError(getContext().getResources().getString(R.string.isnt_valid_mail));
            return;
        } else if (binding.passwEdLog.getText().toString().isEmpty()) {
            binding.passwEdLog.setError(getContext().getResources().getString(R.string.empty_password));
        }
        viewModel.sign_in(binding.mailEdLog.getText().toString(), binding.passwEdLog.getText().toString());
    }

    private void reset_password() {
        //TODO go to reset fragment
    }

    private void userIsLogIn() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("user_data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userID", User.getInstance().getId());
        editor.apply();
        Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(LoginPageFragmentDirections.actionLoginPageFragmentToMainFragment());
        //May be it a bad idea...
        for (Fragment fragment :  getParentFragmentManager().getFragments()) {
            getParentFragmentManager().beginTransaction().remove(fragment).commit();
        }
    }

}


