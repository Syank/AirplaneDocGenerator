const { ipcRenderer, contextBridge } = require('electron');

contextBridge.exposeInMainWorld('electron', {
    windowControll: {
        minimize () {
            minimizeWindow()
        },
        maximize () {
            maximizeWindow()
        },
        isMaximized () {
            ismaximizedWindow()
        },
        unmaximize () {
            unmaximizeWindow()
        },
        close () {
            closeWindow()
        }
    }
});

function minimizeWindow() {
    ipcRenderer.send("minimize");
}

function maximizeWindow() {
    ipcRenderer.send("maximize");
}

function closeWindow() {
    ipcRenderer.send("close");
}

function unmaximizeWindow() {
    ipcRenderer.send("unmaximize");
}

function ismaximizedWindow() {
    ipcRenderer.send("isMaximized");
}