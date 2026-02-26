# Step 1: Use official OpenJDK image
FROM eclipse-temurin:17-jdk-jammy

# Step 2: Set working directory
WORKDIR /app

# Step 3: Copy Maven wrapper and pom.xml to download dependencies
COPY mvnw pom.xml ./
COPY .mvn .mvn

# Step 4: Download dependencies (offline build)
RUN ./mvnw dependency:go-offline -B

# Step 5: Copy the source code
COPY src src

# Step 6: Build the Spring Boot app
RUN ./mvnw clean package -DskipTests

# Step 7: Expose the port Spring Boot will run on
EXPOSE 8080

# Step 8: Command to run your app
CMD ["java", "-jar", "target/url_shortner-0.0.1-SNAPSHOT.jar"]