FROM openjdk:8-jre-alpine

RUN mkdir maven && mkdir maven/config
COPY maven/ /maven/
COPY /scripts/artifact_runner.sh maven/
COPY /resources/logback.xml maven/config/

RUN chmod 555 /maven/artifact_runner.sh

EXPOSE ${project.service.port}

CMD cd /maven && ./artifact_runner.sh ${final-artifact-name}.jar
