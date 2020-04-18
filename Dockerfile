FROM openjdk:8-jre as java-electron

# Dependencies for headless chrome
RUN apt-get update && \
    apt-get install -y \
    libgtk-3-dev \
    libnss3-dev \
    libxss1 \
    libasound2 \
    xvfb

FROM openjdk:8-jdk as builder

LABEL maintainer="Philipp Arndt <2f.mail@gmx.de"
LABEL version="1.0"
LABEL description="Calexport"

ENV LANG en_US.UTF-8
ENV TERM xterm

# Install maven
RUN apt-get update && apt-get -y install maven

# Install nodejs 
RUN apt-get update && \
apt-get -y install curl gnupg && \
curl -sL https://deb.nodesource.com/setup_12.x  | bash - && \
apt-get -y install nodejs && \
npm install

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
RUN mvn install assembly:single

FROM java-electron

COPY --from=builder /opt/calexport/de.rnd7.calexport.pdf/dist/build/pdfconverter-linux-x64 /pdfconverter-linux-x64
COPY --from=builder /opt/calexport/de.rnd7.calexport/target/calexport.jar /calexport.jar

COPY /docker/scripts/* /

CMD ["/app.sh"]