# Selenium POC API

A MuleSoft application demonstrating integration with Selenium WebDriver for automated web browser testing and validation.

## Overview

This proof-of-concept showcases how to integrate Selenium WebDriver within a MuleSoft application to automate web page interactions and validations. The application uses Selenium's Chrome WebDriver to navigate to a specified URL and verify the presence of expected text on the page.

## Features

- HTTP-triggered Selenium automation workflow
- Chrome WebDriver integration
- Configurable URL and validation text via properties
- Java module integration for Selenium operations
- Externalized configuration management
- Logging and error handling

## Technology Stack

- **MuleSoft Runtime**: Mule 4.x
- **Selenium**: 4.18.1
- **Java**: 17 (compiled with source compatibility 1.8)
- **Build Tool**: Maven
- **WebDriver**: ChromeDriver

## Prerequisites

Before running this application, ensure you have:

1. **MuleSoft Runtime** (Mule 4.x) installed
2. **Java 17** or higher
3. **Maven** 3.x or higher
4. **Google Chrome** browser installed
5. **ChromeDriver** installed and available in your system PATH
   - Download from: https://chromedriver.chromium.org/
   - Ensure ChromeDriver version matches your Chrome browser version

## Project Structure

```
selenium-poc-api/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/selenium/poc/
│   │   │       └── SeleniumPocUtil.java       # Selenium logic
│   │   ├── mule/
│   │   │   └── selenium-poc-api.xml           # Mule flows
│   │   └── resources/
│   │       ├── log4j2.xml                      # Logging config
│   │       └── selenium.properties             # Selenium config
│   └── test/
│       ├── munit/                              # MUnit tests
│       └── resources/
├── pom.xml                                     # Maven dependencies
└── mule-artifact.json                          # Mule artifact descriptor
```

## Configuration

### Selenium Properties

Edit [src/main/resources/selenium.properties](src/main/resources/selenium.properties) to configure:

```properties
# URL to navigate to
selenium.url=https://playground.mailslurp.com

# Expected text to validate on the page
selenium.expected.text=Sign in to your account
```

### HTTP Listener

The application exposes an HTTP endpoint on:
- **Host**: `0.0.0.0`
- **Port**: `8081`
- **Path**: `/trigger`

## Installation

1. Clone the repository:
```bash
git clone <repository-url>
cd selenium-poc-api
```

2. Install ChromeDriver (if not already installed):
```bash
# macOS (using Homebrew)
brew install chromedriver

# Or download manually from:
# https://chromedriver.chromium.org/
```

3. Build the project:
```bash
mvn clean package
```

## Running the Application

### Start the Mule Application

```bash
mvn mule:run
```

The application will start on port 8081.

### Trigger the Selenium Workflow

Once the application is running, trigger the Selenium test by calling:

```bash
curl http://localhost:8081/trigger
```

**Expected Response:**
```json
{"status": "completed"}
```

## How It Works

1. **HTTP Request**: A GET request to `/trigger` initiates the flow
2. **Java Invocation**: The flow calls `SeleniumPocUtil.openPlaygroundAndGetTitle()`
3. **Browser Automation**: 
   - ChromeDriver launches a Chrome browser instance
   - Navigates to the URL specified in `selenium.properties`
   - Retrieves the page source
   - Searches for the expected text
4. **Validation**: Returns `true` if text is found, `false` otherwise
5. **Logging**: 
   - SUCCESS message if text is found
   - ERROR message if text is not found
6. **Response**: Returns a JSON status to the caller

## Flow Details

### Main Flow (`main_flow`)
- **Trigger**: HTTP Listener on `/trigger`
- **Processing**: Calls `selenium_sub_flow`
- **Response**: Returns completion status

### Sub Flow (`selenium_sub_flow`)
- Logs entry
- Invokes static Java method `openPlaygroundAndGetTitle()`
- Choice router evaluates the result:
  - **When true**: Logs success message
  - **Otherwise**: Logs error message
- Logs exit

## Key Dependencies

```xml
<dependency>
    <groupId>org.seleniumhq.selenium</groupId>
    <artifactId>selenium-java</artifactId>
    <version>4.18.1</version>
</dependency>

<dependency>
    <groupId>org.mule.connectors</groupId>
    <artifactId>mule-http-connector</artifactId>
    <version>1.11.0</version>
</dependency>

<dependency>
    <groupId>org.mule.module</groupId>
    <artifactId>mule-java-module</artifactId>
    <version>1.2.15</version>
</dependency>
```

## Development

### Modifying Test Configuration

To test different URLs or validation text:

1. Update [src/main/resources/selenium.properties](src/main/resources/selenium.properties)
2. Rebuild and restart the application
3. Trigger the endpoint

### Adding New Selenium Tests

1. Add new methods to `SeleniumPocUtil.java`
2. Create corresponding flows in `selenium-poc-api.xml`
3. Configure new HTTP endpoints or schedulers as needed

## Troubleshooting

### ChromeDriver Issues

**Error**: `WebDriver not found` or `ChromeDriver executable not found`

**Solution**: 
- Ensure ChromeDriver is installed and in your PATH
- Verify ChromeDriver version matches your Chrome browser version

### Port Conflict

**Error**: `Address already in use: 8081`

**Solution**: 
- Change the port in [selenium-poc-api.xml](src/main/mule/selenium-poc-api.xml):
```xml
<http:listener-connection host="0.0.0.0" port="8082" />
```

### Browser Not Closing

**Note**: The browser is intentionally left open for testing purposes (see `SeleniumPocUtil.java` line 50-52). To enable automatic browser cleanup, uncomment:

```java
if (driver != null) {
    driver.quit();
}
```

## License

This is a proof-of-concept project for demonstration purposes.

## Author

MuleSoft POC Project

---

**Note**: This is a demonstration project and should not be used in production without proper error handling, security measures, and resource management.
