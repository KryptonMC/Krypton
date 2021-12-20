FROM openjdk:17-jdk-slim
WORKDIR /opt/krypton
COPY server/build/libs/Krypton-*.jar Krypton.jar
ENTRYPOINT ["java", "-jar", "Krypton.jar"]