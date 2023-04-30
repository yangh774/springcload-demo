FROM myjdk11:1.0.0

COPY *.jar /app.jar

CMD ["--server.port=8801"]

EXPOSE 8801

ENTRYPOINT ["java", "-jar", "/app.jar"]
