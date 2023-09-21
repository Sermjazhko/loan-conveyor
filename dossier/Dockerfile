FROM maven:3.8.2-jdk-11

WORKDIR /dossier
COPY . .
RUN mvn clean install -Dmaven.test.skip=true -e

CMD mvn spring-boot:run