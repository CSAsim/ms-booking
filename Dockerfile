FROM openjdk:21-jdk-slim
COPY build/libs/ms-booking-ms-booking-0.1.jar ms-booking-0.1.jar
ENTRYPOINT ["java","-jar","/ms-booking-0.1.jar"]