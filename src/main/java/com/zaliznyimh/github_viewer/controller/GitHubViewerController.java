package com.zaliznyimh.github_viewer.controller;

import com.zaliznyimh.github_viewer.dto.response.RepositoryResponse;
import com.zaliznyimh.github_viewer.service.GitHubService;

import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/github-viewer/")
@AllArgsConstructor
public class GitHubViewerController {

    private GitHubService gitHubService;

    @GetMapping("{username}/repos")
    public ResponseEntity<List<RepositoryResponse>> getUserRepositories(
            @PathVariable String username
    ) {
        List<RepositoryResponse> response = gitHubService.getNonForkedUserRepositories(username);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
