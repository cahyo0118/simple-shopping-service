FROM maven:3.6.3-openjdk-15-slim as MAVEN_BUILD

COPY . /build/

WORKDIR /build/

RUN mvn package

FROM maven:3.6.3-openjdk-15-slim

WORKDIR /app

COPY --from=MAVEN_BUILD