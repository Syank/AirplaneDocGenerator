import React from "react";

// Imagens usadas
import aviao from '../assets/misc/images/aviao.jpg';
import loginImg from "../assets/misc/images/loginImage.png";

// Componente dos ícones do FontAwesome
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'

// Ícones do FontAwesome
import { faUser } from '@fortawesome/free-solid-svg-icons'
import { faKey } from '@fortawesome/free-solid-svg-icons'



/**
 * Uma classe de "view" que representa a tela de login da aplicação
 * 
 * @author Rafael Furtado
 */
class LoginScreen extends React.Component{
    constructor(){
        super();

        this.userIcon = <FontAwesomeIcon icon={faUser} color={"#5E74D6"}/>;
        this.passwordIcon = <FontAwesomeIcon icon={faKey} color={"#5E74D6"}/>;

        this.iconColor = "#5E74D6";

        this.inputStyle = "border-b-2 outline-none text-center focus:bg-gray-200 ml-3";

        this.userLoginInputId = "userLogin";
        this.userPasswordInputId = "userPassword";

        this.userLoginFormName = "userLogin";
        this.userPasswordFormName = "userPassword";

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
     * Função responsável por realizar a autenticação do usuário
     * 
     * @param {String} userLogin Login do usuário
     * @param {String} userPassword Senha do usuário
     * @returns Caso o usuário seja autenticado com sucesso pelo servidor, returna true, se não, 
     *          retorna false
     * @author Rafael Furtado
     */
    authenticateUser(userLogin, userPassword) {
        console.log("Realizando login");

        return true;
    }

    /**
     * Função responsável por chamar a cadeia de eventos que irão realizar a autenticação e redirecionamento
     * da página ao realizar o login
     * 
     * @param {Event} event Evento passado como parâmetro para a função quando o formulário é submetido
     * @author Rafael Furtado
     */
    login(event){
        // Impede o formulário de ser enviado automáticamente
        event.preventDefault();
        
        let userLogin = document.getElementById(this.userLoginInputId).value;
        let userPassword = document.getElementById(this.userPasswordInputId).value;

        let authenticated = this.authenticateUser(userLogin, userPassword);

        if(authenticated){
            console.log("Redirecionando para página home");

        }else{
            console.log("Não foi possível realizar o login");

        }

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
                class="w-full h-full select-none absolute filter blur-blurLogin opacity-60"/>
        
        return backgroundImage;
    }

    /**
     * Constrói e configura o cabeçalho do formulário de login
     * 
     * @returns Retorna o cabeçalho do formulário de login
     * @author Rafael Furtado
     */
    getLoginHeader(){
        let loginHeader =
            <div class="flex flex-col items-center pt-8">
                <img src={loginImg} alt="Login" class="w-20" onDragStart={this.preventImageDrag}/>

                <label class="mb-4 mt-4 text-accent text-2xl"><b>Login</b></label>

                <label class="text-center ml-2 mr-2">Insira seus dados para acessar a plataforma</label>
            </div>

        return loginHeader;
    }

    /**
     * Constrói e configura o formulário de login
     * 
     * @returns Retorna o formulário de login
     * @author Rafael Furtado
     */
    getLoginForm(){
        let loginForm = 
            <form class="flex flex-col items-center mt-5" onSubmit={this.login}>
                <div class="relative right-3">
                    <FontAwesomeIcon icon={faUser} color={this.iconColor}/>
                    <input id={this.userLoginInputId} type="text" name={this.userLoginFormName} class={this.inputStyle} placeholder="Usuário"/>
                </div>
                <div class="relative right-3">
                    <FontAwesomeIcon icon={faKey} color={this.iconColor}/>
                    <input id={this.userPasswordInputId} type="password" name={this.userPasswordFormName} class={"mb-5 mt-4 " + this.inputStyle} placeholder="Senha"/>
                </div>
                <button class="bg-green-500 text-white shadow-simpleShadow pb-2 pt-2 pl-10 pr-10 select-none hover:bg-green-400 active:bg-green-600 focus:bg-green-400 outline-none">Entrar</button>
            </form>

        return loginForm;
    }

    /**
     * Constrói e organiza os elementos que compoêm a página de login
     * 
     * @returns Retorna a página de login para ser renderizada
     * @author Rafael Furtado
     */
    getLoginScreen(){
        let loginScreen =
            <div id="contentDisplay" class="w-full h-full">
                {this.getBackgroundImage()}

                <div id="loginScreen" class="w-full h-full flex flex-col items-center justify-center relative select-none">
                    <div id="loginForm" class="w-loginFormW h-loginFormH bg-white shadow-loginFormShadow">
                        {this.getLoginHeader()}
                        {this.getLoginForm()}
                    </div>
                </div>
            </div>
        
        return loginScreen;
    }

    /**
     * Método obrigatório herdado da classe React.Component
     * 
     * Renderiza a página de login na tela
     * 
     * @returns Retorna o elemento a ser renderizado na janela
     * @author Rafael Furtado
     */
    render(){
        let loginScreen = this.getLoginScreen();
            
        return loginScreen;
    }

}

// Permite a exportação do componente
export default LoginScreen;