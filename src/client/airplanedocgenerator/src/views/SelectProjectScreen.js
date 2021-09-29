import {
    faLongArrowAltLeft,
    faLongArrowAltRight,
    faSearch,
} from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import React from "react";
import Button from "../assets/components/Button";
import CardHeader from "../assets/components/CardHeader";
import { notification } from "../assets/components/Notifications";
import SelectProjectItem from "../assets/components/SelectProjectItem";
import { getBackgroundImage } from "../utils/pagesUtils";
import ServerRequester from "../utils/ServerRequester";
import Loader from "../assets/components/Loader";

/**
 * Classe de componente que representa a tela de listagem e seleção de manuais no sistema
 *
 * @author Rafael Furtado
 */
class SelectProjectScreen extends React.Component {
    constructor(props) {
        super(props);

        this.headerCardTitle = "Gerir documento";
        this.headerCardText =
            "Escolha um manual na lista para editar sua composição ou excluí-lo da plataforma";

        this.state = {
            selectedProject: {
                nome: "Selecione um manual",
                descricao:
                    "Selecione um manual na lista para ver mais detalhes e informações sobre ele aqui",
            },
            projectsList: [[]],
            searchList: [[]],
            originalProjectsList: [[]],
            page: 1,
            search: "",
            loading: true,
            loadingProjectList: true,
            loadingProjectSelect: true,
        };

        this.setSelectedItem = this.setSelectedItem.bind(this);
        this.deleteProject = this.deleteProject.bind(this);
        this.nextPage = this.nextPage.bind(this);
        this.previousPage = this.previousPage.bind(this);
        this.search = this.search.bind(this);
    }

    /**
     * Função do ciclo de vida dos componentes do React, será chamado apenas uma vez, quando o
     * componente for renderizado na tela
     *
     * @author Rafael Furtado
     */
    async componentDidMount() {
        let projects = await this.getProjecsList();

        let projectsList = [];

        let projectsByPage = [];

        for (let i = 0; i < projects.length; i++) {
            const project = projects[i];

            if (projectsByPage.length < 5) {
                projectsByPage.push(project);
            } else {
                projectsList.push(projectsByPage);

                projectsByPage = [project];
            }
        }

        if (projectsByPage.length > 0) {
            projectsList.push(projectsByPage);
        }

        this.setState({
            projectsList: projectsList,
            originalProjectsList: projects,
            loadingProjectList: false,
            loadingProjectSelect: false,
            loading: false,
        });
    }

    /**
     * Faz uma requisição ao servidor para obter a lista de todos os projetos
     *
     * @returns Retorna a lista de projetos no sistema
     * @author Rafael Furtado
     */
    async getProjecsList() {
        let serverRequester = new ServerRequester("http://localhost:8080");

        let response = await serverRequester.doGet("/project/all");

        let projectsList = [];

        if (response["responseJson"] === false) {
            notification(
                "error",
                "Algo deu errado 🙁",
                "Não foi possível carregar a lista de projetos"
            );

            return projectsList;
        }

        projectsList = response["responseJson"];

        return projectsList;
    }

    /**
     * Constrói adequadamente o componente do cabeçalho de título da página
     *
     * @returns Retorna o cabeçalho de título da página
     * @author Carolina Margiotti
     */
    getHeaderCard() {
        let headerCard = (
            <CardHeader
                title={this.headerCardTitle}
                description={this.headerCardText}
            ></CardHeader>
        );

        return headerCard;
    }

    /**
     * Constrói adequadamente o componente que representa a página de exibição da lista de manuais
     *
     * @returns Retorna o componente que representa a página de exibição da lista de manuais
     * @author Carolina Margiotti
     */
    getSelectProjectScreen() {
        let selectProjectScreen = (
            <div id="contentDisplay" className="w-full h-full">
                {getBackgroundImage()}

                <div
                    id="selectProjectScreen"
                    className="w-full h-full flex flex-col items-center justify-center relative select-none"
                >
                    <div className="w-full h-2/4 flex flex-row justify-center items-start">
                        {this.getHeaderCard()}
                    </div>
                    <div className="w-full h-full flex flex-row items-center justify-evenly">
                        {this.getSelectContainer()}
                    </div>
                </div>
            </div>
        );

        return selectProjectScreen;
    }

