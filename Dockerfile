FROM azul/zulu-openjdk-alpine:21-latest AS build

WORKDIR /app

COPY gradlew .
COPY gradle gradle
COPY build.gradle .

RUN chmod +x ./gradlew

RUN ./gradlew dependencies

COPY src src

RUN ./gradlew build -x test

FROM azul/zulu-openjdk-alpine:21-jre-latest

WORKDIR /app

RUN addgroup --system --gid 1001 springboot && \
    adduser --system --uid 1001 --ingroup springboot springboot

COPY --from=build --chown=springboot:springboot /app/build/libs/*.jar app.jar

USER springboot

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]