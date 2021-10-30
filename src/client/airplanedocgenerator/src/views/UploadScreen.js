import React from "react";

import Button from "../assets/components/Button";

import { getBackgroundImage } from "../utils/pagesUtils";
import { notification } from "../assets/components/Notifications";

import ServerRequester from "../utils/ServerRequester";

class UploadScreen extends React.Component {
    constructor(props) {
        super(props);

        this.sendFormData = this.sendFormData.bind(this);
    }

    async sendFormData() {
        let serverRequester = new ServerRequester("http://localhost:8080");

        let codelistInput = document.getElementById("codelist-file");
        let projectFolderInput = document.getElementById("directory-location");

        let codelistFile = codelistInput.files[0];
        let projectFiles = projectFolderInput.files;

        let files = []

        for (let i = 0; i < projectFiles.length; i++) {
            let file = projectFiles[i];

            if(file.name.includes(".pdf")){
                files.push(file);

            }
            
        }

        let formData = new FormData();

        formData.append("codelist", codelistFile);

        for (let i = 0; i < files.length; i++) {
            const file = files[i];

            formData.append("projectFile", file);

        }

        
        let response = await serverRequester.doPost("/project/import",  formData, "multipart/form-data");

        if (response["responseJson"] === true) {
            notification(
                "success",
                "Oba! 😄",
                "Upload realizado com sucesso!"
            );
        } else {
            notification(
                "error",
                "Algo deu errado 🤕",
                "Ocorreu um problema ao tentar fazer a importação do projeto"
            );

        } 

    }

    /**
     * Função que identifica o nome do arquivo selecionado pelo usuário e o coloca na label
     * @returns Nome do arquivo selecionado pelo usuário
     */
    setFileName() {
        let files = document.getElementById("codelist-file").files;

        if (files.length !== 0) {
            let fileName = files[0].name;
            let labelFileName = document.getElementById("codelist-file-name");
            labelFileName.textContent = fileName;
        }
    }
    setDirectoryName() {
        let files = document.getElementById("directory-location").files;

        if (files.length !== 0) {
            let fileName = files[0].name;
            let labelFileName = document.getElementById("directory-name");
            labelFileName.textContent = fileName;
        }
    }

    /**
     * Função que cria a página
     *
     * @returns Página de criar um novo projeto
     * @author Anna Yamada
     */
    getUploadScreen() {
        let newProjectScreen = (
            <div id="contentDisplay" className="w-full h-full">
                {getBackgroundImage()}
                <div id="uploadtScreen"
                    className="w-full h-full flex justify-center items-center">
                    <div className="bg-white lg:w-5/12 md:w-10/12 h-5/6 relative flex flex-col border-r-8 border-b-8 border-accent">
                        <div className="m-8 text-center">
                            <p className="text-2xl font-bold">
                                Faça o upload de um projeto
                            </p>
                            <hr></hr>
                            <p className="text-lg mt-4">
                                Escolha o diretório de um projeto em sua máquina
                            </p>
                        </div>

                        <div className="m-8 p-4 text-center">
                            <label className="text-lg">Pasta: </label>
                            <label
                                htmlFor="directory-location"
                                id="directory-name"
                                className="w-68 p-1 px-4 rounded-lg bg-inputFileColor text-white cursor-pointer hover:bg-blue-300 active:bg-blue-300">
                                Selecionar diretório em sua máquina
                            </label>
                            <input
                                type="file"
                                id="directory-location"
                                onChange={this.setDirectoryName}
                                className="hidden" webkitdirectory="true"
                                accept=".pdf"
                            ></input>
                        </div>
                        <div className="m-8 p-5">
                            <label className="text-lg">Codelist: </label>
                            <label
                                htmlFor="codelist-file"
                                id="codelist-file-name"
                                className="w-68 p-1 px-4 rounded-lg bg-inputFileColor text-white cursor-pointer hover:bg-blue-300 active:bg-blue-300"
                            >
                                Selecionar Codelist em sua máquina
                            </label>
                            <input
                                type="file"
                                id="codelist-file"
                                onChange={this.setFileName}
                                className="hidden"
                                accept=".xls,.xlsx"
                            ></input>

                            <p className="text-xs ml-12 mr-12 mt-4 text-center">
                                Os campos nesta página são obrogatórios para realizar um upload
                            </p>
                        </div>
                        <div className="mt-12 text-center">
                            <Button text="Fazer Upload" onClick={this.sendFormData} type="confirm"></Button>
                        </div>
                    </div>
                </div>
            </div>
        );

        return newProjectScreen;
    }

    /**
     * Renderiza a página de upload
     * @returns Elemento a ser renderizado
     *
     */
    render() {
        return this.getUploadScreen();
    }
}

export default UploadScreen;
