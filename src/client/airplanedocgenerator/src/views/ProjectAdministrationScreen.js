import React from "react";
import CardHeader from "../assets/components/CardHeader";
import { notification } from "../assets/components/Notifications";
import { getBackgroundImage, isValidProjectName } from "../utils/pagesUtils";
import ServerRequester from "../utils/ServerRequester";
import Button from "../assets/components/Button";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faCheck, faFileAlt, faPen, faTimes } from "@fortawesome/free-solid-svg-icons";
import CodelistManager from "../assets/components/CodelistManager";


class ProjectAdministrationScreen extends React.Component{
    constructor(props) {
        super(props);

        this.projectName = window.sessionStorage.getItem("selectedProject");

        this.projectNameInputId = "projectNameInput";
        this.projectDescriptionInputId = "textAreaDescription";

        this.state = {
            projectData: {
                situation: {
                    situationTitle: "",
                    situationMessage: "",
                    ok: false
                }
            },
            projectVariations: {},
            editingProjectName: false,
            editingProjectDescription: false,
            showCodelist: false,
            codelistFilter: "all"
        };

        this.headerCardTitle = "Administração do projeto";
        this.headerCardText = "Administre o projeto do manual, " 
            + "visualizando a codelist completa ou a individual de cada variação, crie novas linhas na codelist e mais";

        this.toggleEditProjectName = this.toggleEditProjectName.bind(this);
        this.changeProjectName = this.changeProjectName.bind(this);
        this.componentDidMount = this.componentDidMount.bind(this);
        this.toggleEditProjectDescription = this.toggleEditProjectDescription.bind(this);
        this.changeProjectDescription = this.changeProjectDescription.bind(this);
        this.showCodelist = this.showCodelist.bind(this);
        this.hideCodelistManager = this.hideCodelistManager.bind(this);
        this.loadProjectData = this.loadProjectData.bind(this);

    }

    async componentDidMount(){
        await this.loadProjectData();

    }

    async loadProjectData(){
        let serverRequester = new ServerRequester("http://localhost:8080");
        
        let requestParameters = {
            projectName: this.projectName
        };

        let response = await serverRequester.doGet("/project/findByName", requestParameters);

        if(response["ok"] === true){
            let projectVariations = this.getVariations(response["responseJson"]);

            this.setState({
                projectData: response["responseJson"],
                projectVariations: projectVariations,
                editingProjectName: false,
                editingProjectDescription: false
            });

            return response["responseJson"];

        }else{
            notification("error", "Algo deu errado 🙁", 
                "Não foi possível carregar as informações do projeto, você será redirecionado para a página de escolha de projetos");
            
            this.props.navigation("selectProject");
            
        }

    }

