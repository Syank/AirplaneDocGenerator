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
        },
        downloadFile (base64File, supposedSelectedPath, codelistName) {
            return downloadCodelistFile(base64File, supposedSelectedPath, codelistName)
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

function downloadCodelistFile(base64File, supposedSelectedPath, codelistName) {
    ipcRenderer.send("download", [base64File, supposedSelectedPath, codelistName]);
}
