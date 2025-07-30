package com.zaliznyimh.github_viewer.service.impl;

import com.zaliznyimh.github_viewer.client.GitHubClient;
import com.zaliznyimh.github_viewer.dto.github.GitBranch;
import com.zaliznyimh.github_viewer.dto.github.GitRepository;
import com.zaliznyimh.github_viewer.dto.response.BranchResponse;
import com.zaliznyimh.github_viewer.dto.response.RepositoryResponse;
import com.zaliznyimh.github_viewer.service.GitHubService;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GitHubServiceImplementation implements GitHubService {

    private final GitHubClient client;

    @Override
    public List<RepositoryResponse> getNonForkedUserRepositories(String username) {
       List<GitRepository> allUserRepositories = client.getUserRepositories(username);

       return allUserRepositories.stream()
               .filter(repository -> !repository.fork())
               .map(this::mapToRepositoryResponse)
               .toList();
    }

    private RepositoryResponse mapToRepositoryResponse(GitRepository repository) {
        List<GitBranch> branches = client.getRepositoryBranches(
                repository.owner().login(),
                repository.name()
        );

        List<BranchResponse> branchResponses = branches.stream()
                .map(branch -> new BranchResponse(
                        branch.name(),
                        branch.commit().sha()
                ))
                .toList();

        return new RepositoryResponse(
                repository.owner().login(),
                repository.name(),
                branchResponses
        );
    }
}
