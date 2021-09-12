import React from "react";
import TopBar from "./assets/components/TopBar";
import HomeScreen from "./views/HomeScreen";
import CreationScreen from "./views/CreationScreen";
import LoginScreen from "./views/LoginScreen";
import NewProjectScreen from "./views/NewProjectScreen";



/**
 * Classe de componente raiz da aplicação, todos os outros componentes deverão ser filhos desta classe
 *
 * @author Rafael Furtado
 */
class Application extends React.Component {
    constructor() {
        super();

        this.state = { pageToRender: "login" };

        this.setPageToRender = this.setPageToRender.bind(this);
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
            default:
                pageToDisplay = this.getLoginScreen();

                break;
        }

        return pageToDisplay;
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
            <LoginScreen navigation={this.setPageToRender}></LoginScreen>
        );

        return loginScreen;
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
                <TopBar></TopBar>

                {this.getPageToDisplay()}
            </div>
        );

        return applicationView;
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
