FROM openjdk:21-jdk
COPY build/libs/ms-booking-0.1.jar ms-booking-0.1.jar
ENTRYPOINT ["java","-jar","/ms-booking-0.1.jar"]