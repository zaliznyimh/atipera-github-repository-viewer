package com.zaliznyimh.github_viewer.client;

import com.zaliznyimh.github_viewer.dto.github.GitBranch;
import com.zaliznyimh.github_viewer.dto.github.GitRepository;
import com.zaliznyimh.github_viewer.util.exception.UserNotFoundException;

import lombok.AllArgsConstructor;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
@AllArgsConstructor
public class GitHubClient {

    private RestClient restClient;

    public List<GitRepository> getUserRepositories(String username) {
        try {
            return restClient.get()
                    .uri("/users/{user}/repos", username)
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {
                    });
        } catch (HttpClientErrorException.NotFound ex) {
            throw new UserNotFoundException(username);
        }
    }

    public List<GitBranch> getRepositoryBranches(String owner, String repo) {
        return restClient.get()
                .uri("/repos/{owner}/{repo}/branches", owner, repo)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
        }
    }
