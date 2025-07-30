package com.zaliznyimh.github_viewer.dto.response;

public record BranchResponse(
        String name,
        String lastCommitSHA
) {
}
