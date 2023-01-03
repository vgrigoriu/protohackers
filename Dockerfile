FROM amazoncorretto:17

RUN mkdir /app && chown -R 1000 /app

USER 1000

COPY --chown=1000:1000 ./target/scala-3.2.1/Protohackers-assembly-0.1.0-SNAPSHOT.jar /app/protohackers.jar
WORKDIR /app

ENTRYPOINT [ "java", "-jar", "protohackers.jar" ]
