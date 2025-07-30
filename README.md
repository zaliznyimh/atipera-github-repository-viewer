# Atipera GitHub Viewer API

> Technical assignment for Atipera. REST API built with Spring Boot 3.5.4 and Java 21 that retrieves GitHub user repositories with branch information.

## **🛠 Tech Stack**
- **Java 21**
- **Spring Boot 3.5**
- **JUnit 5** for integration testing
- **Gradle 8** for build automation
- **Lombok** for boilerplate reduction


## 🚀 Getting Started

### Clone the repository
```
git clone https://github.com/zaliznyimh/atipera-github-viewer.git
```

### Build the project
```
./gradlew build
```
### Run the application
```
./gradlew bootRun
```

The application will start on `http://localhost:8080`

## 🧪 Running Tests

### Run all tests
```
./gradlew test         
```

## 📡 API Endpoints

### Get User Repositories
**Description:** Retrieves all non-fork repositories for a GitHub user with branch information.

``` GET /api/github-viewer/{username}/repos ```

**Parameters:**
- `username` (path) - GitHub username

**Success Response (200):**
```
[
  {
    "name": "repository-name",
    "owner": "username",
    "branches": [
      {
        "name": "main",
        "lastCommitSHA": "jfu32dsuwqe2..."
      },
      {
        "name": "develop",
        "lastCommitSHA": "f6e5d4c3b2a1..."
      }
    ]
  }
]
```

**Error Response (404):**
```
{
  "status": 404,
  "message": "Github account with username {username} not found"
}
```