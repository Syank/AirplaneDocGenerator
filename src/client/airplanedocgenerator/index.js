const { app, BrowserWindow } = require("electron");

let mainWindow;

app.on("ready", () => {
    mainWindow = new BrowserWindow({frame: true});

    mainWindow.loadURL(`http://localhost:3000`);
});
