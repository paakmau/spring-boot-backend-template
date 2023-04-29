# Spring Boot Backend Template

[![Java CI with Gradle](https://github.com/paakmau/spring-boot-backend-template/actions/workflows/gradle.yaml/badge.svg)](https://github.com/paakmau/spring-boot-backend-template/actions/workflows/gradle.yaml)

This is a Spring Boot backend template.

## Features

- Database version control with Liquibase
- Automated API document generation with Springdoc
- Code formatting with Spotless
- Code quality checking with Sonarlint
- Commit message cheching with Commitlint

## Requirements

- JDK (version 17 or later)
- MySQL

## Quick start

Clone the repository.

```shell
$ git clone https://github.com/paakmau/spring-boot-backend-template
```

Navigate to project directory and run the Spring Boot application.

```shell
$ cd spring-boot-backend-template
$ ./gradlew bootRun
```

Enter `http://localhost:8080/swagger-ui/index.html` in the browser to access the document.

## Document generation

<https://github.com/springdoc/springdoc-openapi>

Springdoc will scan the `controller` package, and automatically generate API document.

When the application is running, these URLs are available:

- Swagger UI endpoint  
  `http://localhost:8080/swagger-ui/index.html`
- Swagger docs endpoint  
  `http://localhost:8080/v3/api-docs`
- Swagger docs in yaml format endpoint  
  `http://localhost:8080/v3/api-docs.yaml`

## Multiple environments

This template configures dev and prod environments via Spring Boot profiles. The default profile is dev.

The `application.yaml` for test environment is placed alone.

Profiles can be specified like this:

```shell
$ ./gradlew bootRun --args='--spring.profiles.active=uat'
```

## Database

H2 Database is used for dev and test. MySQL is used for prod.

Notice that the password of MySQL should not be saved in the repository. You need to set the password of root user in the `MYSQL_ROOT_PASSWORD` environment variable to access to MySQL database.

### Version control

<https://github.com/liquibase/liquibase>  
<https://github.com/liquibase/liquibase-gradle-plugin>  
<https://github.com/liquibase/liquibase-hibernate>

The changelog file is the `db.changelog-master.yaml` in the `resources/db/changelog` directory.

You can use Liquibase Gradle plugin to maintain the changelog. To modify the database structure, you should follow this workflow:

1. Edit entities in the `entity` package
2. Run the `diffChangeLog` Gradle task
3. Check if the modified `db.changelog-master.yaml` does what you want, edit it if not
4. Run this application, the database will be synced by Liquibase

## Exception handling

Exception classes should be put in the `exception` package. And use the `@ResponseStatus` annotation to specify the HTTP status code.

## Code formatting

<https://github.com/diffplug/spotless>.

### Check the style

You can check format violations by runing the `spotlessCheck` Gradle task.

```shell
$ ./gradlew spotlessCheck
```

### Apply the style

If there are violations, run the `spotlessApply` Gradle task to perform formatting.

```shell
$ ./gradlew spotlessApply
```

### VS Code integration

You can install the [Spotless Gradle](https://marketplace.visualstudio.com/items?itemName=richardwillis.vscode-spotless-gradle) extension to lint and format your code.

## Code quality checking

<https://remal.gitlab.io/gradle-plugins/plugins/name.remal.sonarlint/>.

### Check main

Check Java source files in the `main` directory.

```shell
$ ./gradlew sonarlintMain
```

### Check test

Check Java source files in the `test` directory.

```shell
$ ./gradlew sonarlintTest
```

### VS Code integration

Install the [SonarLint](https://marketplace.visualstudio.com/items?itemName=SonarSource.sonarlint-vscode) extension to lint your code.

## Commit message checking

<https://github.com/NetrisTV/gradle-commitlint-plugin>

Pass a commit message to Commitlint Gradle plugin, the plugin will lint it against [Conventional Commits](https://www.conventionalcommits.org/) rules.

This plugin should be used with Git Hook

## Git hook

<https://github.com/STAR-ZERO/gradle-githook>

There are some hooks configured in this template:

| Hook         | Gradle task     |
| ------------ | --------------- |
| `pre-commit` | `spotlessCheck` |
| `pre-commit` | `sonarlintMain` |
| `pre-commit` | `sonarlintTest` |
| `commit-msg` | `commitlint`    |
