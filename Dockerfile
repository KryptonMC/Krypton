FROM openjdk:11-jdk-slim

WORKDIR /opt/krypton

COPY build/libs/Krypton-*.jar Krypton.jar

ENTRYPOINT ["java", "-jar", "Krypton.jar"]