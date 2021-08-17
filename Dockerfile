#Grab the latest alpine image
FROM alpine:latest

RUN apk add --no-cache --update curl bash

# Add our code
RUN mkdir -p /app/java-getting-started/
ADD ./java-getting-started /app/java-getting-started/
WORKDIR /app/java-getting-started/


# Install the JDK
RUN  curl -s --retry 3 -L -O https://lang-jvm.s3.amazonaws.com/jdk/cedar-14/openjdk1.8-latest.tar.gz && mkdir openjdk1.8 && tar -zxvf openjdk1.8-latest.tar.gz -C openjdk1.8
# Install Maven
RUN  curl -s --retry 3 -L -O https://lang-jvm.s3.amazonaws.com/maven-3.3.3.tar.gz && mkdir maven3.3 &&  tar -zxvf maven-3.3.3.tar.gz -C maven3.3 && chmod +x maven3.3/.maven/bin/mvn


RUN export JAVA_HOME=/app/java-getting-started/openjdk1.8 && export M2_HOME=/app/java-getting-started/maven3.3/.maven && export  PATH=/app/java-getting-started/openjdk1.8/bin:/app/java-getting-started/maven3.3/.maven/bin:$PATH && java -version && mvn -version


# Expose is NOT supported by Heroku
# EXPOSE 5000 		


# Run the app.  CMD is required to run on Heroku
# $PORT is set by Heroku			
CMD java -jar target/java-getting-started-1.0.jar
