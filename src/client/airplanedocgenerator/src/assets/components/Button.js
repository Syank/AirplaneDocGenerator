import React from "react";

/**
 * Uma classe de componente que representa um botão simples
 *
 * Possuí as seguintes propriedades que devem ser declaradas:
 *  - text -> Texto de exibição
 *  - type -> Tipo do botão, os valores válidos são: "confirm" e "cancel". Representam, respectivamente, um botão verde e vermelho
 *  - onClick -> Uma função a ser chamada quando o botão for clicado. Não é possível passar uma função que recebe argumentos
 *
 * @author Rafael Furtado
 */
class Button extends React.Component {
    constructor(props) {
        super(props);

        this.text = props.text;
        this.type = props.type;

        this.onClick = props.onClick;

        this.confirmTypeStyle =
            "bg-green-500 hover:bg-green-400 active:bg-green-600 focus:bg-green-400 text-white";
        this.cancelTypeStyle =
            "bg-red-500 hover:bg-red-400 active:bg-red-600 focus:bg-red-400 text-white";
    }

    /**
     * Constrói o botão com base nas propriedades passadas
     *
     * @returns Retorna o elemento do botão
     * @author Rafael Furtado
     */
    getButton() {
        let buttonStyle;

        switch (this.type) {
            case "confirm":
                buttonStyle = this.confirmTypeStyle;

                break;
            case "cancel":
                buttonStyle = this.cancelTypeStyle;

                break;
            default:
                buttonStyle = this.confirmTypeStyle;

                break;
        }

        let button = (
            <button
                className={
                    buttonStyle +
                    " shadow-simpleShadow pb-2 pt-2 pl-10 pr-10 select-none outline-none content w-min"
                }
                onClick={this.onClick}
            >
                {this.text}
            </button>
        );

        return button;
    }

    /**
     * Método obrigatório herdado da classe React.Component
     *
     * Renderiza o botão na página
     *
     * @returns Retorna o elemento a ser renderizado na janela
     * @author Rafael Furtado
     */
    render() {
        let buttonElement = this.getButton();

        return buttonElement;
    }
}

// Permite a exportação do componente
export default Button;
