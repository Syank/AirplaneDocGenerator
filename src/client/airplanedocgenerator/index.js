const { app, BrowserWindow, ipcMain, dialog} = require("electron");

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
            preload: __dirname + "/src/preload.js", // use a preload script
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

