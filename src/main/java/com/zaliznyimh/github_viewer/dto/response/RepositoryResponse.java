package com.zaliznyimh.github_viewer.dto.response;

import java.util.List;

public record RepositoryResponse(
        String owner,
        String name,
        List<BranchResponse> branches
) {
}
