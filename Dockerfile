FROM openjdk:16-jdk-slim
WORKDIR /opt/krypton
COPY bootstrap/build/libs/Krypton-*.jar Krypton.jar
ENTRYPOINT ["java", "-jar", "Krypton.jar"]