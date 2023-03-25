# 📊⛏ Mobile.de Car Data Scraper
[![CI Maven Build & Sonar](https://github.com/robertciotoiu/mobile-de-car-data-collector/actions/workflows/build-and-sonar-analysis-main.yml/badge.svg?branch=main)](https://github.com/robertciotoiu/mobile-de-car-data-collector/actions/workflows/build-and-sonar-analysis-main.yml)

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=robertciotoiu_mobile-de-scraper&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=robertciotoiu_mobile-de-scraper)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=robertciotoiu_mobile-de-scraper&metric=coverage)](https://sonarcloud.io/summary/new_code?id=robertciotoiu_mobile-de-scraper)
[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=robertciotoiu_mobile-de-scraper&metric=sqale_index)](https://sonarcloud.io/summary/new_code?id=robertciotoiu_mobile-de-scraper)
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=robertciotoiu_mobile-de-scraper&metric=ncloc)](https://sonarcloud.io/summary/new_code?id=robertciotoiu_mobile-de-scraper)

[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=robertciotoiu_mobile-de-scraper&metric=bugs)](https://sonarcloud.io/summary/new_code?id=robertciotoiu_mobile-de-scraper) [![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=robertciotoiu_mobile-de-scraper&metric=vulnerabilities)](https://sonarcloud.io/summary/new_code?id=robertciotoiu_mobile-de-scraper) [![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=robertciotoiu_mobile-de-scraper&metric=duplicated_lines_density)](https://sonarcloud.io/summary/new_code?id=robertciotoiu_mobile-de-scraper) [![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=robertciotoiu_mobile-de-scraper&metric=reliability_rating)](https://sonarcloud.io/summary/new_code?id=robertciotoiu_mobile-de-scraper) [![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=robertciotoiu_mobile-de-scraper&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=robertciotoiu_mobile-de-scraper) [![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=robertciotoiu_mobile-de-scraper&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=robertciotoiu_mobile-de-scraper) [![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=robertciotoiu_mobile-de-scraper&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=robertciotoiu_mobile-de-scraper)

Mobile.de Car Data Scraper is a responsible and ethical data scraping project that retrieves car listing data from [Mobile.de](https://www.mobile.de/). This project enforces delays between requests to avoid overloading the website's servers.

The project is written in Java 19 and makes use of the following technologies:
- Spring Boot
- Maven
- Log4j2
- JUnit 5
- Docker and Kubernetes

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

You will need to have the following software installed on your machine:
- Java 19
- Docker and Kubernetes (optional, only if you want to deploy the project in a containerized environment)

### Running the application

1. Clone the repository

```shell
git clone https://github.com/robertciotoiu/mobile-de-scraper.git
```

2. Set a valid [localPath](infrastructure/kubernetes/mobile-de-mongodb-configmap.yaml) to point to a location on your disk where the MongoDB will persist


3. Navigate to the project directory

```shell
cd mobile-de-car-data-collector/infrastructure
```

4. Build docker & push images and deploy all pods to a K8s namespace

```shell
./deploy.sh
```

Docker images will be built and pushed to the local docker image repository. Then it will create a namespace named "rc"(can be changed) and the K8s resources. 
Each pod will automatically start to crawl, parse and save car data listings into a MongoDB that runs in its own pod but persists the data locally on the disk.

### Running the tests

To run the JUnit tests, execute the following command in the project directory:

```mvn test```


## Deployment

If you want to deploy the project in a containerized environment, you can use Docker and Kubernetes.

## Built With

- Java 19
- Spring Boot
- Maven
- Log4j2
- JUnit 5
- Docker and Kubernetes

## Contributing

Please read [CONTRIBUTING.md](CONTRIBUTING.md) for details on our code of conduct, and the process for submitting pull requests.

## Authors

* **Robert Ciotoiu** - [robertciotoiu](https://github.com/robertciotoiu)

See also the list of [contributors](https://github.com/robertciotoiu/mobile-de-scraper/contributors) who participated in this project.

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details.
