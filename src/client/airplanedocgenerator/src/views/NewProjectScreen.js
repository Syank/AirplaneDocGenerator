import React from "react";

import Button from "../assets/components/Button";
import Tooltip from "../assets/components/Tooltip";
import Loader from "../assets/components/Loader";

import { getBackgroundImage } from "../utils/pagesUtils";
import { notification } from "../assets/components/Notifications";

import ServerRequester from "../utils/ServerRequester";

/**
 * Classe que representa a página de criação de um projeto
 * @author Bárbara Port
 */
class NewProjectScreen extends React.Component {
    constructor(props) {
        super(props);

        this.isValidPartLetter = this.isValidPartLetter.bind(this);
        this.isValidPartNumber = this.isValidPartNumber.bind(this);
        this.sendFormData = this.sendFormData.bind(this);

        this.state = {
            loading: true,
            loadingCreation: true,
        };
    }

    /**
     * Criar um novo projeto
     *
     * @param {Event} event
     * @author Rafael Furtado
     */
    async sendFormData(event) {
        this.setState({ loadingCreation: true });
        event.preventDefault();

        let serverRequester = new ServerRequester("http://localhost:8080");

        //let fileInput = document.getElementById("codelist-file");
        let nameInput = document.getElementById("project-name");

        let projectName = nameInput.value;

        let partLetter = projectName.split("-")[0];
        let partNumber = projectName.split("-")[1];

        if (
            partLetter === undefined ||
            partNumber === undefined ||
            !this.isValidPartLetter(partLetter) ||
            !this.isValidPartNumber(partNumber)
        ) {
            notification(
                "error",
                "Nome de projeto inválido! 😵",
                "O formato do nome de projetos devem iguais ao seguinte exemplo: ABC-1234"
            );
        } else {
            let newProjectForm = {
                nome: projectName,
            };

            let response = await serverRequester.doPost(
                "/project/create",
                newProjectForm
            );

            if (response["responseJson"] === true) {
                notification(
                    "success",
                    "Oba! 😄",
                    "Projeto criado com sucesso!"
                );
            } else {
                notification(
                    "error",
                    "Ops...",
                    "Não foi possível criar o projeto. Tente novamente."
                );
            }
        }

        this.setState({ loadingCreation: false });
    }

    /**
     * Verifica se a parte de números do nome de um projeto contêm apenas números
     *
     * @param {String} supposedPartNumber String da parte de números do nome de um projeto
     * @author Rafael Furtado
     */
    isValidPartNumber(supposedPartNumber) {
        if (
            supposedPartNumber === undefined ||
            supposedPartNumber.length !== 4 ||
            supposedPartNumber.includes(".")
        ) {
            return false;
        }

        let isNumber = +supposedPartNumber;

        if (isNaN(isNumber)) {
            return false;
        }

        return true;
    }

    /**
     * Verifica se a parte de letras de um nome de projeto contêm apenas letras
     *
     * @param {String} supposedPartLetter String da parte de letras do nome do projeto
     * @author Rafael Furtado
     */
    isValidPartLetter(supposedPartLetter) {
        if (
            supposedPartLetter === undefined ||
            !isNaN(supposedPartLetter) ||
            supposedPartLetter.length !== 3
        ) {
            return false;
        }

        let regex = /[a-zA-Z]/;

        let letters = supposedPartLetter.split("");

        for (let i = 0; i < letters.length; i++) {
            const letter = letters[i];

            if (!regex.test(letter)) {
                return false;
            }
        }

        return true;
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
                                <Tooltip
                                    id="codelistNameInput"
                                    text="O nome deve ter o seguinte formato: 3 letras - 4 números. Ex.: ABC-1234. Não é possível utilizar o mesmo nome em mais de um projeto."
                                />
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
                                {this.getCreateButton()}
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        );

        return newProjectScreen;
    }

    /**
     * Retorna botão caso não esteja carregando a criação de um projeto
     *
     * @author Carolina Margiotti
     */
    getCreateButton() {
        if (this.state.loadingCreation) {
            return <Loader />;
        }
        return <Button text="Criar" type="confirm"></Button>;
    }

    /**
     * Constrói o Loader de pagina
     *
     * @returns Retorna o Loader de pagina.
     * @author Carolina Margiotti
     */
    getLoaderScreen() {
        let loaderScreen = (
            <div id="contentDisplay" className="w-full h-full">
                {getBackgroundImage()}

                <div
                    id="loaderScreen"
                    className="w-full h-full flex flex-col items-center justify-center relative select-none"
                >
                    <Loader />
                </div>
            </div>
        );

        return loaderScreen;
    }

    /**
     * É invocado imediatamente após um elemento ser montado
     *
     * @returns Desliga o loading após a pagina ser totalmente montada.
     * @author Carolina Margiotti
     */
    componentDidMount() {
        this.setState({ loading: false, loadingCreation: false });
    }

    /**
     * Renderiza a página de criação de um novo projeto
     * @returns Elemento a ser renderizado
     * @author Bárbara Port
     */
    render() {
        let loaderScreen = this.getLoaderScreen();

        if (this.state.loading) {
            return loaderScreen;
        }

        return this.getNewProjectScreen();
    }
}

export default NewProjectScreen;
