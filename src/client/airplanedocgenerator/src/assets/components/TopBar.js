import React from "react";

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'

// Icones do FontAwesome
import { faArrowLeft } from '@fortawesome/free-solid-svg-icons'
import { faHome } from '@fortawesome/free-solid-svg-icons'
import { faBars } from '@fortawesome/free-solid-svg-icons'
import { faMinus } from '@fortawesome/free-solid-svg-icons'
import { faTimes } from '@fortawesome/free-solid-svg-icons'

import { white } from "tailwindcss/colors";
import RegisterUser from "./RegisterUser";
import TopBarMenu from "./TopBarMenu";



/**
 * Classe do componente TopBar
 * 
 * A TopBar é responsável por fornecer ao usuário o controle da janela, como arrastar, minimizar ou fechar a aplicação e, 
 * caso esteja logado, acesso ao menu, voltar a página inicial ou à anterior
 * 
 * @author Rafael Furtado
 */
class TopBar extends React.Component{
    constructor(props){
        super(props);

        this.returnIcon = <FontAwesomeIcon icon={faArrowLeft} color={white}/>;
        this.homeIcon = <FontAwesomeIcon icon={faHome} color={white}/>;
        this.menuIcon = <FontAwesomeIcon icon={faBars} color={white}/>;
        this.closeIcon = <FontAwesomeIcon icon={faMinus} color={white}/>;
        this.minimizeIcon = <FontAwesomeIcon icon={faTimes} color={white}/>;

        // Como todos os botões são iguais, declarei aqui para evitar repetição de código
        this.iconBoxStyle = "h-full w-auto flex flex-row items-center text-20px pr-3 pl-3 "
                          + "hover:bg-hoverTopBarButton "
                          + "active:bg-activeTopBarButton";

        this.setRegisterUserState = this.setRegisterUserState.bind(this);
        this.returnToHomePage = this.returnToHomePage.bind(this);
        this.returnToPreviousPage = this.returnToPreviousPage.bind(this);
        this.toggleTopBarMenu = this.toggleTopBarMenu.bind(this);
        this.minimizeApplication = this.minimizeApplication.bind(this);

        this.state = {
            showRegisterUser: false, 
            showMenu: false
        };

    }

    /**
     * Fecha a aplicação
     * 
     * @author Rafael Furtado
     */
    closeApplication(){
        window.electron.windowControll.close();

    }

    /**
     * Minimiza a aplicação
     * 
     * @author Rafael Furtado
     */
    minimizeApplication(){
        window.electron.windowControll.minimize();

    }

    /**
     * Abre o menu de funcionalidades extras disponíveis ao usuário
     * 
     * @author Rafael Furtado
     */
    toggleTopBarMenu(){
       this.setState({showMenu: !this.state["showMenu"]});

    }

    /**
     * Retorna a página inicial da aplicação
     * 
     * @author Rafael Furtado
     */
    returnToHomePage(){
        this.props.navigation("home");

    }

    /**
     * Retorna para a página anterior
     * 
     * @author Rafael Furtado
     */
    returnToPreviousPage(){
        this.props.returnToPreviousPage();

    }

    /**
     * Função de controle para exibir ou esconder o componente de registro de usuários
     * 
     * @param {Boolean} show Valor booleano para decidir se deve ou não exibir a tela de registro de usuários
     * @author Rafael Furtado
     */
    setRegisterUserState(show){
        this.setState({showRegisterUser: show});

    }

    /**
     * Retorna os componentes extras para a tob bar acessíveis apenas quando o usuário está logado na aplicação
     * 
     * @returns Retorna funcionalidades extras para a top bar para um usuário logado
     * @author Rafael Furtado
     */
    getUserLoggedFunctions(){
        let userLoggedFunctions =
        <div className="flex flex-row items-center h-full w-auto">
            {this.state["showMenu"] && 
                <TopBarMenu adminOptions={this.props.userLoggedType} 
                            loggoutFunction={this.props.loggoutFunction}
                            setShowRegisterUser={this.setRegisterUserState}/>
            }

            <div className={this.iconBoxStyle}
                onClick={this.toggleTopBarMenu}>
                {this.menuIcon}
            </div>
            <div className={this.iconBoxStyle}
                onClick={this.returnToHomePage}>
                {this.homeIcon}
            </div>
            <div className={this.iconBoxStyle}
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
        let topBarBox = (
            <div>
                <div className="bg-topBar w-screen h-8 flex justify-between items-center shadow-topBarShadow">
                    <div className="h-full w-auto flex flex-row">
                    {// Caso tenha um usuário logado, chama a função que retorna os botões das funcionalidades extras
                        this.props["userLoggedState"] && 
                            this.getUserLoggedFunctions()
                    }
                    </div>

                    <div className="h-full w-auto flex flex-row">
                        <div className={this.iconBoxStyle}
                            onClick={this.minimizeApplication}>
                            {this.closeIcon}
                        </div>

                        <div className={this.iconBoxStyle}
                            onClick={this.closeApplication}>
                            {this.minimizeIcon}
                        </div>
                    </div>
                </div>

                {
                this.state.showRegisterUser &&
                    <RegisterUser control={this.setRegisterUserState}></RegisterUser>
                }
            </div>
        );

        return topBarBox;
    }

    /**
     * Verifica se existe um usuário logado na aplicação
     * 
     * @returns Caso exista um usuário logado, retorna true, se não, retorna false
     * @author Rafael Furtado
     */
    isUserLogged(){
        let isLogged = sessionStorage.getItem("isLogged");

        if(isLogged === null){
            return false;
        }

        return isLogged;
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
        let comp = this.getTopBar();

        return comp;
    }

}

// Permite a exportação do componente
export default TopBar;
