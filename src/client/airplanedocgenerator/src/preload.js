const { ipcRenderer, contextBridge } = require('electron');

contextBridge.exposeInMainWorld('electron', {
    windowControll: {
        minimize () {
            minimizeWindow()
        },
        maximize () {
            maximizeWindow()
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