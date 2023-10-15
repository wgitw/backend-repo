# Backend Installation

## Execution: Dev Mode
1. 실행 버전
실행은 develop branch에서 진행합니다.
2. 실행 환경
도커 환경에서 실행되도록 만든 개발환경입니다.
아래 내용을 실행하기 전, 도커 deamon이 local 환경에 세팅되어 있는지 확인해주세요.

### Run command
```
cd “YOUR_DOWNLOAD_LOCATION”

git clone https://github.com/Pocket-PT/backend-repo

## insert configFile (.env.dev)
vi .env.dev

## Before starting the next line, update .env.dev using the information below.
docker-compose -f docker-compose.dev.yml up --build

```

### .env.dev
```
# Spring setting
SPRING_PROFILES_ACTIVE=dev
SPRING_JPA_GENERATE_DDL=true
SPRING_JPA_HIBERNATE_DDL_AUTO=create

# Database setting for docker
MYSQL_DATABASE=userdb
MYSQL_ROOT_HOST=root
MYSQL_ROOT_PASSWORD=rootpwd

# Spring Database setting for docker
SPRING_DATASOURCE_URL=jdbc:mysql://mysql-db:3306/userdb?useSSL=false&autoReconnect=true&allowPublicKeyRetrieval=true&useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC
SPRING_DATASOURCE_USERNAME=root
SPRING_DATASOURCE_PASSWORD=rootpwd

# Spring OAuth setting
SPRING_SECURITY_KAKAO_CLIENT_ID=${YOUR_SPRING_SECURITY_KAKAO_CLIENT_ID}
SPRING_SECURITY_KAKAO_CLIENT_SECRET=${YOUR_SPRING_SECURITY_KAKAO_CLIENT_SECRET}
SPRING_SECURITY_KAKAO_REDIRECT_URI=http://localhost:8080/login/oauth2/code/kakao

# Spring JWT setting
JWT_SECRET=${YOUR_JWT_SECRET}
# 24시간(ms)
JWT_ACCESS_EXPIRATION=60
# JWT_ACCESS_EXPIRATION=86400000
# JWT_ACCESS_EXPIRATION=20000
# 1주일(ms)
JWT_REFRESH_EXPIRATION=604800000
JWT_HEADER=Authorization
JWT_PREFIX=Bearer

# Spring CORS setting
# CORS_FRONTEND=https://pocketpt.netlify.app
CORS_FRONTEND=http://localhost:8080/api/v1/main

# Spring AWS S3 setting
AWS_S3_BUCKET_NAME=${YOUR_AWS_S3_BUCKET_NAME}
AWS_S3_BUCKET_URL=${YOUR_AWS_S3_BUCKET_URL}
AWS_S3_REGION=${YOUR_AWS_S3_REGION}
AWS_S3_ACCESS_KEY=${YOUR_AWS_S3_ACCESS_KEY}
AWS_S3_SECRET_KEY=${YOUR_AWS_S3_SECRET_KEY}

# Spring Redis setting for docker
SPRING_REDIS_HOST=redis
SPRING_REDIS_PORT=6379
SPRING_REDIS_PASSWORD=${YOUR_SPRING_REDIS_PASSWORD}

```

## Monitoring
1. 실행 버전(실행 환경과 동일)
실행은 develop branch에서 진행합니다.
2. 실행 환경(실행 환경과 동일)
도커 환경에서 실행되도록 만든 개발환경입니다.
아래 내용을 실행하기 전, 도커 deamon이 local 환경에 세팅되어 있는지 확인해주세요.

### Run command
실행 환경에서 'docker-compose -f docker-compose.dev.yml up --build' 이후에 새로운 terminal을 띄워서 진행하세요.
```
docker-compose -f docker-compose.monitoring.yml up --build
```

### Check
1. http://loaclhost:3000 로 grafana에 접속합니다.
2. id: admin, password: admin으로 로그인합니다.
3. dashboard 메뉴에 접속하여 원하는 dashboard를 선택한 후 모니터링을 진행합니다.
