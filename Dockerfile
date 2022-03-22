FROM openjdk:11
COPY target/cassandra-0.0.1-SNAPSHOT.jar cassandra-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/cassandra-0.0.1-SNAPSHOT.jar"]