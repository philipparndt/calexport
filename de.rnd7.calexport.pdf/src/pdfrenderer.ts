import { BrowserWindow, PrintToPDFOptions, WebContents, app } from "electron";
const pdf = require('pdf-parse');
import * as fs from 'fs';

export function render(sourceFile: string): Promise<void> {
    return new Promise<void>((resolve, reject) => 
        savePDF(sourceFile).then(resolve).catch(error => reject(error)));
}

async function savePDF(sourceFile: string) {
    const window = await loadDocument(sourceFile);
    const webContents = window.webContents;

    var data: Buffer;
    var zoomLevel = 100;

    for (; zoomLevel > 70; zoomLevel-=2.5) {
        webContents.insertCSS(`@media print {html, body {zoom: ${zoomLevel}%;}`);

        data = await printToPDF(webContents);
    
        const pageCount = await pdf(data).then((document: any) => document.numpages);

        if (pageCount == 1) {
            break;
        }
        else {
            console.log(`too many pages: ${pageCount} with zoomLevel: ${zoomLevel}%`);
        }
    }

    const targetFile = sourceFile.replace(/\.html$/ig, '.pdf');
    await writeFile(targetFile, data);
    console.log(`pdf file written: ${targetFile} (${zoomLevel}% scale)`)
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
    return new Promise<Buffer>((resolve, reject) => 
        webContents.printToPDF(options(), (error, data) => error ? reject(error) : resolve(data)));        
}

function writeFile(fileName: string, data: Buffer): Promise<void> {
    return new Promise<void>((resolve, reject) => 
        fs.writeFile(fileName, data, (error) => error ? reject(error) : resolve()));        
}