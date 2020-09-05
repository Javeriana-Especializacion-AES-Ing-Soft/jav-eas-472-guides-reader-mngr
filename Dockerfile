FROM openjdk:11
ADD target/jav-eas-472-guides-reader-mngr-0.0.1-SNAPSHOT.jar jav-eas-472-guides-reader-mngr-0.0.1-SNAPSHOT.jar
EXPOSE 9096
ENTRYPOINT ["java", "-jar", "jav-eas-472-guides-reader-mngr-0.0.1-SNAPSHOT.jar"]

