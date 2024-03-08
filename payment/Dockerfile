FROM maven:3.8.6-openjdk-11-slim as build         
WORKDIR /app                      
COPY . .                                     
RUN mvn clean package -DskipTests        

FROM openjdk:11                               
WORKDIR /app                                     
COPY --from=build ./app/target/*.jar ./app.jar
 
ARG EUREKA_SERVER=valorDefault 
ARG RABBITMQ_SERVER=valorDefault   
                     
ENTRYPOINT java -jar app.jar