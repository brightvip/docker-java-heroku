#Grab the latest alpine image
FROM alpine:latest

RUN apk add --no-cache --update curl bash

# Add our code
RUN mkdir -p /app/java-getting-started/
ADD ./java-getting-started /app/java-getting-started/
WORKDIR /app/java-getting-started/


# Install the JDK
RUN curl -s --retry 3 -L https://lang-jvm.s3.amazonaws.com/jdk/cedar-14/openjdk1.8-latest.tar.gz | tar -zxvf
# Install Maven
RUN curl -s --retry 3 -L https://lang-jvm.s3.amazonaws.com/maven-3.3.3.tar.gz | tar -zxvf

RUN chmod +x /app/.maven/bin/mvn


RUN export JAVA_HOME=/app/.jdk && export M2_HOME=/app/.maven && export  PATH=/app/.jdk/bin:/app/.maven/bin:$PATH && java -version && mvn -version


# Expose is NOT supported by Heroku
# EXPOSE 5000 		


# Run the app.  CMD is required to run on Heroku
# $PORT is set by Heroku			
CMD java -jar target/java-getting-started-1.0.jar
