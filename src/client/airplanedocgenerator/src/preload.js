const { ipcRenderer, contextBridge } = require('electron');

contextBridge.exposeInMainWorld('electron', {
    windowControll: {
        minimize () {
            minimizeWindow()
        },
        close () {
            closeWindow()
        }
    }
});

function minimizeWindow() {
    ipcRenderer.send("minimize");

}

function closeWindow() {
    ipcRenderer.send("close");
}