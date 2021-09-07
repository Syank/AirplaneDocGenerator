import React from "react";

import aviao from '../assets/misc/images/cloud.jpg';

import CardHeader from "../assets/components/CardHeader";
import CardOption from "../assets/components/CardOption";



class HomeScreen extends React.Component {
    constructor() {
        super();

        this.headerCardTitle = "Bem vindo";
        this.headerCardText = "Faça alguma coisa para ativar suas configurações de não sei o que computador tela janela cama '-'"

        this.cardOptionTexts = {
            newProject: "Crie um novo projeto de manual a partir da importação de um já existente ou siga os passos do guia e crie um do zero",
            editProject: "Selecione um manual da lista de administre suas informações, crie revisões e edite suas partes",
            printProject: "Selecione um manual da lista e gere os PDFs de suas versões DELTA e FULL"
        }

        this.cardOptionButtonsText = {
            newProject: "Criar",
            editProject: "Administrar",
            printProject: "Gerar"
        }
        
    }

        /**
     * Previne que imagens sejam "arrastáveis"
     * 
     * Esta função deve ser passada para o atributo "onDragStart" do elemento
     * 
     * @param {Event} event Evento recebido como parâmetro quando a ação de arrastar é iniciada pelo usuário
     * @author Rafael Furtado
     */
         preventImageDrag(event){
            event.preventDefault();
    
        }
    
    /**
     * Constrói e configura o elemento para ser a imagem de fundo da tela de login
     * 
     * @returns Retorna o elemento da imagem de fundo da tela de login
     * @author Rafael Furtado
     */
     getBackgroundImage(){
        let backgroundImage = 
            <img src={aviao} alt="Avião" 
                onDragStart={this.preventImageDrag} 
                className="w-full h-full select-none absolute filter blur-blurLogin opacity-20"/>
        
        return backgroundImage;
    }

    getHeaderCard(){
        let headerCard = (
            <CardHeader title={this.headerCardTitle} description={this.headerCardText}></CardHeader>
        );

        return headerCard;
    }

    goToCreateNewProjectPage(){
        console.log("Indo para página de criar novo projeto");
    }

    goToEditProjectPage(){
        console.log("Indo para página de editar projeto");
    }

    goToPrintProjectPage(){
        console.log("Indo para página de gerar documento");
    }

    getCardOption(cardText, buttonText, buttonOnClick, iconName){
        let cardOption = (
            <CardOption cardText={cardText} buttonOnClick={buttonOnClick} 
                        buttonText={buttonText} icon={iconName}></CardOption>
        );

        return cardOption;
    }

    getHomeScreen(){
        let cardOption1 = this.getCardOption(this.cardOptionTexts["newProject"], this.cardOptionButtonsText["newProject"], this.goToCreateNewProjectPage, "file");
        let cardOption2 = this.getCardOption(this.cardOptionTexts["editProject"], this.cardOptionButtonsText["editProject"], this.goToEditProjectPage, "edit");
        let cardOption3 = this.getCardOption(this.cardOptionTexts["printProject"], this.cardOptionButtonsText["printProject"], this.goToPrintProjectPage, "print");

        let homeScreen = (
            <div id="contentDisplay" className="w-full h-full">
                {this.getBackgroundImage()}

                <div id="homeScreen" className="w-full h-full flex flex-col items-center justify-center relative select-none">
                    <div className="w-full h-2/4 flex flex-row justify-center items-start">
                        {this.getHeaderCard()}
                    </div>
                    <div className="w-full h-full flex flex-row items-center justify-evenly">
                        {cardOption1}
                        {cardOption2}
                        {cardOption3}
                    </div>
                </div>
            </div>
        );

        return homeScreen;
    }

    render(){
        let homeScreen = this.getHomeScreen();

        return homeScreen;
    }

}

// Permite a exportação do componente
export default HomeScreen;