    getVariations(projectData){
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

    getDescriptionContainer(){
        let container = (
            <div className="flex flex-col w-1/3 pr-3 pt-1 pb-1 h-full justify-between">
                <div className="pb-1 border-b-2 border-black border-opacity-50">
                   {this.getProjectNameBox()}
                </div>
                    {this.getProjectDescriptionBox()}
                <div className="border-t-2 pt-2 border-black border-opacity-50 flex flex-col items-center text-sm">
                   <Button text="Codelist" type="confirm" onClick={this.showCodelist}></Button>
                   <Button text="Revisões" type="confirm" onClick={this.showRevisions}></Button>
                </div>
            </div>
        );

        return container;
    }

    getCodelistRemarkToShow(event){
        let filterCriteria = event.target.parentElement.getAttribute("attributeName");

        if(filterCriteria === null){
            filterCriteria = event.target.getAttribute("attributeName");

            if(filterCriteria === null){
                filterCriteria = "all";
    
            }

        }

        return filterCriteria;
    }

    showCodelist(event){
        let filterCriteria = this.getCodelistRemarkToShow(event);

        this.setState({
            codelistFilter: filterCriteria,
            showCodelist: true
        });

    }

    showRevisions(){
        notification("info", "Aguarde um pouco! 🤓", "Essa funcionalidade estará disponível em breve!");

    }

    getProjectDescriptionBox(){
        let editing = this.state["editingProjectDescription"];
        
        let container;

        if(editing){
            container = (
            <div className="text-center h-full mt-5">
                <div className="w-full flex flex-row justify-end mb-3">
                    <FontAwesomeIcon onClick={this.changeProjectDescription} className="cursor-pointer mr-3" icon={faCheck} color={"#18cb26"}/>
                    <FontAwesomeIcon onClick={this.toggleEditProjectDescription} className="cursor-pointer" icon={faTimes} color={"#ef2c2c"}/>
                </div>
                <textarea id={this.projectDescriptionInputId} className="text-sm w-full h-3/4 overflow-hidden text-center resize-none" maxLength="160" placeholder={this.state["projectData"]["descricao"]}></textarea>
            </div>
            );
        }else{
            container = (
                <div className="text-center h-full mt-5">
                    <div className="w-full flex flex-row justify-end mb-3">
                        <FontAwesomeIcon onClick={this.toggleEditProjectDescription} className="cursor-pointer" icon={faPen} color={"#5E74D6"}/>
                    </div>
                    <p className="text-sm">{this.state["projectData"]["descricao"]}</p>
                </div>
            );
        }

        return container;
    }

    async changeProjectDescription(){
        let projectDescriptionInput = document.getElementById(this.projectDescriptionInputId);

        let newProjectDescription = projectDescriptionInput.value;

        let serverRequester = new ServerRequester("http://localhost:8080");

        let data = {
            projectName: this.projectName,
            projectDescription: newProjectDescription
        };

        let response = await serverRequester.doPost("/project/changeDescription", data);

        if(response["responseJson"] === true){
            notification("success", "Sucesso! 😄", "A descrição do projeto foi alterada com sucesso!");

            // Lê novamente os dados do projeto para atualizar a página adequadamente
            await this.loadProjectData();

        }else{
            notification("error", "Ops 🙁", "Algo deu errado durante a alteração da descrição do projeto");

        }

    }

    toggleEditProjectDescription(){
        let state = this.state["editingProjectDescription"];

        this.setState({editingProjectDescription: !state})
    }

    getProjectNameBox(){
        let editing = this.state["editingProjectName"];

        let component;

        if(editing){
            component = (
                <div className="flex flex-row justify-center items-center">
                    <input id={this.projectNameInputId} className="mr-2 w-24" maxLength="8" placeholder={this.projectName}></input>
                    <FontAwesomeIcon onClick={this.changeProjectName} className="cursor-pointer mr-3" icon={faCheck} color={"#18cb26"}/>
                    <FontAwesomeIcon onClick={this.toggleEditProjectName} className="cursor-pointer" icon={faTimes} color={"#ef2c2c"}/>
                </div>
            );
        }else{
            component = (
                <div className="flex flex-row justify-center items-center">
                    <label className="mr-2">{this.projectName}</label>
                    <FontAwesomeIcon onClick={this.toggleEditProjectName} className="cursor-pointer" icon={faPen} color={"#5E74D6"}/>
                </div>
            );
        }

        return component;
    }

    async changeProjectName(){
        let projectNameInput = document.getElementById(this.projectNameInputId);

        let newProjectName = projectNameInput.value;

        let validName = isValidProjectName(newProjectName);

        if(!validName){
            notification(
                "error",
                "Nome de projeto inválido! 😵",
                "O formato do nome de projetos devem iguais ao seguinte exemplo: ABC-1234"
            );
        }else{
            let serverRequester = new ServerRequester("http://localhost:8080");

            let data = {
                oldName: this.projectName,
                newName: newProjectName
            };

            let response = await serverRequester.doPost("/project/changeName", data);

            if(response["responseJson"] === true){
                notification("success", "Sucesso! 😄", "O nome do projeto foi alterado com sucesso!");

                this.projectName = newProjectName.toUpperCase();

                // Lê novamente os dados do projeto para atualizar a página adequadamente
                await this.loadProjectData();

            }else{
                notification("error", "Ops 🙁", "Algo deu errado enquanto o nome do projeto era alterado");

            }

        }

    }

    toggleEditProjectName(){
        let state = this.state["editingProjectName"];

        this.setState({editingProjectName: !state});

    }

    getVariationsToList(){
        let variationsElements = [];

        let variationsList = this.state["projectVariations"];

        let keys;

        if(variationsList !== undefined){
            keys = Object.keys(variationsList);

        }else{
            keys = [];

        }
        
        for (let i = 0; i < keys.length; i++) {
            const key = keys[i];
            
            let apelido = variationsList[key];

            let element = (
                <div key={apelido} className="flex flex-row border-b-2 border-black border-opacity-50 mb-2 pr-1 pl-1 items-center justify-between">
                    <div className="flex flex-col">
                        <label>{this.projectName + "-" + key}</label>
                        <label>{"(" + apelido + ")"}</label>
                    </div>
                    <FontAwesomeIcon attributeName={key} className="text-2xl cursor-pointer" onClick={this.showCodelist} icon={faFileAlt} color={"#5E74D6"}/>
                </div>

            );

            variationsElements.push(element);
        }

        return variationsElements;
    }

    getVariationsContainer(){
        let container = (
            <div className="flex flex-col w-1/3 ml-3 mr-3 pt-1 pb-1 h-full">
                <div className="flex flex-row justify-center pb-1 border-b-2 border-black border-opacity-50">
                   <label>Variações</label>
                </div>
                <div className="text-sm pt-4 h-full overflow-auto">
                    {this.getVariationsToList()}
                </div>
            </div>
        );

        return container;
    }

    getInformationsContainer(){
        let situationTitleColor;

        if(this.state["projectData"]["situation"]["ok"]){
            situationTitleColor = "text-green-600";

        }else{
            situationTitleColor = "text-red-600";

        }
        
        let container = (
            <div className="flex flex-col w-1/3 pl-3 pt-1 pb-1 h-full justify-between">
                <div className="flex flex-row justify-center pb-1 border-b-2 border-black border-opacity-50">
                   <label>{"Situação"}</label>
                </div>
                <div className="text-center h-full pt-5">
                    <label className={"font-bold " + situationTitleColor}>{this.state["projectData"]["situation"]["situationTitle"]}</label>
                    <p className="text-sm mt-5">{this.state["projectData"]["situation"]["situationMessage"]}</p>
                </div>
                <div className="border-t-2 pt-2 border-black border-opacity-50 flex flex-col items-center text-sm">
                   <Button text="Exportar" type="confirm" onClick={this.exportProject}></Button>
                </div>
            </div>
        );

        return container;
    }

    exportProject(){
        notification("info", "Aguarde um pouco! 🤓", "Essa funcionalidade estará disponível em breve!");

    }

    getProjectAdministrationContainer(){
        let container = (
            <div className="text-xl w-10/12 h-selectProjectH bg-white flex flex-row items-center px-6 py-2 shadow-2xl justify-evenly">
                {this.getDescriptionContainer()}
                <div className="border-black border-opacity-40 border-l-2 h-full mt-8 mb-8"></div>
                {this.getVariationsContainer()}
                <div className="border-black border-opacity-40 border-l-2 h-full mt-8 mb-8"></div>
                {this.getInformationsContainer()}
            </div>
        );

        return container;
    }

    async hideCodelistManager(){
        this.setState({showCodelist: false});

        await this.loadProjectData();

    }

    getAdministrationScreen(){
        let selectProjectScreen = (
            <div id="contentDisplay" className="w-full h-full">
                {getBackgroundImage()}
                {this.state["showCodelist"] && 
                    <CodelistManager 
                        reloadData={this.loadProjectData}
                        projectData={this.state["projectData"]}
                        filter={this.state["codelistFilter"]}
                        hide={this.hideCodelistManager}/>
                }
                <div
                    id="projectAdministrationScreen"
                    className="w-full h-full flex flex-col items-center justify-center relative select-none"
                >
                    <div className="w-full h-2/4 flex flex-row justify-center items-start">
                        {this.getHeaderCard()}
                    </div>
                    <div className="w-full h-full flex flex-row items-center justify-evenly">
                        {this.getProjectAdministrationContainer()}
                    </div>
                </div>
            </div>
        );

        return selectProjectScreen;
    }

    /**
     * Renderiza a página de criação de um novo projeto
     * @returns Elemento a ser renderizado
     * @author Rafael Furtado
     */
     render() {
         let  administrationScreen = this.getAdministrationScreen();

        return administrationScreen;
    }

}

export default ProjectAdministrationScreen;