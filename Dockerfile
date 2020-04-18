FROM openjdk:8-jre-alpine as java-electron

RUN echo "http://dl-cdn.alpinelinux.org/alpine/v3.11/main" >> /etc/apk/repositories

# Dependencies for headless chrome
RUN apk update --no-cache && \
    apk add --no-cache \
    ttf-dejavu \
    xvfb \
    xvfb-run

FROM openjdk:8-jdk-alpine as builder

LABEL maintainer="Philipp Arndt <2f.mail@gmx.de"
LABEL version="1.0"
LABEL description="Calexport"

ENV LANG en_US.UTF-8
ENV TERM xterm

# Install maven
RUN apk update --no-cache && \
    apk add --no-cache maven

RUN apk add --update nodejs npm

# Install electron packager 
RUN npm install -g electron-packager

WORKDIR /opt/calexport

# Copy sources
COPY ./src /opt/calexport

# Compile, assemble and deploy the app
RUN cd de.rnd7.calexport.pdf && \
    npm install && \
    npm run build && \
    electron-packager . --asar --overwrite --out ./dist/build

RUN apk add ttf-dejavu
RUN mvn install assembly:single

FROM java-electron

COPY --from=builder /opt/calexport/de.rnd7.calexport.pdf/dist/build/pdfconverter-linux-x64 /pdfconverter-linux-x64
COPY --from=builder /opt/calexport/de.rnd7.calexport/target/calexport.jar /calexport.jar

COPY /docker/scripts/* /

#CMD ["/app.sh"]
CMD tail -f /dev/null