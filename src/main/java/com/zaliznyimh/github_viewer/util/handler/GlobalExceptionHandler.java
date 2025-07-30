package com.zaliznyimh.github_viewer.util.handler;

import com.zaliznyimh.github_viewer.dto.response.ApplicationErrorResponse;
import com.zaliznyimh.github_viewer.util.exception.UserNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApplicationErrorResponse handleUserNotFoundException(UserNotFoundException ex) {
        return new ApplicationErrorResponse(404, ex.getMessage());
    }

    @ExceptionHandler(HttpClientErrorException.Forbidden.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApplicationErrorResponse handleGitHubForbidden() {
        return new ApplicationErrorResponse(403, "GitHub API rate limit exceeded. Please try again later.");
    }
}
