FROM openjdk:11
WORKDIR /user/app
COPY target/chess_docker_app.jar  .
ENTRYPOINT ["java","-jar","chess_docker_app.jar"]
