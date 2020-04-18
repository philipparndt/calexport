import * as electron from "electron";
import * as fs from "fs";

const pdf = require("pdf-parse");

export function render(sourceFile: string): Promise<void> {
    return new Promise<void>((resolve, reject) =>
        savePDF(sourceFile)
        .then(resolve)
        .catch((error) => reject(error)));
}

async function savePDF(sourceFile: string) {
    const window = await loadDocument(sourceFile);
    const webContents = window.webContents;

    let data: Buffer;
    let zoomLevel = 100;

    for (; zoomLevel > 70; zoomLevel -= 2.5) {
        webContents.insertCSS(`@media print {html, body {zoom: ${zoomLevel}%;}`);

        data = await printToPDF(webContents);

        const pageCount = await pdf(data)
        .then((document: any) => document.numpages)
        .catch((error: any) => console.log(`Error parsing pdf for file '${sourceFile}': ${error}`));

        if (pageCount === 1) {
            break;
        } else {
            console.log(`too many pages: ${pageCount} with zoomLevel: ${zoomLevel}%`);
        }
    }

    const targetFile = sourceFile.replace(/\.html$/ig, ".pdf");
    await writeFile(targetFile, data);
    console.log(`pdf file written: ${targetFile} (${zoomLevel}% scale)`);
}

function loadDocument(sourceHtml: string): Promise<electron.BrowserWindow> {
    return new Promise<electron.BrowserWindow>((resolve, reject) => {
        const window = new electron.BrowserWindow({
            show: false,
            webPreferences: {
                offscreen: true,
            },
        });

        window.loadFile(sourceHtml);
        window.webContents.on("did-finish-load", () => {
            resolve(window);
        });
    });
}

function options(): electron.PrintToPDFOptions {
    return {
        landscape: false,
        marginsType: 0,
        pageSize: "A4",
        printBackground: true,
        printSelectionOnly: false,
    };
}

function printToPDF(webContents: electron.WebContents): Promise<Buffer> {
    return webContents.printToPDF(options());
}

function writeFile(fileName: string, data: Buffer): Promise<void> {
    return new Promise<void>((resolve, reject) =>
        fs.writeFile(fileName, data, (error) => error ? reject(error) : resolve()));
}
