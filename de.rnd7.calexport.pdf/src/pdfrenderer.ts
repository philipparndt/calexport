import { BrowserWindow, PrintToPDFOptions, WebContents, app } from "electron";
const pdf = require('pdf-parse');
import * as fs from 'fs';

export function render(sourceFile: string): Promise<void> {
    return new Promise<void>((resolve, reject) => {
        savePDF(sourceFile).then(resolve);
    });
}

async function savePDF(sourceFile: string) {
    let window = await loadDocument(sourceFile);
    let webContents = window.webContents;

    var data: Buffer;

    for (var zoomLevel = 100; zoomLevel > 70; zoomLevel-=2.5) {
        webContents.insertCSS(`@media print {html, body {zoom: ${zoomLevel}%;}`);

        console.log(`Generating page with zoomLevel: ${zoomLevel}%`);
        data = await printToPDF(webContents);
    
        let pageCount = await pages(data);

        if (pageCount == 1) {
            break;
        }
        else {
            console.log(`too many pages: ${pageCount}`);
        }
    }

    let targetFile = sourceFile.replace(/\.html$/ig, '.pdf');
    await writeFile(targetFile, data);
    console.log(`pdf file written: ${targetFile}`)
}

function loadDocument(sourceHtml: string): Promise<BrowserWindow> {
    return new Promise<BrowserWindow>((resolve, reject) => {
        let window = new BrowserWindow({
            show: false,
            webPreferences: {
                offscreen: true
            }
        });
        
        window.loadFile(sourceHtml);
        window.webContents.on("did-finish-load", () => {
            resolve(window);
        });
    });        
}

function options(): PrintToPDFOptions {
    return {
        marginsType: 0,
        pageSize: 'A4',
        printBackground: true,
        printSelectionOnly: false,
        landscape: false
    }
}

function printToPDF(webContents: WebContents): Promise<Buffer> {
    return new Promise<Buffer>((resolve, reject) => {
        webContents.printToPDF(options(), (error, data) => {
            if (error) {
                reject(error);    
            }
            else {
                resolve(data);
            }
        });
    });        
}

const pages = (data: Buffer) => pdf(data).then((document: any) => {
    return document.numpages;
});

function writeFile(fileName: string, data: Buffer): Promise<void> {
    return new Promise<void>((resolve, reject) => {
        fs.writeFile(fileName, data, (error) => {
            if (error) {
                reject(error);    
            }
            else {
                resolve();
            }
        });
    });        
}

function documentWritten(error: Error) {
    if (error) {
        throw error
    }

    console.log('Write PDF successfully.')
}
