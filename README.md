## **🚧 Work in progress. Feel free to contribute 🤝**
# Mobile.de Car Data Scraper

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

### Installing

1. Clone the repository

```git clone https://github.com/robertciotoiu/mobile-de-scraper.git```

3. Navigate to the project directory

```cd mobile.de-car-data-scraper```

3. Build the project using Maven

```mvn clean install```

4. Run the project

```java -jar target/mobile.de-car-data-scraper.jar```


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
