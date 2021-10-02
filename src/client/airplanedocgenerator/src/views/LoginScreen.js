import React from "react";

// Imagens usadas
import loginImg from "../assets/misc/images/loginImage.png";

// Componente dos ícones do FontAwesome
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";

// Ícones do FontAwesome
import { faUser } from "@fortawesome/free-solid-svg-icons";
import { faKey } from "@fortawesome/free-solid-svg-icons";

import Button from "../assets/components/Button";
import Loader from "../assets/components/Loader";

import { getBackgroundImage } from "../utils/pagesUtils";
import { notification } from "../assets/components/Notifications";

import ServerRequester from "../utils/ServerRequester";

/**
 * Uma classe de "view" que representa a tela de login da aplicação
 *
 * @author Rafael Furtado
 */
class LoginScreen extends React.Component {
    constructor(props) {
        super(props);

        this.userIcon = <FontAwesomeIcon icon={faUser} color={"#5E74D6"} />;
        this.passwordIcon = <FontAwesomeIcon icon={faKey} color={"#5E74D6"} />;

        this.iconColor = "#5E74D6";

        this.inputStyle =
            "border-b-2 outline-none text-center focus:bg-gray-200 ml-3";

        this.userLoginInputId = "userLogin";
        this.userPasswordInputId = "userPassword";

        this.userLoginFormName = "userLogin";
        this.userPasswordFormName = "userPassword";

        this.state = {
            loading: true,
        };

        this.login = this.login.bind(this);
    }

    /**
     * Chama pelo método recebido do componente pai para navegar a aplicação para a página inicial (home)
     *
     * @author Rafael Furtado
     */
    goToHomePage() {
        this.props.navigation("home");
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
    async authenticateUser(userLogin, userPassword) {
        let serverRequester = new ServerRequester("http://localhost:8080");

        let userCredentials = {
            email: userLogin,
            password: userPassword,
        };

        let response = await serverRequester.doPost(
            "/authentication/login",
            userCredentials
        );

        if (response["responseJson"] === true) {
            return true;
        }

        return false;
    }

    /**
     * Função responsável por chamar a cadeia de eventos que irão realizar a autenticação e redirecionamento
     * da página ao realizar o login
     *
     * @param {Event} event Evento passado como parâmetro para a função quando o formulário é submetido
     * @author Rafael Furtado
     */
    async login(event) {
        // Impede o formulário de ser enviado automáticamente
        event.preventDefault();

        try {
            let userLogin = document.getElementById(this.userLoginInputId).value;
            let userPassword = document.getElementById(
                this.userPasswordInputId
            ).value;

            let authenticated = await this.authenticateUser(
                userLogin,
                userPassword
            );

            if (authenticated) {
                let isAdmin = await this.isUserAdmin();

                this.props.setUserLoggedState(true);
                this.props.setUserLoggedType(isAdmin);

                this.goToHomePage();
            } else {
                notification(
                    "error",
                    "Ops!",
                    "Verifique o e-mail e a senha e tente novamente!"
                );
            }
        }
        catch (exception) {
            notification("error", "Ops!", "Não há conexão com o servidor. Entre em contato com a administração.  😵");
        }
    }

    async isUserAdmin() {
        let serverRequester = new ServerRequester("http://localhost:8080");

        let response = await serverRequester.doGet(
            "/authentication/isUserAdmin"
        );

        if (response["responseJson"] === true) {
            return true;
        }

        return false;
    }

    /**
     * Constrói e configura o cabeçalho do formulário de login
     *
     * @returns Retorna o cabeçalho do formulário de login
     * @author Rafael Furtado
     */
    getLoginHeader() {
        let loginHeader = (
            <div className="flex flex-col items-center pt-8">
                <img
                    src={loginImg}
                    alt="Login"
                    className="w-20"
                    onDragStart={this.preventImageDrag}
                />

                <label className="mb-4 mt-4 text-accent text-2xl">
                    <b>Login</b>
                </label>

                <label className="text-center ml-2 mr-2">
                    Insira seus dados para acessar a plataforma
                </label>
            </div>
        );

        return loginHeader;
    }

    /**
     * Constrói e configura o formulário de login
     *
     * @returns Retorna o formulário de login
     * @author Rafael Furtado
     */
    getLoginForm() {
        let loginForm = (
            <form
                className="flex flex-col items-center mt-5"
                onSubmit={this.login}
            >
                <div className="relative right-3">
                    <FontAwesomeIcon icon={faUser} color={this.iconColor} />
                    <input
                        id={this.userLoginInputId}
                        type="text"
                        name={this.userLoginFormName}
                        className={this.inputStyle}
                        placeholder="Usuário"
                    />
                </div>
                <div className="relative right-3">
                    <FontAwesomeIcon icon={faKey} color={this.iconColor} />
                    <input
                        id={this.userPasswordInputId}
                        type="password"
                        name={this.userPasswordFormName}
                        className={"mb-5 mt-4 " + this.inputStyle}
                        placeholder="Senha"
                    />
                </div>

                <Button text="Entrar" type="confirm"></Button>
            </form>
        );

        return loginForm;
    }

    /**
     * Constrói e organiza os elementos que compoêm a página de login
     *
     * @returns Retorna a página de login para ser renderizada
     * @author Rafael Furtado
     */
    getLoginScreen() {
        let loginScreen = (
            <div id="contentDisplay" className="w-full h-full">
                {getBackgroundImage()}

                <div
                    id="loginScreen"
                    className="w-full h-full flex flex-col items-center justify-center relative select-none"
                >
                    {this.getLoginComponents()}
                </div>
            </div>
        );

        return loginScreen;
    }

    /**
     * Constrói o Loader de pagina
     *
     * @returns Retorna o Loader de pagina caso esteja carregando a pagina.
     * @author Carolina Margiotti
     */
    getLoginComponents() {
        if(this.state.loading){
            return <Loader />
        }
        else{
            return (
            <div
                id="loginForm"
                className="w-loginFormW h-loginFormH bg-white shadow-loginFormShadow"
            >
                {this.getLoginHeader()}
                {this.getLoginForm()}
            </div>)
        }
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
     * Renderiza a página de login na tela
     *
     * @returns Retorna o elemento a ser renderizado na janela
     * @author Rafael Furtado
     */
    render() {
        let loginScreen = this.getLoginScreen();
        return loginScreen;
    }
}

// Permite a exportação do componente
export default LoginScreen;
