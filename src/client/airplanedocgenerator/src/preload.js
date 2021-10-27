const { ipcRenderer, contextBridge, remote } = require('electron');

contextBridge.exposeInMainWorld('electron', {
    windowControll: {
        minimize () {
            minimizeWindow()
        },
        maximize () {
            maximizeWindow()
        },
        unmaximize () {
            unmaximizeWindow()
        },
        close () {
            closeWindow()
        },
        showDialog () {
            return showOpenDialog()
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

function showOpenDialog() {
    ipcRenderer.send("showDialog");
    return remote.getGlobal("pathToSaveFile");
}
