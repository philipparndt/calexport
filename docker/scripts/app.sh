#!/bin/sh

cd /target

# Download the calendars and export them to html
java -jar /calexport.jar -template /var/lib/calexport/calexport-prod.ftl -config /var/lib/calexport/calexport-prod.xml

# Convert the html files to pdf
xvfb-run -a /pdfconverter-linux-x64/pdfconverter --no-sandbox --disable-dev-shm-usage /target