import React from "react";
import Button from "./Button";

// Componente dos ícones do FontAwesome
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";

// Ícones do FontAwesome
import {
    faFile,
    faEdit,
    faPrint,
    faFileUpload,
} from "@fortawesome/free-solid-svg-icons";

/**
 * Classe do componente de cards de opções, o componente fornece ao usuário um ícone do que o card representa,
 * uma mensagem e um botão que irá ativar alguma opção
 *
 * Possuí as seguintes propriedades que devem ser declaradas:
 *  - icon -> Ícone que aparecerá no card
 *  - cardText -> Texto que será exibido no card
 *  - buttonText -> Texto que será exibido no botão do card
 *  - buttonOnClick -> Função que será chamada quando o botão do card for clicado
 *
 * @author Rafael Furtado
 */
class CardOption extends React.Component {
    constructor(props) {
        super(props);

        this.icon = props.icon;
        this.cardText = props.cardText;
        this.buttonText = props.buttonText;
        this.buttonOnClick = props.buttonOnClick;

        this.iconColor = "#5E74D6";
        this.iconSize = "3x";
    }

    /**
     * Nome do ícone do FontAwesome que deverá aparecer no card
     *
     * @param {String} iconName
     * @returns Retorna o componente de ícone do FontAwesome
     * @author Rafael Furta
     */
    getIconComponent(iconName) {
        let component;

        switch (iconName) {
            case "file":
                component = (
                    <FontAwesomeIcon
                        icon={faFile}
                        size={this.iconSize}
                        color={this.iconColor}
                    />
                );

                break;

            case "edit":
                component = (
                    <FontAwesomeIcon
                        icon={faEdit}
                        size={this.iconSize}
                        color={this.iconColor}
                    />
                );

                break;

            case "print":
                component = (
                    <FontAwesomeIcon
                        icon={faPrint}
                        size={this.iconSize}
                        color={this.iconColor}
                    />
                );

                break;
            case "file-upload":
                component = (
                    <FontAwesomeIcon
                        icon={faFileUpload}
                        size={this.iconSize}
                        color={this.iconColor}
                    />
                );
                break;
            default:
                component = (
                    <FontAwesomeIcon
                        icon={faFile}
                        size={this.iconSize}
                        color={this.iconColor}
                    />
                );

                break;
        }

        return component;
    }

    /**
     * Constrói e configura o componente do card de opção
     *
     * @returns Retorna o componente pronto para ser renderizado
     * @author Rafael Furta
     */
    getCardOption() {
        let iconComponent = this.getIconComponent(this.icon);

        let cardOption = (
            <div className="w-56 h-80 bg-white flex flex-col items-center p-5 justify-around shadow-2xl transition duration-300 ease-in-out hover:bg-green-100">
                {iconComponent}

                <label className="mb-5 mt-5 text-center">{this.cardText}</label>

                <Button
                    text={this.buttonText}
                    type="confirm"
                    onClick={this.buttonOnClick}
                ></Button>
            </div>
        );

        return cardOption;
    }

    /**
     * Método obrigatório herdado da classe React.Component
     *
     * Renderiza o card na página
     *
     * @returns Retorna o elemento a ser renderizado na janela
     * @author Rafael Furtado
     */
    render() {
        return this.getCardOption();
    }
}

// Permite a exportação do componente
export default CardOption;
