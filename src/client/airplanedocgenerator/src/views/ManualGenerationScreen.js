import { faLongArrowAltLeft, faLongArrowAltRight, faSearch } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import React from "react";
import Button from "../assets/components/Button";
import CardHeader from "../assets/components/CardHeader";
import { notification, withConfirmation } from "../assets/components/Notifications";
import { getBackgroundImage } from "../utils/pagesUtils";
import ServerRequester from "../utils/ServerRequester";



class ManualGenerationScreen extends React.Component {
    constructor(props) {
        super(props);

        this.headerCardTitle = "Geração de manuais";
        this.headerCardText = "Escolha um projeto de manual na lista e a sua variação para gerar os manuais FULL e DELTA";

        this.state = {
            selectedProject: {
                nome: "Selecione um manual",
                descricao: "Selecione um manual na lista para ver suas variações"
            },
            projectsList: [[]],
            searchList: [[]],
            originalProjectsList: [[]],
            page: 1,
            search: "",
            selectedVariation: undefined,
            selectedVariationName: ""
        }

        this.setSelectedItem = this.setSelectedItem.bind(this);
        this.deleteProject = this.deleteProject.bind(this);
        this.nextPage = this.nextPage.bind(this);
        this.previousPage = this.previousPage.bind(this);
        this.search = this.search.bind(this);
        this.generateFull = this.generateFull.bind(this);
        this.generateDelta = this.generateDelta.bind(this);
        this.getVariationsToList = this.getVariationsToList.bind(this);
        this.getVariationsContainer = this.getVariationsContainer.bind(this);
        this.getVariations = this.getVariations.bind(this);
        this.selectVariation = this.selectVariation.bind(this);

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
            notification("error", "Algo deu errado 🙁", "Não foi possível carregar a lista de projetos");

            return projectsList;
        }

        projectsList = response["responseJson"];

