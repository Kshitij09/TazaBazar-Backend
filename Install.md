# Installation Guide

TazaBazar backend is a Spring Boot Application and uses Gradle for building the artifacts. The App requires Postgres Server to be running on the same network and it involves maintaining certain configurations. We use [spring-dotenv](https://github.com/paulschwarz/spring-dotenv) to provide an environment source for the development setup. Production containers rely on the System's environment variables which are configured using `.env` file with docker-compose.

[Generate keypair](https://www.notion.so/Generate-keypair-9196f107c73d40518a1329d4a190f8d1)

## The `.env` file

Repository contains a `.env.example` file at the following directories:

- springboot-app
- deployment

Rename this file to `.env` and update the properties as needed.

| Variable Name         | Description                                                                                                                                            |
|-----------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------|
| DB_NAME               | Postgres database name                                                                                                                                 |
| DB_USERNAME           | Database username to use with JDBC                                                                                                                     |
| DB_PASSWORD           | Database password to use with JDBC                                                                                                                     |
| DB_HOST               | Host IP address on which Postgres Server is running                                                                                                    |
| TEST_DB_NAME          | Database for running tests                                                                                                                             |
| TEST_DB_USERNAME      | Test database username to use while running integration tests                                                                                          |
| TEST_DB_PASSWORD      | Test database password to use while running integration tests                                                                                          |
| FILESTORE_HOST        | Host IP address and port on which Spring Boot Server is running (This is used in generating image urls of the static image files served by the server) |
| FILESTORE_SOURCE_PATH | Absolute path to tazabazar image files' directory on your machine                                                                                      |
| FILESTORE_PATH        | Path within a container to mount 'FILESTORE_SOURCE_PATH' to                                                                                            |

## Setup Development environment

1. Prepare your own copy of `.env` file using `.env.example` file provided in the repository
2. You need a Postgres server running on your machine or network with two accounts (DB_USERNAME & TEST_DB_USERNAME) and two databases (DB_NAME & TEST_DB_NAME). You can run a Postgres executable meant for your platform and configure it accordingly or run a Docker container with the said configuration. The latter option is the **recommended** one and this repository is already providing the required setup for the same. To run a Postgres docker container with the mentioned setup:
    1. Install [docker](https://docs.docker.com/engine/install/) and [docker-compose](https://docs.docker.com/compose/install/) for your Platform.
    2. Make sure you're using the same copy of `.env`  file (***recommended**: create a symlink or shortcut*) in the 'springboot-app' and 'deployment' sub-directories.
    3. Change your directory to 'deployment'
    4. Run `docker-compose --project-name "tazabazaar" up database -d`
    5. This should launch a docker container and do the necessary initialization steps
3. "DB_HOST" should be set to IP address on which you're running the Postgres Server/Container (most commonly *localhost)*
4. *Continue with Common steps*

## Setup Deployment (Production) environment

*Currently, you need to install the JDK and need a jar (or jar.exe) to extract the jar. This will be handled in the docker-build itself in future updates.*

1. Install [docker](https://docs.docker.com/engine/install/) and [docker-compose](https://docs.docker.com/compose/install/) for your Platform.
2. Download and install the latest OpenJDK for your Platform.
3. Download the latest API-Server jar file from GitHub [Releases](https://github.com/Kshitij09/TazaBazar-Backend/releases)
4. Extract this jar file under `deployment/backend/target` directory
5. "DB_HOST" should be set to the container's internal IP address (setting it to 'database' would do the job as per current docker-compose.yml)
6. *Continue with Common steps*

## Common steps

1. Follow [Key Generation Guide](https://www.notion.so/b885dd8d5ed200d1260191e28b95b723) to create your own copy of Public and Private keypair and put those files under `springboot-app/src/main/resources/keys` directory.
2. Update `jwt.private-key-filepath` & `jwt.public-key-filepath` in the [application.properties](http://application.properties) files with the appropriate filenames you used. (We've renamed the keypair files to `tzb_key.pkcs8.private` & `tzb_key.x509.public` for better understanding)
3. Download and extract Images' Zip file from [Google Drive](https://drive.google.com/file/d/1ux9Hsqymn1BGVNr--fvz1nCCOiSoR3qs/view?usp=sharing) and extract it on the target machine
4. Update `FILESTORE_SOURCE_PATH` in the `.env` file to the absolute path where zip file from the previous step was extracted
5. Change your directory to 'deployment' and run `docker-compose --project-name "tazabazaar" up -d` (use `--force-recreate` option in case you've previously built images on this machine or trying to upgrade the Server's jar file)
