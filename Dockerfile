#Grab the latest alpine image
FROM alpine:latest

RUN apk add --no-cache --update curl bash openjdk8

# Add our code
RUN mkdir -p /app/java-getting-started/
ADD ./java-getting-started /app/java-getting-started/
WORKDIR /app/java-getting-started/


# Install Maven
RUN  curl -s --retry 3 -L -O https://lang-jvm.s3.amazonaws.com/maven-3.3.3.tar.gz && mkdir maven3.3 &&  tar -zxvf maven-3.3.3.tar.gz -C maven3.3 && chmod +x maven3.3/.maven/bin/mvn



RUN export M2_HOME=/app/java-getting-started/maven3.3/.maven && export PATH=/app/java-getting-started/maven3.3/.maven/bin:$PATH && mvn package -DskipTests


# Expose is NOT supported by Heroku
# EXPOSE 5000 		


# Run the app.  CMD is required to run on Heroku
# $PORT is set by Heroku			
CMD java -jar target/java-getting-started-1.0.jar
