const { app, BrowserWindow, ipcMain, dialog} = require("electron");
const fs = require('fs');

var mainWindow;

app.on("ready", () => {
    mainWindow = new BrowserWindow({
        height: 850,
        width: 1200,
        minHeight: 675,
        minWidth: 880,
        frame: true,
        webPreferences: {
            enableRemoteModule: true,
            nodeIntegration: false,
            worldSafeExecuteJavaScript: true,
            contextIsolation: true,
            preload: __dirname + "/src/preload.js",
        },
    });

    mainWindow.setResizable(true);

    mainWindow.loadURL(`http://localhost:3000`);
    
});

ipcMain.on("minimize", function () {
    mainWindow.minimize();
});

ipcMain.on("close", function () {
    mainWindow.close();
});

ipcMain.on("maximize", function () {
    mainWindow.maximize();
});

ipcMain.on("unmaximize", function () {
    mainWindow.unmaximize();
});

ipcMain.on("showDialog", function () {
    let options = {
        properties:["openDirectory"]
    }
    global.pathToSaveFile = dialog.showOpenDialog(options);
});

ipcMain.on("download", function (event, data) {
    let base64File = data[0];
    let path = data[1][0]
    let fileName = data[2];
    let fileExtension = data[3];

    let fileBuffer;
    if (fileExtension === ".xlsx") {
        fileBuffer = Buffer.from(base64File.replace("data:application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;base64", ""),'base64');
    }
    else {
        fileBuffer = Buffer.from(base64File, 'base64');
    }
    
    fs.writeFileSync(path + "\\" + fileName + fileExtension, fileBuffer);
});

