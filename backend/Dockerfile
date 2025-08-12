FROM sbtscala/scala-sbt:eclipse-temurin-17.0.4_1.7.1_3.2.0

WORKDIR /app

COPY build.sbt .
COPY project ./project
# RUN sbt update

COPY . .
RUN sbt clean compile

CMD ["sbt", "run"]
