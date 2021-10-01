import React from "react";
import TopBar from "./assets/components/TopBar";
import HomeScreen from "./views/HomeScreen";
import CreationScreen from "./views/CreationScreen";
import LoginScreen from "./views/LoginScreen";
import NewProjectScreen from "./views/NewProjectScreen";
import ServerRequester from "./utils/ServerRequester";
import SelectProjectScreen from "./views/SelectProjectScreen";
import ProjectAdministrationScreen from "./views/ProjectAdministrationScreen";
import CodelistScreen from "./views/CodelistScreen";



/**
 * Classe de componente raiz da aplicação, todos os outros componentes deverão ser filhos desta classe
 *
 * @author Rafael Furtado
 */
class Application extends React.Component {
    constructor() {
        super();

        this.state = {
            pageToRender: "login",
            userLogged: false,
            isUserAdmin: false
        };

        this.previousPageMap = {
            "creation-screen": "home",
            "new-project-screen": "creation-screen",
            "selectProject": "home"
        };

        this.setPageToRender = this.setPageToRender.bind(this);
        this.setUserLoggedState = this.setUserLoggedState.bind(this);
        this.returnToPreviousPage = this.returnToPreviousPage.bind(this);
        this.setUserLoggedType = this.setUserLoggedType.bind(this);
        this.logoutUser = this.logoutUser.bind(this);

    }

    /**
     * Identifica, pelo atributo state.pageToRender, qual página deverá ser renderizada
     *
     * @returns Retorna o componente que representa a deverá ser renderizada
     * @author Rafael Furtado
     */
    getPageToDisplay() {
        let pageToDisplay;

        switch (this.state.pageToRender) {
            case "login":
                pageToDisplay = this.getLoginScreen();

                break;
            case "home":
                pageToDisplay = this.getHomeScreen();

                break;
            case "creation-screen":
                pageToDisplay = this.getCreationScreen();

                break;
            case "new-project-screen":
                pageToDisplay = this.getNewProjectScreen();

                break;
            case "selectProject":
                pageToDisplay = this.getSelectProjectScreen();
                
                break;
            case "projectAdministration":
                pageToDisplay = this.getProjectAdministrationScreen();

                break;
            case "codelistScreen":
                pageToDisplay = this.getCodelistScreen();

                break;
            default:
                pageToDisplay = this.getLoginScreen();

                break;
        }

        return pageToDisplay;
    }

    getSelectProjectScreen(){
        let screen = (
            <SelectProjectScreen navigation={this.setPageToRender}></SelectProjectScreen>
        );

        return screen
    }

    getProjectAdministrationScreen(){
        let screen = (
            <ProjectAdministrationScreen navigation={this.setPageToRender}></ProjectAdministrationScreen>
        );

        return screen;
    }

    getCodelistScreen(){
        let screen = (
            <CodelistScreen navigation={this.setPageToRender}></CodelistScreen>
        );

        return screen;
    }

    /**
     * Altera o atributo state.pageToRender para o nome da página que deverá ser renderizada
     *
     * A alteração do atributo automáticamente chama métodos internos do ciclo de vida do React que fazem
     * a atualização dos componentes na tela
     *
     * @param {String} pageName Nome da página que deverá ser renderizada
     * @author Rafael Furtado
     */
    setPageToRender(pageName) {
        this.setState({ pageToRender: pageName });

    }

    /**
     * Constrói o componente que representa a tela de login e passa para ele o método de navegação
     * do componente Application
     *
     * @returns Retorna o componente que representa a página de login
     * @author Rafael Furtado
     */
    getLoginScreen() {
        let loginScreen = (
            <LoginScreen navigation={this.setPageToRender} 
                         setUserLoggedState={this.setUserLoggedState}
                         setUserLoggedType={this.setUserLoggedType}></LoginScreen>
        );

        return loginScreen;
    }

    /**
     * Altera o estado do tipo de usuário logado
     * 
     * Esta função é usada pelo menu da topbar para decidir o que deverá ser exibido
     * 
     * @param {boolean} isAdmin Valor booleando determinando se o usuário logado é administrador ou não
     * @author Rafael Furtado
     */
    setUserLoggedType(isAdmin){
        this.setState({isUserAdmin: isAdmin});

    }

    /**
     * Constrói o componente que representa a tela de criação 1 e passa para ele o método de navegação
     * do componente Application
     *
     * @returns Retorna o componente que representa a página de criação 1
     * @author Carolina Margiotti
     *
     */
    getCreationScreen() {
        let creationScreen = (
            <CreationScreen navigation={this.setPageToRender}></CreationScreen>
        );

        return creationScreen;
    }

    /**
     * Constrói o componente da segunda tela de criação de um projeto
     * @returns Componente da segunda parte da criação do projeto
     * @author Bárbara Port
     */
    getNewProjectScreen () {
        let newProjectScreen = (
            <NewProjectScreen navigation={this.setPageToRender}></NewProjectScreen>
        );

        return newProjectScreen;
    }

    /**
     * Constrói o componente que representa a tela inicial (home) e passa para ele o método de navegação
     * do componente Application
     *
     * @returns Retorna o componente que representa a página home
     * @author Rafael Furtado
     */
    getHomeScreen() {
        let homeScreen = (
            <HomeScreen navigation={this.setPageToRender}></HomeScreen>
        );

        return homeScreen;
    }

    /**
     * Constrói o componente raiz da aplicação
     *
     * @returns Retorna o componente a ser renderizado na janela
     * @author Rafael Furtado
     */
    getApplicationView() {
        let applicationView = (
            <div className="App w-screen h-screen flex flex-col overflow-hidden">
                <TopBar navigation={this.setPageToRender} 
                        userLoggedState={this.state["userLogged"]}
                        returnToPreviousPage={this.returnToPreviousPage}
                        userLoggedType={this.state["isUserAdmin"]}
                        loggoutFunction={this.logoutUser}>
                </TopBar>

                {this.getPageToDisplay()}
            </div>
        );

        return applicationView;
    }

    /**
     * Realiza o logout do usuário do sistema e retorna para a página de login
     * 
     * @author Rafael Furtado
     */
    async logoutUser(){
        let serverRequester = new ServerRequester("http://localhost:8080");

        let response = await serverRequester.doGet("/authentication/logout");

        if(response["responseJson"] === true){
            console.log("Redirecionando para login");

        }else{
            console.log("O servidor está offline 😥\nVocê será redirecionado para a página de login");
        }

        this.setState({userLogged: false, isUserAdmin: false, pageToRender: "login"})

    }

    /**
     * Retorna para a página anterior
     * 
     * @author Rafael Furtado
     */
    returnToPreviousPage(){
        let actualPAge = this.state["pageToRender"];
        let previousPage = this.previousPageMap[actualPAge];

        if(previousPage !== undefined){
            this.setPageToRender(previousPage);

        }
        
    }

    /**
     * Altera do estado de se o usuário está logado ou não
     * 
     * @param {boolean} state Valor booleano para determinado se o usuário está logado ou não
     * @author Rafael Furtado
     */
    setUserLoggedState(state){
        this.setState({userLogged: state});

    }

    /**
     * Método obrigatório herdado da classe React.Component
     *
     * Renderiza a página raiz da aplicação
     *
     * @returns Retorna o elemento a ser renderizado na janela
     * @author Rafael Furtado
     */
    render() {
        let application = this.getApplicationView();

        return application;
    }
}

export default Application;
