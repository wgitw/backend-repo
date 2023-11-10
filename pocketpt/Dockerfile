#FROM openjdk:17-oracle AS builder
##RUN microdnf install findutils
##RUN apk update && apk add findutils
#COPY gradlew .
#COPY gradle gradle
#COPY build.gradle .
#COPY settings.gradle .
#COPY src src
#RUN chmod +x ./gradlew
#RUN ./gradlew bootjar
#
#FROM openjdk:17-oracle
#COPY --from=builder build/libs/pocketpt-0.0.1-SNAPSHOT.jar app.jar
#
#EXPOSE 8080
#ENTRYPOINT ["java","-jar","/app.jar"]


## JAR_FILE 변수 정의 -> 기본적으로 jar file이 2개이기 때문에 이름을 특정해야함
#ARG JAR_FILE=./build/libs/pocketpt-0.0.1-SNAPSHOT.jar
#
## JAR 파일 메인 디렉토리에 복사
#COPY ${JAR_FILE} pocketpt-0.0.1-SNAPSHOT.jar
#
## 시스템 진입점 정의
#ENTRYPOINT ["java","-jar","pocketpt-0.0.1-SNAPSHOT.jar"]

#
FROM openjdk:17-jdk

# JAR_FILE 변수 정의 -> 기본적으로 jar file이 2개이기 때문에 이름을 특정해야함
ARG JAR_FILE=./build/libs/pocketpt-0.0.1-SNAPSHOT.jar

# JAR 파일 메인 디렉토리에 복사
COPY ${JAR_FILE} pocketpt-0.0.1-SNAPSHOT.jar

# 시스템 진입점 정의
ENTRYPOINT ["java","-jar","pocketpt-0.0.1-SNAPSHOT.jar"]

# # GraalVM Image Build
## JAR_FILE 변수 정의 -> 기본적으로 jar file이 2개이기 때문에 이름을 특정해야함
#ARG JAR_FILE=./build/libs/pocketpt
#
## JAR 파일 메인 디렉토리에 복사
#COPY ${JAR_FILE} pocketpt
#
## 시스템 진입점 정의
#ENTRYPOINT ["./pocketpt"]
