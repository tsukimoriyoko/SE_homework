package com.example.se.ui.register;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.se.R;
import com.example.se.data.RegisterRepository;
import com.example.se.data.Result;

public class RegisterViewModel extends ViewModel {

    private MutableLiveData<RegisterFormState> registerFormState = new MutableLiveData<>();
    private MutableLiveData<RegisterResult> registerResult = new MutableLiveData<>();
    private RegisterRepository registerRepository;

    RegisterViewModel(RegisterRepository registerRepository) {
        this.registerRepository = registerRepository;
    }

    LiveData<RegisterFormState> getRegisterFormState() {
        return registerFormState;
    }

    LiveData<RegisterResult> getRegisterResult() {
        return registerResult;
    }

    public void register(String username, String nickname,
                         String password, String bind_code) {
        // can be launched in a separate asynchronous job
        Result<String> result = registerRepository.register(username, nickname, password, bind_code);
        if (result instanceof Result.Success) {
            registerResult.setValue(new RegisterResult("Register Success"));
        } else {
            registerResult.setValue(new RegisterResult(-1));
        }
    }

    public void getCaptcha(String cellphone) {
        Result<String> result = registerRepository.getCaptcha(cellphone);
        if (result instanceof Result.Success) {
            registerResult.setValue(new RegisterResult("Get Captcha Success"));
        } else {
            registerResult.setValue(new RegisterResult(-1));
        }
    }

    public void registerDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            registerFormState.setValue(new RegisterFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            registerFormState.setValue(new RegisterFormState(null, R.string.invalid_password));
        } else {
            registerFormState.setValue(new RegisterFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (!username.matches("[0-9]*")) {
            return false;
        } else {
            return username.length() == 11;
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }
}
