package com.zaliznyimh.github_viewer.dto.response;

public record ApplicationErrorResponse(
        Integer status,
        String message
) {
}
