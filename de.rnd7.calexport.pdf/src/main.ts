import { app } from "electron";

import * as fs from "fs";
import * as pdf from "./pdfrenderer";

app.disableHardwareAcceleration();

console.log(`Welcome to PDF converter.
This will convert all *.html files in a given folder to single page PDF files.`);

const targetFolder = target(process.argv);
if (!targetFolder) {
    console.log("please specify a target folder");
    app.quit();
} else {
    console.log(targetFolder);

    app.once("ready", () => {
        const dir = fs.readdirSync(targetFolder);
        const promises = dir.filter((element) => element.endsWith(".html"))
        .map((element) => `${targetFolder}/${element}`)
        .map((file) => pdf.render(file));

        Promise.all(promises).then(() => {
            console.log("==== Completed all ====");
            app.quit();
        }).catch((error) => {
            console.log(error);
        });
    });
}

function target(args: string[]): string {
    if (args) {
        if (args.length === 3 && args[1].includes("main.js")) {
            // executed using npm start
            return args[2];
        } else if (args.length === 2 && !args[1].includes("main.js")) {
            // executed the exported app
            return args[1];
        }
    }
}
