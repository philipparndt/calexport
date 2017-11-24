<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN"
        "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="de" lang="de">
    <head>
        <meta charset="utf-8"/>
        <style>
            body {
              font-family: Calibri, Arial, Sans-Serif;
              font-size: 10pt;
            }

            table.image, tr.image, td.image {
            	border: 0px solid #555;
                padding: 0px;
            }

            table {
                border-collapse: collapse;
                width: 100%;
            }

            tr.noBorder {
                border: 0px solid black;
            }

            tfoot {
                border-top: 1px solid #000;
            }

            tr {
                border: 1px solid #555;
            }

            table, th, td {
                border: 0px solid #000;
            }

            td.leftBorder {
                border-left: 1px solid #000;
            }

            thead {
                border-top: 1px solid #000;
                font-weight: bold;
                font-variant: small-caps;
                font-size: 12pt;
            }

            .date {
                color: #234060;
            }

            .moessingen {
                color: #234060;
            }

            .bodelshausen {
                color: #00B04F;
            }

            .dusslingen {
                color: #C00200;
            }

            td { 
                padding: 1px;
                vertical-align:top;
            }

            td.dayColumn {
                width: 15%;
            }

            td.eventColumn {
                width: 30%;
            }

            .single-day { 
                font-size: 80%;
                font-weight: normal;
                color: #000;
            }

            .sunday { 
                background-color: #FAE2B0;
                font-weight: normal;
            }

            .multi-day {
                font-weight: normal;
                font-size: 80%;
                background-color: #EEE;
                border: 1px solid #333;
            }

            .multi-day p {
              writing-mode: vertical-rl;
              
              background-color: #EEE;
              padding: 2px;

              margin-left: auto;
              margin-right: auto;
              width: 1em;
            }

            .multi-day p span {
               background-color: #EEE;
            }

            .title {
                font-weight: bold;
                color: #555;
                margin-bottom: 10px;
            }

            .titleDate {
                font-weight: bold;
                font-size: 120%;
            }
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
