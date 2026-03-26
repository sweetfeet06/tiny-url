# Getting Started

## Run locally
To run the application from the command line at project base:

```bash
./gradlew bootRun
```

To add a URL to the system use the following command once the application is running:

```bash
curl -v -H "Content-Type: text/plain" -H "Accept: text/plain"  -X POST "http://localhost:8080/v1/short-url" -d "https://www.google.com/"

```

To use the tiny URL functionality, open the browser and copy the returned URL from the command above and the address mapped to by the tiny URI will be loaded


### Reference Documentation
For further reference, please consider the following sections:

* [Official Gradle documentation](https://docs.gradle.org)
* [Spring Boot Gradle Plugin Reference Guide](https://docs.spring.io/spring-boot/4.0.1/gradle-plugin)
* [Create an OCI image](https://docs.spring.io/spring-boot/4.0.1/gradle-plugin/packaging-oci-image.html)

### Additional Links
These additional references should also help you:

* [Gradle Build Scans – insights for your project's build](https://scans.gradle.com#gradle)