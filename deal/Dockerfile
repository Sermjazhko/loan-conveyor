FROM maven:3.8.2-jdk-11

WORKDIR /deal
COPY . .
RUN mvn clean install -Dmaven.test.skip=true

CMD mvn spring-boot:run