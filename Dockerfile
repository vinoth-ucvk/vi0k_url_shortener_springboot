# Step 1: Use official OpenJDK image
FROM eclipse-temurin:17-jdk-jammy

# Step 2: Set working directory
WORKDIR /app

# Step 3: Copy Maven wrapper and pom.xml
COPY mvnw pom.xml ./
COPY .mvn .mvn

# Step 3.1: Make mvnw executable
RUN chmod +x mvnw

# Step 4: Download dependencies
RUN ./mvnw dependency:go-offline -B

# Step 5: Copy source code
COPY src src

# Step 6: Build Spring Boot app
RUN ./mvnw clean package -DskipTests

# Step 7: Expose port
EXPOSE 8080

# Step 8: Run the app
CMD ["java", "-jar", "target/url_shortner-0.0.1-SNAPSHOT.jar"]


