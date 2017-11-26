# calexport

![Build status](https://travis-ci.org/philipparndt/calexport.svg?branch=master) [![Quality Gate](https://sonarcloud.io/api/badges/gate?key=de.rnd7.calexport:de.rnd7.calexport)](https://sonarcloud.io/dashboard/index/de.rnd7.calexport:de.rnd7.calexport) [![Coverage](https://sonarcloud.io/api/badges/measure?key=de.rnd7.calexport:de.rnd7.calexport&metric=coverage)](https://sonarcloud.io/dashboard/index/de.rnd7.calexport:de.rnd7.calexport)

This tool will create a month export from multiple web calendars (ical). 

# Example export:
![example](https://github.com/philipparndt/calexport/blob/master/example.png)

# Command line parameters:
There are two command line arguments:
template (template file)
config (configuration file)

# Template
The template file is a freemarker template file containing a html template with the following parameters:
- Year (Year name)
- Month (Month 01-12)
- MonthName (Month name)
- Title (The title as specified in the configuration file)
- Table (The month table, one column per calendar and one row per day)

Example:
```
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN"
        "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="de" lang="de">
    <head>
        <meta charset="utf-8"/>
        <style>
        ...
        </style>
        <title>
            ${Year}-${Month}
        </title>
    </head>
    <body>
        <div class="titleDate">
            ${MonthName} ${Year}
        </div>
        <div class="title">
            ${Title}
        </div>
        ${Table}
    </body>
</html>
```

You can find a full example here:
https://raw.githubusercontent.com/philipparndt/calexport/master/de.rnd7.calexport/src/main/resources/de/rnd7/calexport/renderer/template.ftl

# Configuration
Within the configuration file it is possible to specify the amount of months to be exported, the calendars to be exported and some hashtags that will be replaced with images and a bas64 encoded representation of the image.

It is possible to use the template paramters in the configuration as well (for example as a calendar title)

Example:
```
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<calconfig>
	<exportmonths>16</exportmonths>
	<title>Example title</title>
	<footer></footer>
	<calendar>
		<url>https://calendar.google.com/calendar/ical/.../basic.ics</url>
		<name>${MonthName} ${Year}</name>
		<classes>main</classes>
		<type>DATE</type>
	</calendar>
	<calendar>
		<url>https://calendar.google.com/calendar/ical/.../basic.ics</url>
		<name>A</name>
		<classes>classA</classes>
		<type>LOCATION</type>
	</calendar>	
	...
	<hashtag>
		<name>AB</name>
		<width>10</width>
		<height>01</height>
		<image>data:image/png;base64,iVB...</image>
	</hashtag>
</calconfig>
```
