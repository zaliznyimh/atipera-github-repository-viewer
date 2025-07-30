package com.zaliznyimh.github_viewer.util.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String username) {
        super("Github account with username " + username + " not found");
    }
}
