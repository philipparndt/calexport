import { app } from "electron";

import * as fs from "fs";
import * as pdf from "./pdfrenderer";

app.disableHardwareAcceleration();
app.allowRendererProcessReuse = false

console.log(`Welcome to PDF converter.
This will convert all *.html files in a given folder to single page PDF files.`);

const targetFolder = target(process.argv);
if (!targetFolder) {
    console.log("please specify a target folder as last command line argument");
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
    return args[args.length - 1]
}
