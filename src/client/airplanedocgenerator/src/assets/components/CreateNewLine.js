import React from "react";
import Button from "./Button";
import FileInput from "./FileInput";

import ServerRequester from "../../utils/ServerRequester";
import { notification } from "./Notifications";

class CreateNewLine extends React.Component{
    constructor(props){
        super(props);

        this.id="registerNewLine"

        this.close = this.close.bind(this);
        this.createNewLine = this.createNewLine.bind(this);

        console.log(this.props.control);
    }

    /**
     * Constrói o componente de formulário para a criação de uma nova linha
     *
     * @returns Retorna o componente do formulário de criar uma nova linha pronto para ser renderizado
     * @author Carolina Margiotti
     */
    getRegisterNewLineComponent(){
        let component = (
            <div id={this.id} className="z-50 w-full h-full absolute flex flex-row items-center justify-center backdrop-filter backdrop-blur-blurLogin" onClick={this.close}>
                <form className="bg-white w-loginFormW h-loginFormH flex flex-col justify-evenly items-center p-5 shadow-registerUser" onSubmit={this.createNewLine}>
                    <label className="text-accent text-2xl text-center font-bold select-none">Cadastro de nova linha</label>

                    <div className="flex flex-col items-center justify-center">
                        <FileInput accept="application/pdf"/>
                    </div>

                    <Button text="Cadastrar" type="confirm"></Button>
                </form>
            </div>
        )

        return component;
    }

    async createNewLine(event){
        let fileSelected = document.getElementById('selectedFileId').files.item(0);

        event.preventDefault();

        let response = {};

        if(response["responseJson"] === true){
            notification("success", "Sucesso!", "Nova linha cadastrada com sucesso! 🥳");

        }else{
            notification("error", "Ops!", "Não foi possível criar a nova linha 😥");
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
     * Método obrigatório herdado da classe React.Component
     *
     * Renderiza o formulário de criação de nova linha.
     *
     * @returns Retorna o elemento a ser renderizado na janela
     * @author Carolina Margiotti
     */
    render(){
        let createNewLineComponent = this.getRegisterNewLineComponent();
        return createNewLineComponent;
    }
}

export default CreateNewLine;