package com.zaliznyimh.github_viewer;

import com.github.tomakehurst.wiremock.WireMockServer;

import com.zaliznyimh.github_viewer.dto.response.BranchResponse;
import com.zaliznyimh.github_viewer.dto.response.RepositoryResponse;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ControllerIntegrationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    private static WireMockServer wireMockServer;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        wireMockServer = new WireMockServer(wireMockConfig().dynamicPort());
        wireMockServer.start();

        registry.add("github.api.base-url",
                () -> "http://localhost:" + wireMockServer.port());
    }

    @AfterEach
    void tearDown() {
        if (wireMockServer != null) {
            wireMockServer.stop();
        }
    }

    @Test
    void ShouldReturnAllNonForkRepositoriesWithBranchesAndLastCommit() {

        setupGitHubApiExpectations();

        ResponseEntity<List<RepositoryResponse>> response = restTemplate.exchange(
                "/api/github-viewer/testUser/repos",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );

        Assertions.assertEquals(200, response.getStatusCode().value());

        List<RepositoryResponse> userRepositories = response.getBody();
        Assertions.assertNotNull(userRepositories);

        Assertions.assertEquals(2, userRepositories.size());

        List<String> repositoryNames = userRepositories.stream()
                .map(RepositoryResponse::name)
                .toList();
        Assertions.assertTrue(repositoryNames.contains("spring-boot-microservice"));
        Assertions.assertTrue(repositoryNames.contains("react-dashboard"));

        Assertions.assertFalse(repositoryNames.contains("forked-kotlin"));
        Assertions.assertFalse(repositoryNames.contains("community-fork-repo"));


        RepositoryResponse firstRepo = findRepositoryByName(userRepositories, "spring-boot-microservice");
        Assertions.assertEquals("spring-boot-microservice", firstRepo.name());
        Assertions.assertEquals("testUser", firstRepo.owner());
        Assertions.assertEquals(2, firstRepo.branches().size());

        List<String> branchNames = firstRepo.branches().stream()
                .map(BranchResponse::name)
                .toList();
        Assertions.assertTrue(branchNames.contains("main"));
        Assertions.assertTrue(branchNames.contains("feature/authentication"));

        List<String> commits = firstRepo.branches().stream()
                .map(BranchResponse::lastCommitSHA)
                .toList();
        Assertions.assertTrue(commits.contains("a1b2c3d4e5f6"));
        Assertions.assertTrue(commits.contains("f6e5d4c3b2a1"));

        RepositoryResponse secondRepo = findRepositoryByName(userRepositories, "react-dashboard");
        Assertions.assertEquals("react-dashboard", secondRepo.name());
        Assertions.assertEquals("testUser", secondRepo.owner());
        Assertions.assertEquals(1, secondRepo.branches().size());
    }

    private void setupGitHubApiExpectations() {
        wireMockServer.stubFor(get(urlEqualTo("/users/testUser/repos"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                                [
                                  {
                                    "name": "spring-boot-microservice",
                                    "fork": false,
                                    "owner": {"login": "testUser"}
                                  },
                                  {
                                    "name": "forked-kotlin",
                                    "fork": true,
                                    "owner": {"login": "testUser"}
                                  },
                                  {
                                    "name": "react-dashboard",
                                    "fork": false,
                                    "owner": {"login": "testUser"}
                                  },
                                  {
                                    "name": "community-fork-repo",
                                    "fork": true,
                                    "owner": {"login": "testUser"}
                                  }
                                ]
                                """)));

        wireMockServer.stubFor(get(urlEqualTo("/repos/testUser/spring-boot-microservice/branches"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                                [
                                  {
                                    "name": "main",
                                    "commit": {"sha": "a1b2c3d4e5f6"}
                                  },
                                  {
                                    "name": "feature/authentication",
                                    "commit": {"sha": "f6e5d4c3b2a1"}
                                  }
                                ]
                                """)));

        wireMockServer.stubFor(get(urlEqualTo("/repos/testUser/react-dashboard/branches"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                                [
                                  {
                                    "name": "master",
                                    "commit": {"sha": "abc123main"}
                                  }
                                ]
                                """)));
    }

    private RepositoryResponse findRepositoryByName(List<RepositoryResponse> repositories, String name) {
        return repositories.stream()
                .filter(repository -> name.equals(repository.name()))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Repository " + name + " not found"));
    }
}
