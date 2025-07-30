package com.zaliznyimh.github_viewer.service;

import com.zaliznyimh.github_viewer.dto.response.RepositoryResponse;

import java.util.List;

public interface GitHubService {
    List<RepositoryResponse> getNonForkedUserRepositories(String username);
}
