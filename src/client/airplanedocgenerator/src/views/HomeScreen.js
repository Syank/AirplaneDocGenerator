import React from "react";

import CardHeader from "../assets/components/CardHeader";
import CardOption from "../assets/components/CardOption";
import Loader from "../assets/components/Loader";

import { notification } from "../assets/components/Notifications";

import { getBackgroundImage } from "../utils/pagesUtils";

/**
 * Uma classe de "view" que representa a tela de login da aplicação
 *
 * @author Rafael Furtado
 */
class HomeScreen extends React.Component {
    constructor() {
        super();

        this.headerCardTitle = "Bem-vindo";
        this.headerCardText =
            "Na página inicial você tem acesso a todos os serviços para criar, compor e gerar um manual, que podem ser acessados através de seus respectivos caminhos logo abaixo";

        this.cardOptionTexts = {
            newProject:
                "Crie um novo projeto de manual a partir da importação de um já existente ou siga os passos do guia e crie um do zero",
            editProject:
                "Selecione um manual da lista de administre suas informações, crie revisões e edite suas partes",
            printProject:
                "Selecione um manual da lista e gere os PDFs de suas versões DELTA e FULL",
        };

        this.cardOptionButtonsText = {
            newProject: "Criar",
            editProject: "Administrar",
            printProject: "Gerar",
        };

        this.goToCreateNewProjectPage =
            this.goToCreateNewProjectPage.bind(this);

        this.goToSelectProjectPage = this.goToSelectProjectPage.bind(this);

        this.state = {
            loading: true,
        };
    }

    /**
     * Constrói adequadamente o componente do cabeçalho de título da página
     *
     * @returns Retorna o cabeçalho de título da página
     * @author Rafael Furtado
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
     * Chama pelo método recebido do componente pai para navegar a aplicação para a página de
     * criação de um novo projeto de manual
     *
     * @author Carolina Margiotti
     */
    goToCreateNewProjectPage() {
        this.props.navigation("creation-screen");
    }

    /**
     * Chama pelo método recebido do componente pai para navegar a aplicação para a página de
     * edição de projetos/manuais
     *
     * @author Rafael Furtado
     */
    goToSelectProjectPage() {
        this.props.navigation("selectProject");

        //notification("info", "Aguarde um pouco!", "Essa funcionalidade estará disponível em breve!");
    }

    /**
     * Chama pelo método recebido do componente pai para navegar a aplicação para a página
     * de geração de manuais
     *
     * @author Rafael Furtado
     */
    goToPrintProjectPage() {
        notification(
            "info",
            "Aguarde um pouco!",
            "Essa funcionalidade estará disponível em breve!"
        );
    }

    /**
     * Constrói o componente do card de opção com as informações passadas como parâmetro
     *
     * @param {String} cardText Texto de exibição do card de opção
     * @param {String} buttonText Texto do botão do card
     * @param {Function} buttonOnClick Função a ser chamada no evento onClick do botão do card
     * @param {String} iconName Nome do ícone a ser exibido no card
     * @returns Retorna o componente do card de opção
     * @author Rafael Furtado
     */
    getCardOption(cardText, buttonText, buttonOnClick, iconName) {
        let cardOption = (
            <CardOption
                cardText={cardText}
                buttonOnClick={buttonOnClick}
                buttonText={buttonText}
                icon={iconName}
            ></CardOption>
        );

        return cardOption;
    }

    /**
     * Constrói adequadamente o componente que representa a página home em si
     *
     * @returns Retorna o componente que representa a página home
     * @author Rafael Furtado
     */
    getHomeScreen() {
        let homeScreen = (
            <div id="contentDisplay" className="w-full h-full">
                {getBackgroundImage()}

                <div
                    id="homeScreen"
                    className="w-full h-full flex flex-col items-center justify-center relative select-none"
                >
                    {this.getHomeScreenComponents()}
                </div>
            </div>
        );

        return homeScreen;
    }

     /**
     * Retorna componentes da pagina Home caso não esteja carregando, caso esteja mostra Loader.
     *
     * @returns Retorna componentes da HomeScreen ou Loader.
     * @author Carolina Margiotti
     */
    getHomeScreenComponents(){
        if(this.state.loading){
            return <Loader />
        }

        let cardOption1 = this.getCardOption(
            this.cardOptionTexts["newProject"],
            this.cardOptionButtonsText["newProject"],
            this.goToCreateNewProjectPage,
            "file"
        );
        let cardOption2 = this.getCardOption(
            this.cardOptionTexts["editProject"],
            this.cardOptionButtonsText["editProject"],
            this.goToSelectProjectPage,
            "edit"
        );
        let cardOption3 = this.getCardOption(
            this.cardOptionTexts["printProject"],
            this.cardOptionButtonsText["printProject"],
            this.goToPrintProjectPage,
            "print"
        );

        return (
        <div>
            <div className="w-full h-2/4 flex flex-row justify-center items-start">
                {this.getHeaderCard()}
            </div>
            <div className="w-full h-full flex flex-row items-center justify-evenly">
                {cardOption1}
                {cardOption2}
                {cardOption3}
            </div>
        </div>)
    }


    /**
     * É invocado imediatamente após um elemento ser montado
     *
     * @returns Desliga o loading após a pagina ser totalmente montada.
     * @author Carolina Margiotti
     */
    componentDidMount() {
            this.setState({ loading: false });
    }

    /**
     * Método obrigatório herdado da classe React.Component
     *
     * Renderiza a página inicial da aplicação
     *
     * @returns Retorna o elemento a ser renderizado na janela
     * @author Rafael Furtado
     */
    render() {
        let homeScreen = this.getHomeScreen();
        return homeScreen;
    }
}

// Permite a exportação do componente
export default HomeScreen;
