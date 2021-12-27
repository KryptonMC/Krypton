FROM openjdk:17-alpine
WORKDIR /opt/krypton
COPY server/build/libs/Krypton-*.jar Krypton.jar
ENTRYPOINT ["java", "-jar", "Krypton.jar"]