    /**
     * Constrói a seção onde será exibido a lista de manuais e suas informações
     *
     * @returns Retorna o container onde será exibido a lista de manuais no sistema
     * @author Rafael Furtado
     */
    getSelectContainer() {
        let container;
        if (this.state.loadingProjectSelect) {
            container = (
                <div className="text-xl w-selectProjectW h-selectProjectH bg-white flex flex-row items-center px-6 py-2 shadow-2xl">
                    {this.getListSide()}
                    <div className="bg-gray-400 h-full mt-8 mb-8 w-1"></div>
                    <Loader />
                </div>
            );
        } else {
            container = (
                <div className="text-xl w-selectProjectW h-selectProjectH bg-white flex flex-row items-center px-6 py-2 shadow-2xl">
                    {this.getListSide()}
                    <div className="bg-gray-400 h-full mt-8 mb-8 w-1"></div>
                    {this.getInformationSide()}
                </div>
            );
        }

        return container;
    }

    /**
     * Constrói a lista em si de onde os manuais serão exibidos para serem selecionados
     *
     * @returns Retorna o lado do container de manuais onde está a lista de manuais
     * @author Rafael Furtado
     */
    getListSide() {
        let container = (
            <div className="w-full h-full flex flex-col justify-between pr-5">
                <div className="mb-5 border-b-2 border-gray-400 h-10 w-full flex flex-row items-center justify-center">
                    <label>Manuais na plataforma</label>
                </div>
                <div className="w-full h-full">{this.getPopulateList()}</div>
                <div className="h-12 w-full flex flex-row justify-between pt-3 pl-2 pr-2">
                    <FontAwesomeIcon
                        className="cursor-pointer"
                        icon={faLongArrowAltLeft}
                        onClick={this.previousPage}
                    />

                    {this.getPaginationElement()}

                    <FontAwesomeIcon
                        className="cursor-pointer"
                        icon={faLongArrowAltRight}
                        onClick={this.nextPage}
                    />
                </div>
            </div>
        );

        return container;
    }

    /**
     * Retorna lista caso não esteja carregando ainda
     *
     * @author Carolina Margiotti
     */
    getPopulateList() {
        if (this.state.loadingProjectList) {
            return <Loader />;
        }
        return <div className="w-full h-full">{this.populateList()}</div>;
    }

    /**
     * Retorna para a página anterior na lista de seleção de manuais
     *
     * @author Rafael Furtado
     */
    previousPage() {
        this.setState({ loadingProjectList: true });
        let actualPage = this.state["page"];

        if (actualPage > 1) {
            this.setState({ page: actualPage - 1 });
        }
        this.setState({ loadingProjectList: false });
    }

    /**
     * Avança para a próxima página na lista de seleção de manuais
     *
     * @author Rafael Furtado
     */
    nextPage() {
        this.setState({ loadingProjectList: true });

        let actualPage = this.state["page"];
        let maxPage = this.state["projectsList"].length;

        if (actualPage < maxPage) {
            this.setState({ page: actualPage + 1 });
        }
        this.setState({ loadingProjectList: false });
    }

    /**
     * Realiza a busca de manuais de acordo com o texto escrito no input de busca
     *
     * Após a busca, o componente será renderizado novamente para exibir o resultado
     */
    search() {
        this.setState({ loadingProjectSelect: true });
        let searchInput = document.getElementById("projectSearch");

        let valueToSearch = searchInput.value;

        let searchList = this.getSearchList(valueToSearch);

        this.setState({
            search: valueToSearch,
            projectsList: searchList,
            page: 1,
            loadingProjectSelect: false,
        });
    }

    /**
     * Utilizando o texto de busca, filtra a lista de manuais a serem exibidos
     *
     * @param {String} valueToSearch Valor digitado no input de pesquisa
     * @returns Retorna uma nova lista de manuais a serem exibidos de acordo com o parâmetro de busca
     * @author Rafael Furtado
     */
    getSearchList(valueToSearch) {
        let projectsList = [];

        let projectsByPage = [];

        let originalList = this.state["originalProjectsList"];

        for (let i = 0; i < originalList.length; i++) {
            const project = originalList[i];
            const projectName = project["nome"];

            if (
                valueToSearch !== "" &&
                !projectName.includes(valueToSearch.toUpperCase())
            ) {
                continue;
            }

            if (projectsByPage.length < 5) {
                projectsByPage.push(project);
            } else {
                projectsList.push(projectsByPage);

                projectsByPage = [project];
            }
        }

        if (projectsByPage.length > 0) {
            projectsList.push(projectsByPage);
        }

        return projectsList;
    }

