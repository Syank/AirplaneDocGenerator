const { app, BrowserWindow } = require("electron");

let mainWindow;

app.on("ready", () => {
    mainWindow = new BrowserWindow({
        height:850, width:1200, 
        minHeight: 675, minWidth: 880, 
        frame: true
    });

    mainWindow.setResizable(true);
    
    mainWindow.loadURL(`http://localhost:3000`);
    
});
