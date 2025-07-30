package com.zaliznyimh.github_viewer.dto.github;

public record GitRepository(
        String name,
        Boolean fork,
        GitOwner owner
) {
}
