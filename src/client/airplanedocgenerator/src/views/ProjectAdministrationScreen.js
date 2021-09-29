import React from "react";
import CardHeader from "../assets/components/CardHeader";
import { notification } from "../assets/components/Notifications";
import { getBackgroundImage } from "../utils/pagesUtils";
import ServerRequester from "../utils/ServerRequester";


class ProjectAdministrationScreen extends React.Component{
    constructor(props) {
        super(props);

        this.projectName = window.sessionStorage.getItem("selectedProject");

        this.state = {
            projectData: {}
        };

        this.headerCardTitle = "Administração do projeto";
        this.headerCardText = "Administre o projeto do manual, " 
            + "visualizando a codelist completa ou a individual de cada variação, crie novas linhas na codelist e mais";

    }

    async componentDidMount(){
        let serverRequester = new ServerRequester("http://localhost:8080");
        
        let requestParameters = {
            projectName: this.projectName
        }

        let response = await serverRequester.doGet("/project/findByName", requestParameters);

        if(response["ok"] === true){
            this.setState({projectData: response["responseJson"]});

        }else{
            notification("error", "Algo deu errado 🙁", 
                "Não foi possível carregar as informações do projeto, você será redirecionado para a página de escolha de projetos");;
            
            this.props.navigation("selectProject");
            
        }

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
            <div>

            </div>
        );

        return container;
    }

    getVariationsContainer(){
        let container = (
            <div>
                
            </div>
        );

        return container;
    }

    getInformationsContainer(){
        let container = (
            <div>
                
            </div>
        );

        return container;
    }

    getProjectAdministrationContainer(){
        let container = (
            <div className="text-xl w-10/12 h-selectProjectH bg-white flex flex-row items-center px-6 py-2 shadow-2xl justify-evenly">
                {this.getDescriptionContainer()}
                <div className="bg-gray-400 h-full mt-8 mb-8 w-1"></div>
                {this.getVariationsContainer()}
                <div className="bg-gray-400 h-full mt-8 mb-8 w-1"></div>
                {this.getInformationsContainer()}
            </div>
        );

        return container;
    }

    getAdministrationScreen(){
        let selectProjectScreen = (
            <div id="contentDisplay" className="w-full h-full">
                {getBackgroundImage()}

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
     * @author Bárbara Port
     */
     render() {
         let  administrationScreen = this.getAdministrationScreen();

        return administrationScreen;
    }
}

export default ProjectAdministrationScreen;