        return projectsList;
    }

    getHeaderCard() {
        let headerCard = (
            <CardHeader
                title={this.headerCardTitle}
                description={this.headerCardText}
            ></CardHeader>
        );

        return headerCard;
    }

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

    getSelectContainer(){
        let container = (
            <div className="text-xl w-3/5 h-selectProjectH bg-white flex flex-row items-center px-6 py-2 shadow-2xl">
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

        if (searchList.length === 0){
            notification("info", "Nenhum resultado 😮", "Nenhum projeto foi encontrado para essa pesquisa");
        
            return searchList;
        }
        else{
            this.setState({search: valueToSearch, projectsList: searchList, page: 1});
        }
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
                <div className="w-full h-full pr-5 pl-5 mt-5 mb-5 flex flex-col items-center text-center">
                    <label>{this.state["selectedProject"]["nome"]}</label>
                    {this.getVariationsContainer()}
                </div>
                <div className="w-full text-sm flex flex-col justify-center text-center">
                    <label>{this.state["selectedVariationName"]}</label>
                    <div className="w-full text-sm flex flex-row justify-center">
                        <Button text="Versão FULL" type="confirm" onClick={this.generateFull}></Button>
                        <Button text="Versão DELTA" type="confirm" onClick={this.generateDelta}></Button>
                    </div>
                </div>
            </div>
        );

        return container;
    }

    getVariations(projectData) {
        let codelist = projectData["codelist"];
        let linhas = codelist["linhas"];

        let variations = {};

        for (let i = 0; i < linhas.length; i++) {
            const linha = linhas[i];

            let remarks = linha["remarks"];

            for (let x = 0; x < remarks.length; x++) {
                const remark = remarks[x];

                let traco = remark["traco"];
                let apelido = remark["apelido"];

                variations[traco] = apelido;

            }

        }

        return variations;
    }

    getVariationsContainer() {
        let projectId = this.state["selectedProject"]["id"];

        let container;

        if(projectId !== undefined){
            container = (
                <div className="flex flex-col w-full ml-3 mr-3 pt-1 pb-1 h-full">
                    <div className="text-sm pt-4 w-full h-full overflow-auto">
                        {this.getVariationsToList()}
                    </div>
                </div>
            );
        }else{
            container = (
                    <p className="text-sm mt-6" >
                        Selecione um projeto para ver suas variações
                    </p>
            );
        }

        return container;
    }

    getVariationsToList() {
        let variationsElements = [];

        let variationsList = this.getVariations(this.state["selectedProject"]);
        let projectName = this.state["selectedProject"]["nome"];

        let keys;

        if (variationsList !== undefined) {
            keys = Object.keys(variationsList);

        } else {
            keys = [];

        }

        for (let i = 0; i < keys.length; i++) {
            const key = keys[i];

            let apelido = variationsList[key];

            let element = (
                <div key={apelido} className="flex flex-row border-b-2 border-black border-opacity-50 mb-2 pr-1 pl-1 items-center justify-between">
                    <div className="flex flex-row pl-5">
                        <label id={projectName + "-" + key + " (" + apelido + ")"} className="cursor-pointer hover:text-green-600" onClick={this.selectVariation}>
                            {projectName + "-" + key + " (" + apelido + ")"}</label>
                    </div>
                </div>

            );

            variationsElements.push(element);
        }

        return variationsElements;
    }

    selectVariation(event){
        let selectedVariation = event.target.id;
        
        let variation = selectedVariation.split("-")[2].split(" ")[0];

        this.setState({selectedVariation: variation, selectedVariationName: selectedVariation});

    }

    generateFull(){
        let selectedProject = this.state["selectedProject"];

        let projectId = selectedProject["id"];
        let variation = this.state["selectedVariation"];
        
        if(projectId === undefined || variation === undefined){
            notification("warning", "Um momento! 🤨", "Primeiro selecione um projeto e depois uma de suas variações");

        }else{
            // A notificação abaixo é apenas para teste, retirar e colocar a requisição
            notification("success", "Sucesso! 🤗", "A variação " + variation + " do projeto de ID " + projectId + " foi selecionado!");

        }

    }

    async generateDelta(){
        let selectedProject = this.state["selectedProject"];

        let projectId = selectedProject["id"];
        let variation = this.state["selectedVariation"];
        
        if(projectId === undefined || variation === undefined){
            notification("warning", "Um momento! 🤨", "Primeiro selecione um projeto e depois uma de suas variações");

        }
        else{

            notification("success", "Sucesso! 🤗", "A variação " + variation + " do projeto de ID " + projectId + " foi selecionado!");

            let formData = new FormData();
            formData.append("projectId", projectId);
            formData.append("variation", variation);

            let serverRequester = new ServerRequester("http://localhost:8080");
            let response = await serverRequester.doPost("/project/generateDelta", formData, "multipart/form-data");
            console.log(variation);
            //console.log(response);
        }

    }

    getPaginationElement(){
        let maxPage = this.state["projectsList"].length;

        let pagination = (
            <label className="text-sm">Página {this.state["page"]} de {maxPage}</label>
        );

        return pagination;
    }

    getProjectsToShow() {
        let page = this.state["page"] - 1;

        let projectsToShow = this.state["projectsList"][page];

        if (projectsToShow === undefined) {
            projectsToShow = [];

        }

        return projectsToShow;
    }

    populateList(){
        let projects = [];

        let projectsToShow = this.getProjectsToShow();

        for (let i = 0; i < projectsToShow.length; i++) {
            const projectData = projectsToShow[i];

            const projectName = projectData["nome"];
    
            let element = (
                <div className="flex flex-row justify-center pr-3 pl-3 border-b-2 border-gray-400 mb-5">
                    <label id={projectName} className="hover:text-green-600 cursor-pointer" onClick={this.setSelectedItem}>{projectName}</label>
                </div>
            );

            projects.push(element);

        }

        return projects;
    }

    setSelectedItem(event){
        let name = event.target.id;

        let project = this.getProjectDataByName(name);

        this.setState({selectedProject: project, selectedVariation: undefined, selectedVariationName: ""});

    }

    async deleteProject(projectName){
        let confirmation = await withConfirmation("Tem certeza que quer excluir o projeto " + projectName + "?",
                                                  "Ao confirmar, não será possível reverter a ação!",
                                                  "warning",
                                                  "Tenho certeza!",
                                                  "Ops! Não quero");
        
        if (confirmation === true) {

            let formData = new FormData();
            formData.append("projectName", projectName);

            let serverRequester = new ServerRequester("http://localhost:8080");
            let response = await serverRequester.doPost("/project/delete", formData, "multipart/form-data");

            if (response.status === 200) {
                notification("success", "Ufa! 😎", "O projeto foi deletado!");

                let projectsList = this.state["projectsList"][0];
                for (let i = 0; i < projectsList.length; i++) {
                    if (projectsList[i].nome === projectName) {
                        projectsList.splice(i, 1);
                    }
                }
                this.setState({projectsLists: projectsList});

            }
            else {
                notification("error", "Ops...", "Não foi possível apagar o projeto. Tente novamente. 🤕");
            }
        }
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

    render() {
        let selectProjectScreen = this.getSelectProjectScreen();

        return selectProjectScreen;
    }

}

export default ManualGenerationScreen;