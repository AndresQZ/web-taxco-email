FROM gradle:6.5.1-jdk as builder
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN chmod +x ./wait-for-it.sh
RUN gradle build
RUN mkdir -p build/dependency && (cd build/dependency; jar -xf ../libs/*.jar)

FROM openjdk:8-jdk-alpine
RUN addgroup -S spring && adduser -S spring -G spring
USER root:root
COPY --from=builder /home/gradle/src/build/dependency/BOOT-INF/lib /app/lib
COPY --from=builder /home/gradle/src/build/dependency/META-INF /app/META-INF
COPY --from=builder /home/gradle/src/build/dependency/BOOT-INF/classes /app
COPY --from=builder /home/gradle/src/wait-for-it.sh ./
RUN apk add --update bash
#RUN chmod +x ./wait-for-it.sh
RUN ls -all

#ARG DEPENDENCY=target/dependency
#COPY ${DEPENDENCY}/BOOT-INF/lib /app/lib
#COPY ${DEPENDENCY}/META-INF /app/META-INF
#COPY ${DEPENDENCY}/BOOT-INF/classes /app

CMD ["java","-cp","app:app/lib/*","app.taxco.email.WebTaxcoEmailApplication"]