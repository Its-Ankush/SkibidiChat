package com.realtimechatapp.demo;

import java.util.*;

public class GenericCharacterValidator {

    private String input;
    private final Set<Character> symbols;


    public GenericCharacterValidator(String input) {
        this.input = input;
        this.symbols = new HashSet<>();
        symbols.add('!');
        symbols.add('@');
        symbols.add('#');
        symbols.add('$');
        symbols.add('%');
        symbols.add('^');
        symbols.add('&');
        symbols.add('*');

    }


    public Map<Integer, Integer> giveCharacterCounts() {

        Set<Character> lower = new HashSet<>();
        Set<Character> upper = new HashSet<>();
        Set<Character> digit = new HashSet<>();
        Set<Character> others = new HashSet<>();
        Set<Character> symbolCount = new HashSet<>();
        Set<Character> spaces = new HashSet<>();


        for (int i = 0; i < input.length(); i++) {
            char currentChar = input.charAt(i);
            if (Character.isUpperCase(currentChar)) {
                upper.add(currentChar);
            } else if (Character.isLowerCase(currentChar)) {
                lower.add(currentChar);

            } else if (Character.isDigit(currentChar)) {
                digit.add(currentChar);

            } else if (symbols.contains(currentChar)) {
                symbolCount.add(currentChar);
            } else if (Character.isSpaceChar(currentChar)) {
                spaces.add(currentChar);
            } else {
                others.add(currentChar);
            }

        }
        Map<Integer, Integer> countReturn = new HashMap<>();
        countReturn.put(0, upper.size());
        countReturn.put(1, lower.size());
        countReturn.put(2, digit.size());
        countReturn.put(3, symbolCount.size());
        countReturn.put(4, spaces.size());
        countReturn.put(5, others.size());

        return countReturn;

    }

    void addError(Boolean condition, String error, List<String> errors) {
        if (condition) errors.add(error);
    }


    public String[] validatePassword() {
        String[] ans = new String[2];
        List<String> errors = new ArrayList<>();

        Map<Integer, Integer> charCounts = giveCharacterCounts();

        if (input.length() < 10 || input.length() > 20) {
            ans[0] = "false";
            ans[1] = "Password is less than 10 characters or more than 20 characters";
            return ans;
        }
        addError(charCounts.get(0) < 2, "Password must have minimum 2 unique uppercase letters", errors);
        addError(charCounts.get(1) < 2, "Password must have minimum 2 unique lowercase letters", errors);
        addError(charCounts.get(2) < 2, "Password must have minimum 2 unique digits", errors);
        addError(charCounts.get(3) < 2, "Password must have minimum 2 unique symbols from !@#$%^&*", errors);
        addError(charCounts.get(4) != 0, "No spaces allowed in password", errors);
        addError(charCounts.get(5) != 0, "One or more characters is not allowed in this password so please choose from !@#$%^&*", errors);

        if (errors.isEmpty()) {
            ans[0] = "true";
            ans[1] = "";
            return ans;

        } else {
            ans[0] = "false";
            ans[1] = String.join(". ", errors);
        }
        return ans;

    }

    public String[] validateUsername() {
        String[] ans = new String[2];
        List<String> errors = new ArrayList<>();

        Map<Integer, Integer> charCounts = giveCharacterCounts();

        if (input.length() < 5 || input.length() > 10) {
            ans[0] = "false";
            ans[1] = "Username is less than 5 characters or more than 10 characters";
            return ans;
        }

        int unnecessary = charCounts.get(0) + charCounts.get(3) + charCounts.get(4) + charCounts.get(5);
        addError(charCounts.get(1) < 2, "Username must have minimum 2 unique lowercase letters", errors);
        addError(charCounts.get(2) < 2, "Username must have minimum 2 unique digits", errors);
        addError(unnecessary != 0, "Characters other than lowercase letters and digits present in this username", errors);

        if (errors.isEmpty()) {
            ans[0] = "true";
            ans[1] = "";
            return ans;

        } else {
            ans[0] = "false";
            ans[1] = String.join(". ", errors);
        }
        return ans;
    }


    public Boolean loginUsernameValidation() {
        String[] ans = validateUsername();
        if (ans[0].equals("true")) {
            return true;
        }
        return false;
    }

    public Boolean loginPasswordValidation() {

        String[] ans = validatePassword();
        if (ans[0].equals("true")) {
            return true;
        }
        return false;


    }


}

