import { faLongArrowAltLeft, faLongArrowAltRight, faSearch } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import React from "react";
import Button from "../assets/components/Button";
import CardHeader from "../assets/components/CardHeader";
import { notification } from "../assets/components/Notifications";
import SelectProjectItem from "../assets/components/SelectProjectItem";
import { getBackgroundImage } from "../utils/pagesUtils";
import ServerRequester from "../utils/ServerRequester";



/**
 * Uma classe de "view" que representa a tela de login da aplicação
 *
 * @author Rafael Furtado
 */
class SelectProjectScreen extends React.Component {
    // eslint-disable-next-line no-useless-constructor
    constructor(props) {
        super(props);

        this.headerCardTitle = "Gerir documento";
        this.headerCardText = "Escolha um manual na lista para editar sua composição ou excluí-lo da plataforma";

        this.state = {
            selectedProject: {
                nome: "Selecione um manual",
                descricao: "Selecione um manual na lista para ver mais detalhes e informações sobre ele aqui"
            },
            projectsList: [[]],
            searchList: [[]],
            originalProjectsList: [[]],
            page: 1,
            search: ""
        }

        this.setSelectedItem = this.setSelectedItem.bind(this);
        this.deleteProject = this.deleteProject.bind(this);
        this.nextPage = this.nextPage.bind(this);
        this.previousPage = this.previousPage.bind(this);
        this.search = this.search.bind(this);

    }

    async componentDidMount(){
        let projects = await this.getProjecsList();

        let projectsList = [];

        let projectsByPage = [];

        for (let i = 0; i < projects.length; i++) {
            const project = projects[i];
            
            if(projectsByPage.length < 5){
                projectsByPage.push(project);

            }else{
                projectsList.push(projectsByPage);

                projectsByPage = [project];

            }

        }

        if(projectsByPage.length > 0){
            projectsList.push(projectsByPage);

        }
        
        this.setState({projectsList: projectsList, originalProjectsList: projects});

    }

    async getProjecsList(){
        let serverRequester = new ServerRequester("http://localhost:8080");

        let response = await serverRequester.doGet("/project/all");

        let projectsList = []

        if(response["responseJson"] === false){
            notification("error", "Algo deu errado 🙁", "Não foi possível carregar a lista de projetos")

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
     * Constrói adequadamente o componente que representa a página de exibição de manual
     *
     * @returns Retorna o componente que representa a exibição de manual
     * @author Carolina Margiotti
     */
    getDisplayScreen() {
        let displayScreen = (
            <div id="contentDisplay" className="w-full h-full">
                {getBackgroundImage()}

                <div
                    id="homeScreen"
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

        return displayScreen;
    }

    getSelectContainer(){
        let container = (
            <div className="text-xl w-selectProjectW h-selectProjectH bg-white flex flex-row items-center px-6 py-2 shadow-2xl">
                {this.getListSide()}
                <div className="bg-gray-400 h-full mt-8 mb-8 w-1"></div>
                {this.getInformationSide()}
            </div>
        );

        return container;
    }

    getListSide(){
        let container = (
            <div className="w-full h-full flex flex-col justify-between pr-5">
                <div className="mb-5 border-b-2 border-gray-400 h-10 w-full flex flex-row items-center justify-center">
                    <label>Manuais na plataforma</label>
                </div>
                <div className="w-full h-full">
                    {this.populateList()}
                </div>
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

    previousPage(){
        let actualPage = this.state["page"];
        
        if(actualPage > 1){
            this.setState({page: actualPage - 1});

        }
    }

    nextPage(){
        let actualPage = this.state["page"];
        let maxPage = this.state["projectsList"].length;
        
        if(actualPage < maxPage){
            this.setState({page: actualPage + 1});

        }

    }

    search(){
        let searchInput = document.getElementById("projectSearch");

        let valueToSearch = searchInput.value;

        let searchList = this.getSearchList(valueToSearch);

        this.setState({search: valueToSearch, projectsList: searchList, page: 1});

    }

    getSearchList(valueToSearch){
        let projectsList = [];

        let projectsByPage = [];

        let originalList = this.state["originalProjectsList"];

        for (let i = 0; i < originalList.length; i++) {
            const project = originalList[i];
            const projectName = project["nome"];

            if (valueToSearch !== "" && !projectName.includes(valueToSearch.toUpperCase())) {
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

    getInformationSide(){
        let container = (
            <div className=" w-full h-full flex flex-col items-center justify-between pt-5 pb-5">
                <div>
                    <input id="projectSearch" type="text" placeholder="Pesquisar por nome" className="text-center outline-none border-b-2 mr-2"></input>
                    <FontAwesomeIcon onClick={this.search} icon={faSearch} className="cursor-pointer"></FontAwesomeIcon>
                </div>
                <div className="w-full h-full mt-5 mb-5 flex flex-col items-center text-center">
                    <label>{this.state["selectedProject"]["nome"]}</label>
                    <p className="mt-5 text-sm">{this.state["selectedProject"]["descricao"]}</p>
                </div>
                <Button text="Selecionar" type="confirm"></Button>
            </div>
        );

        return container;
    }

    getPaginationElement(){
        let maxPage = this.state["projectsList"].length;

        let pagination = (
            <label className="text-sm">Página {this.state["page"]} de {maxPage}</label>
        );

        return pagination;
    }

    getProjectsToShow(){
        let page = this.state["page"] - 1;

        let projectsToShow = this.state["projectsList"][page];

        return projectsToShow;
    }

    populateList(){
        let projects = [];

        let projectsToShow = this.getProjectsToShow();

        for (let i = 0; i < projectsToShow.length; i++) {
            const projectData = projectsToShow[i];

            const projectName = projectData["nome"];
    
            let element = (
                <SelectProjectItem key={projectName} 
                                   projectName={projectName} 
                                   setSelectedItem={this.setSelectedItem} 
                                   deleteProject={this.deleteProject}>
                </SelectProjectItem>
            );

            projects.push(element);

        }

        return projects;
    }

    setSelectedItem(name){
        let project = this.getProjectDataByName(name);

        this.setState({selectedProject: project});

    }

    deleteProject(name){
        console.log(name);

    }

    getProjectDataByName(name){
        let projectsLists = this.state["projectsList"];

        for (let i = 0; i < projectsLists.length; i++) {
            const list = projectsLists[i];
            
            for (let x = 0; x < list.length; x++) {
                const project = list[x];
                
                if(project["nome"] === name){
                    return project;
                }

            }

        }

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
        let displayScreen = this.getDisplayScreen();

        return displayScreen;
    }

}

export default SelectProjectScreen;