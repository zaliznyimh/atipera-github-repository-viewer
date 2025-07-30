package com.zaliznyimh.github_viewer.dto.github;

public record GitBranch(
        String name,
        GitCommit commit
) {
}
