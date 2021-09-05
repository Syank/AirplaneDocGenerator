import React from "react";

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'

// Icones do FontAwesome
import { faArrowLeft } from '@fortawesome/free-solid-svg-icons'
import { faHome } from '@fortawesome/free-solid-svg-icons'
import { faBars } from '@fortawesome/free-solid-svg-icons'
import { faMinus } from '@fortawesome/free-solid-svg-icons'
import { faTimes } from '@fortawesome/free-solid-svg-icons'
import { white } from "tailwindcss/colors";

/**
 * Classe do componente TopBar
 * 
 * A TopBar é responsável por fornecer ao usuário o controle da janela, como arrastar, minimizar ou fechar a aplicação e, 
 * caso esteja logado, acesso ao menu, voltar a página inicial ou à anterior
 * 
 * @author Rafael Furtado
 */
class TopBar extends React.Component{
    constructor(){
        super();

        this.returnIcon = <FontAwesomeIcon icon={faArrowLeft} color={white}/>;
        this.homeIcon = <FontAwesomeIcon icon={faHome} color={white}/>;
        this.menuIcon = <FontAwesomeIcon icon={faBars} color={white}/>;
        this.closeIcon = <FontAwesomeIcon icon={faMinus} color={white}/>;
        this.minimizeIcon = <FontAwesomeIcon icon={faTimes} color={white}/>;

        // Como todos os botões são iguais, declarei aqui para evitar repetição de código
        this.iconBoxStyle = "h-full w-auto flex flex-row items-center text-20px pr-3 pl-3 "
                          + "hover:bg-hoverTopBarButton "
                          + "active:bg-activeTopBarButton";

    }

    /**
     * Fecha a aplicação
     * 
     * @author Rafael Furtado
     */
    closeApplication(){
        console.log("Fechando aplicação");

    }

    /**
     * Minimiza a aplicação
     * 
     * @author Rafael Furtado
     */
    minimizeApplication(){
        console.log("Minimizando aplicação");

    }

    /**
     * Abre o menu de funcionalidades extras disponíveis ao usuário
     * 
     * @author Rafael Furtado
     */
    openTopBarMenu(){
        console.log("Abrindo menu da top bar");

    }

    /**
     * Retorna a página inicial da aplicação
     * 
     * @author Rafael Furtado
     */
    returnToHomePage(){
        console.log("Voltando para a página inicial");

    }

    /**
     * Retorna para a página anterior
     * 
     * @author Rafael Furtado
     */
    returnToPreviousPage(){
        console.log("Voltando para página anterior");

    }

    /**
     * Retorna os componentes extras para a tob bar acessíveis apenas quando o usuário está logado na aplicação
     * 
     * @returns Retorna funcionalidades extras para a top bar para um usuário logado
     * @author Rafael Furtado
     */
    getUserLoggedFunctions(){
        let userLoggedFunctions =
        <div class="flex flex-row items-center h-full w-auto">
            <div class={this.iconBoxStyle}
                onClick={this.openTopBarMenu}>
                {this.menuIcon}
            </div>
            <div class={this.iconBoxStyle}
                onClick={this.returnToHomePage}>
                {this.homeIcon}
            </div>
            <div class={this.iconBoxStyle}
                onClick={this.returnToPreviousPage}>
                {this.returnIcon}
            </div>
        </div>

        return userLoggedFunctions;
    }

    /**
     * Constroí o componente da top bar em si, contendo sempre os botões de controle da janela e, caso um usuário 
     * esteja logado na aplicação, retorna também as funcionalidades extras
     * 
     * @returns Retorna a top bar em si para ser renderizada
     * @author Rafael Furtado
     */
    getTopBar(){
        let userLogger = this.isUserLogged();

        let topBarBox = 
            <div class="bg-topBar w-screen h-8 flex justify-between items-center">
                <div class="h-full w-auto flex flex-row">
                {// Caso tenha um usuário logado, chama a função que retorna os botões das funcionalidades extras
                    userLogger && 
                        this.getUserLoggedFunctions()
                }
                </div>

                <div class="h-full w-auto flex flex-row">
                    <div class={this.iconBoxStyle}
                        onClick={this.minimizeApplication}>
                        {this.closeIcon}
                    </div>

                    <div class={this.iconBoxStyle}
                        onClick={this.closeApplication}>
                        {this.minimizeIcon}
                    </div>
                </div>
            </div>;

        return topBarBox;
    }

    /**
     * Verifica se existe um usuário logado na aplicação
     * 
     * @returns Caso exista um usuário logado, retorna true, se não, retorna false
     * @author Rafael Furtado
     */
    isUserLogged(){
        return false;
    }

    /**
     * Método obrigatório herdado da classe React.Component
     * 
     * Renderiza o componente na janela
     * 
     * @returns Retorna o elemento a ser renderizado na janela
     * @author Rafael Furtado
     */
    render(){
        return this.getTopBar();
    }

}

// Permite a exportação do componente
export default TopBar;
