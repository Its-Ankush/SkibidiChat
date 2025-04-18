package com.realtimechatapp.demo;


import java.util.Objects;

public class AuthServiceClass {

    private final CommonAuthInterface auth;

    public AuthServiceClass(CommonAuthInterface auth) {
        this.auth = auth;

    }

    //    gives jwt else null - good to be used in login but not in signup
    public String getLoginResult(String username, String password) {
        String error = validateFormData(username, password);
        String loginAuth = auth.loginAuth(username, password);

        if (error != null) {
            return "Must follow min username/password requirements. Check Signup page";
        } else {
            if (loginAuth.equals("true")) {
                return null;
            } else {
                return loginAuth;
            }

        }

    }

    //    boolean just a true or a false. If false = same user already exists. Else good to go
    public String getSignupResult(String username, String password) {
        String error = validateFormData(username, password);
        if (error != null) {
            return error;
        } else {
            if (auth.signupAuth(username, password)) {
                return null;
            } else {
                return "Username already exists. Pick a different one";
            }

        }
    }


    public String validateFormData(String username, String password) {
        GenericCharacterValidator genericUsernameValidator = new GenericCharacterValidator(username);
        GenericCharacterValidator genericPasswordValidator = new GenericCharacterValidator(password);
        System.out.println("Validating signup: " + username);
        String[] usernameValidation = genericUsernameValidator.validateUsername();
        String[] passwordValidation = genericPasswordValidator.validatePassword();
        if (Objects.equals(usernameValidation[0], "true") && Objects.equals(passwordValidation[0], "true")) {
            return null;
        } else if (usernameValidation[0].equals("true")) {
            return passwordValidation[1];

        }
        return usernameValidation[1] + ". " + passwordValidation[1];
    }


}
