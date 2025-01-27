FROM openjdk:21-jdk-slim
COPY build/libs/ms-booking-ms-booking-0.1-plain.jar ms-booking-0.1-plain.jar
ENTRYPOINT ["java","-jar","/ms-booking-ms-booking-0.1-plain.jar"]