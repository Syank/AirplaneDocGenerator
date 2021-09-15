import React from "react";

import Button from "../assets/components/Button";
import Tooltip from "../assets/components/Tooltip";

import { getBackgroundImage } from "../utils/pagesUtils";
import { notification } from "../assets/components/Notifications";

import ServerRequester from "../utils/ServerRequester";

/**
 * Classe que representa a página de criação de um projeto
 * @author Bárbara Port
 */
class NewProjectScreen extends React.Component {
    /**
     * Criar um novo projeto
     *
     * @param {Event} event
     * @author Rafael Furtado
     */
    async sendFormData(event) {
        event.preventDefault();

        let serverRequester = new ServerRequester("http://localhost:8080");

        //let fileInput = document.getElementById("codelist-file");
        let nameInput = document.getElementById("project-name");

        let projectName = nameInput.value;

        let newProjectForm = {
            nome: projectName,
        };

        let response = await serverRequester.doPost(
            "/project/create",
            newProjectForm
        );

        if (response["responseJson"] === true) {
            notification("success", "Oba!", "Projeto criado com sucesso!");
        } else {
            notification(
                "error",
                "Ops...",
                "Não foi possível criar o projeto. Tente novamente."
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

    /**
     * Função que cria a página
     * @author Bárbara Port
     * @returns Página de criar um novo projeto
     */
    getNewProjectScreen() {
        let newProjectScreen = (
            <div id="contentDisplay" className="w-full h-full">
                {getBackgroundImage()}
                <div
                    id="newProjectScreen"
                    className="w-full h-full flex justify-center items-center"
                >
                    <div className="bg-white lg:w-5/12 md:w-10/12 h-5/6 relative flex justify-start text-center flex-col border-r-8 border-b-8 border-accent">
                        <div className="m-8">
                            <p className="text-2xl font-bold">
                                Crie um novo projeto de manual
                            </p>
                            <hr></hr>
                            <p className="text-lg mt-4">
                                Esta sessão irá lhe auxiliar na criação de um
                                novo projeto de manual, preencha os campos e
                                siga as instruções!
                            </p>
                        </div>
                        <form onSubmit={this.sendFormData}>
                            <div className="m-10">
                                <label
                                    htmlFor="project-name"
                                    className="text-lg"
                                >
                                    Nome:{" "}
                                </label>
                                <input
                                    type="text"
                                    id="project-name"
                                    className="border-b border-black focus:bg-gray-200 outline-none"
                                    placeholder="XXX-YYYY"
                                ></input>
                                <Tooltip text="O nome deve ser: 3 Numeros - 4 Letras. Não pode existir um outro manual com o mesmo nome." />
                            </div>
                            <div className="m-8">
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

                                <p className="text-xs ml-12 mr-12 mt-4">
                                    A escolha de um Codelist no momento da
                                    criação não é obrigatória, ele pode ser
                                    importado ou criado no gerenciamento do
                                    documento.
                                </p>
                            </div>
                            <div className="mt-20">
                                <Button text="Criar" type="confirm"></Button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        );

        return newProjectScreen;
    }

    /**
     * Renderiza a página de criação de um novo projeto
     * @returns Elemento a ser renderizado
     * @author Bárbara Port
     */
    render() {
        return this.getNewProjectScreen();
    }
}

export default NewProjectScreen;
