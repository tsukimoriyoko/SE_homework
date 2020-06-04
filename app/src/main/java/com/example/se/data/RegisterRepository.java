package com.example.se.data;

public class RegisterRepository {
    private static volatile RegisterRepository instance;

    private RegisterDataSource dataSource;

    public static boolean isSuccess = false;

    // private constructor : singleton access
    private RegisterRepository(RegisterDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static RegisterRepository getInstance(RegisterDataSource dataSource) {
        if (instance == null) {
            instance = new RegisterRepository(dataSource);
        }
        return instance;
    }

    public Result<String> register(String username, String nickname,
                                   String password, String bind_code) {
        // handle login
        Result<String> result = dataSource.register(username, nickname, password, bind_code);
        if (result instanceof Result.Success) {
            isSuccess = true;
        }
        return result;
    }

    public Result<String> getCaptcha(String cellphone) {
        // handle requesting captcha
        return dataSource.getCaptcha(cellphone);
    }
}
