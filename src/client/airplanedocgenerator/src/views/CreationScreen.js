import React from "react";

import CardHeader from "../assets/components/CardHeader";
import CardOption from "../assets/components/CardOption";

import cloud from "../assets/misc/images/cloud.jpg";

/**
 * Uma classe de "view" que representa a tela de criação ou importação de manual
 *
 * @author Carolina Margiotti
 */
class CreationScreen extends React.Component {
    constructor() {
        super();

        this.headerCardTitle = "Criação de manual";
        this.headerCardText =
            "Aqui é possível criar um novo projeto de manual, seja seguindo o guia para criar um do zero ou fazendo a importação de uma estrutura válida direto de sua máquina";

        this.cardOptionTexts = {
            newProject:
                "Siga o passo a passo para a criação de um novo documento",
            importProject:
                "Escolha um diretório com estrutura válida em seu computador para importa-lo ",
        };

        this.cardOptionButtonsText = {
            newProject: "Criar Documento",
            importProject: "Importar Documento",
        };
    }

    /**
     * Previne que imagens sejam "arrastáveis"
     *
     * Esta função deve ser passada para o atributo "onDragStart" do elemento
     *
     * @param {Event} event Evento recebido como parâmetro quando a ação de arrastar é iniciada pelo usuário
     * @author Carolina Margiotti
     */
    preventImageDrag(event) {
        event.preventDefault();
    }

    /**
     * Constrói e configura o elemento para ser a imagem de fundo da tela de login
     *
     * @returns Retorna o elemento da imagem de fundo da tela de login
     * @author Carolina Margiotti
     */
    getBackgroundImage() {
        let backgroundImage = (
            <img
                src={cloud}
                alt="Cloud"
                onDragStart={this.preventImageDrag}
                className="w-full select-none absolute filter opacity-40 blur-blurLogin"
            />
        );

        return backgroundImage;
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
     * Chama pelo método recebido do componente pai para navegar a aplicação para a página de
     * criação de um novo documento de manual
     *
     * @author Carolina Margiotti
     */
    goToCreateNewProjectPage() {
        console.log("Indo para página de criar novo documento");
    }

    /**
     * Chama pelo método recebido do componente pai para navegar a aplicação para a página de
     * importação de projetos/manuais
     *
     * @author Carolina Margiotti
     */
    goToImportProjectPage() {
        console.log("Indo para página de importar documento");
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
     * Constrói adequadamente o componente que representa a página de criação de manual
     *
     * @returns Retorna o componente que representa a página home
     * @author Carolina Margiotti
     */
    getCreationScreen() {
        let cardOption1 = this.getCardOption(
            this.cardOptionTexts["newProject"],
            this.cardOptionButtonsText["newProject"],
            this.goToCreateNewProjectPage,
            "file"
        );
        let cardOption2 = this.getCardOption(
            this.cardOptionTexts["importProject"],
            this.cardOptionButtonsText["importProject"],
            this.goToImportProjectPage,
            "file-upload"
        );

        let creationScreen = (
            <div id="contentDisplay" className="w-full h-full">
                {this.getBackgroundImage()}

                <div
                    id="homeScreen"
                    className="w-full h-full flex flex-col items-center justify-center relative select-none"
                >
                    <div className="w-full h-2/4 flex flex-row justify-center items-start">
                        {this.getHeaderCard()}
                    </div>
                    <div className="w-full h-full flex flex-row items-center justify-evenly">
                        {cardOption1}
                        {cardOption2}
                    </div>
                </div>
            </div>
        );

        return creationScreen;
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
        let creationScreen = this.getCreationScreen();

        return creationScreen;
    }
}

// Permite a exportação do componente
export default CreationScreen;