    /**
     * Constrói o lado do compononte onde estará disponível dados e utilidades para gerir a lista de manuais
     *
     * @returns Retorna o lado do container de manuais no qual será exibido os campos de busca e informações
     * @author Rafael Furtado
     */
    getInformationSide() {
        let container = (
            <div className=" w-full h-full flex flex-col items-center justify-between pt-5 pb-5">
                <div>
                    <input
                        id="projectSearch"
                        type="text"
                        placeholder="Pesquisar por nome"
                        className="text-center outline-none border-b-2 mr-2"
                    ></input>
                    <FontAwesomeIcon
                        onClick={this.search}
                        icon={faSearch}
                        className="cursor-pointer"
                    ></FontAwesomeIcon>
                </div>
                <div className="w-full h-full mt-5 mb-5 flex flex-col items-center text-center">
                    <label>{this.state["selectedProject"]["nome"]}</label>
                    <p className="mt-5 text-sm">
                        {this.state["selectedProject"]["descricao"]}
                    </p>
                </div>
                <Button
                    text="Selecionar"
                    type="confirm"
                    onClick={this.goToProjectView}
                ></Button>
            </div>
        );

        return container;
    }

    /**
     * Vai para a página de edição/gerenciamento do manual selecionado
     *
     * @author Rafael Furtado
     */
    goToProjectView() {
        notification(
            "info",
            "Um momento! 🤔",
            "Este recurso ainda não está disponível no momento"
        );
    }

    /**
     * Constrói o componente de rodapé onde exibi a quantia de páginas de manuais que podem ser vistos
     *
     * @returns Retorna o componente que serve de páginação da lista de manuais
     * @author Rafael Furtado
     */
    getPaginationElement() {
        let maxPage = this.state["projectsList"].length;

        let pagination = (
            <label className="text-sm">
                Página {this.state["page"]} de {maxPage}
            </label>
        );

        return pagination;
    }

    /**
     * Através da variável de estado "page", define quais serão os manuais que deverão ser exibidos para a página
     *
     * @returns Retorna a lista de manuais que serão exibidas para a página em questão
     */
    getProjectsToShow() {
        let page = this.state["page"] - 1;

        let projectsToShow = this.state["projectsList"][page];

        return projectsToShow;
    }

    /**
     * Constrói os componentes no qual o usuário pode interagir e ver qual manual representa
     *
     * @returns Retorna os componentes que representam os manuais a serem exibidos na lista
     * @author Rafael Furtado
     */
    populateList() {
        let projects = [];

        let projectsToShow = this.getProjectsToShow();

        for (let i = 0; i < projectsToShow.length; i++) {
            const projectData = projectsToShow[i];

            const projectName = projectData["nome"];

            let element = (
                <SelectProjectItem
                    key={projectName}
                    projectName={projectName}
                    setSelectedItem={this.setSelectedItem}
                    deleteProject={this.deleteProject}
                ></SelectProjectItem>
            );

            projects.push(element);
        }

        return projects;
    }

    /**
     * Seleciona o manual em razão de seu nome, para exibir mais informações sobre ele
     *
     * @param {String} name Nome do manual a ser selecionado
     * @author Rafael Furtado
     */
    setSelectedItem(name) {
        let project = this.getProjectDataByName(name);

        this.setState({ selectedProject: project });
    }

    /**
     * Deleta o manual do sistema
     *
     * @param {String} name Nome do manual a ser deletado
     * @author Rafael Furtado
     */
    deleteProject(name) {
        notification(
            "info",
            "Um momento! 🤔",
            "Este recurso ainda não está disponível no momento"
        );
    }

    /**
     * Através do nome, retorna as informações do manual a serem exibidas ao usuário
     *
     * @param {String} name Nome do manual a ter as informações acessadas
     * @returns Retorna as informações do manual requisitado
     * @author Rafael Furtado
     */
    getProjectDataByName(name) {
        let projectsLists = this.state["projectsList"];

        for (let i = 0; i < projectsLists.length; i++) {
            const list = projectsLists[i];

            for (let x = 0; x < list.length; x++) {
                const project = list[x];

                if (project["nome"] === name) {
                    return project;
                }
            }
        }
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
     * Método obrigatório herdado da classe React.Component
     *
     * Renderiza a página inicial da aplicação
     *
     * @returns Retorna o elemento a ser renderizado na janela
     * @author Carolina Margiotti
     */
    render() {
        let selectProjectScreen = this.getSelectProjectScreen();
        let loaderScreen = this.getLoaderScreen();

        if (this.state.loading) {
            return loaderScreen;
        }
        return selectProjectScreen;
    }
}

export default SelectProjectScreen;
