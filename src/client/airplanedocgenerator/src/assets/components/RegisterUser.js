import React from "react";
import Button from "./Button";

import SimpleInput from "./SimpleInput";

import ServerRequester from "../../utils/ServerRequester";
import { notification } from "./Notifications";



/**
 * Classe de componente que será utilizado quando o usuário requisitar a abertura do formulária para o
 * cadastro de novos usuários
 * 
 * @author Rafael Furtado
 */
class RegisterUser extends React.Component{
    constructor(props) {
        super(props);

        this.registerUserPath = "";

        this.id = "registerUser"

        this.userNameInputId = "newUserName";
        this.userEmailInputId = "newUserEmail";
        this.userPasswordInputId = "newUserPassword";

        this.userAdminCheckBoxId = "admin";

        this.close = this.close.bind(this);
        this.registerNewUser = this.registerNewUser.bind(this);

    }

    /**
     * Função chamada no momento da submissão do formulário de cadastro
     * 
     * @param {Event} event Evento da submissão do formulário
     * @author Rafael Furtado
     */
    async registerNewUser(event){
        event.preventDefault();

        let serverRequester = new ServerRequester();

        let userName = document.getElementById(this.userNameInputId).value;
        let userEmail = document.getElementById(this.userEmailInputId).value;
        let userPassword = document.getElementById(this.userPasswordInputId).value;

        let isAdmin = document.getElementById(this.userAdminCheckBoxId).checked;

        let newUserForm = {
            nome: userName,
            senha: userPassword,
            email: userEmail,
            admin: isAdmin
        }

        let response = await serverRequester.doPost("/user/register", newUserForm);

        if(response["responseJson"] === true){
            notification("success", "Sucesso!", "O usuário foi cadastrado com sucesso! 🥳");

        }else{
            notification("error", "Ops!", "Não foi possível cadastrar o usuário, talvez o e-mail já esteja em uso 😥");

        }

    }

    /**
     * Fecha o formulário de registro de usuário ao clicar fora dele
     * 
     * @param {Event} event Evento ao clicar fora do formulário de registro
     * @author Rafael Furtado
     */
    close(event){
        let clickTargetId = event.target.id;

        if(clickTargetId === this.id){
            this.props.control(false);

        }
            
    }

    /**
     * Constrói o componente de formulário para o registro de novos usuários
     * 
     * @returns Retorna o componente do formulário de registro de usuários pronto para ser renderizado
     * @author Rafael Furtado
     */
    getRegisterComponent(){
        let component = (
            <div id={this.id} className="z-20 w-full h-full absolute flex flex-row items-center justify-center backdrop-filter backdrop-blur-blurLogin" onClick={this.close}>
                <form className="bg-white w-loginFormW h-loginFormH flex flex-col justify-evenly items-center p-5 shadow-registerUser" onSubmit={this.registerNewUser}>
                    <label className="text-accent text-2xl text-center font-bold select-none">Cadastro de novo usuário</label>

                    <div className="flex flex-col items-center justify-center">
                        <SimpleInput type="text" iconName="user" id={this.userNameInputId} name={this.userNameInputId} placeHolder="Nome"/>

                        <SimpleInput type="email" iconName="email" id={this.userEmailInputId} name={this.userEmailInputId} placeHolder="E-mail"/>

                        <SimpleInput type="password" iconName="key" id={this.userPasswordInputId} name={this.userPasswordInputId} placeHolder="Senha"/>

                        <div className="pt-5">
                            <input id={this.userAdminCheckBoxId} name={this.userAdminCheckBoxId} type="checkbox" ></input>
                            <label htmlFor="admin" className="pl-2 select-none">Administrador</label>
                        </div>
                    </div>

                    <Button text="Cadastrar" type="confirm"></Button>
                </form>
            </div>
        );

        return component;
    }

    /**
     * Método obrigatório herdado da classe React.Component
     * 
     * Renderiza o formulário de cadastro de novos usuários na tela
     * 
     * @returns Retorna o elemento a ser renderizado na janela
     * @author Rafael Furtado
     */
    render(){
        let registerComponent = this.getRegisterComponent();

        return registerComponent;
    }

}

export default RegisterUser;