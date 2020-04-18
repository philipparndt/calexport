FROM maven:3.6.3-jdk-8

LABEL maintainer="Philipp Arndt <2f.mail@gmx.de"
LABEL version="1.0"
LABEL description="Calexport"

ENV LANG en_US.UTF-8
ENV TERM xterm

# Install nodejs 
RUN apt-get update && \
apt-get -y install curl gnupg && \
curl -sL https://deb.nodesource.com/setup_12.x  | bash - && \
apt-get -y install nodejs && \
npm install

# Dependencies for headless chrome
RUN apt-get install -y libgtk-3-dev libnss3-dev libxss1 libasound2 xvfb

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

WORKDIR /opt/calexport
RUN cp -r de.rnd7.calexport.pdf/dist/build/pdfconverter-linux-x64 /

RUN mvn install assembly:single
RUN cp ./de.rnd7.calexport/target/calexport.jar /calexport.jar

COPY /docker/scripts/* /

CMD ["/app.sh"]
