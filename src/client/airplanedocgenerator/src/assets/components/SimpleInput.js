import React from "react";

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { faEnvelope, faKey, faUser } from '@fortawesome/free-solid-svg-icons';



/**
 * Classe de componente que representa um campo de input simples com um ícone associado ao lado
 * 
 * Possuí as seguintes propriedades que devem ser declaradas:
 *  - iconName -> Ícone que aparecerá ao lado do input
 *  - type -> Tipo do input, como "text", "password", etc
 *  - placeHolder -> Texto de dica que será exibido no input
 *  - id -> ID que será associado ao input
 *  - name -> Nome do input, utilizado por formulários
 * 
 * @author Rafael Furtado
 */
class SimpleInput extends React.Component{
    constructor(props) {
        super(props);

        this.iconColor = "#5E74D6";

        this.inputStyle = "border-b-2 outline-none text-center focus:bg-gray-200 ml-3";
        
        this.iconName = this.props.iconName;
        this.type = this.props.type;
        this.placeHolder = this.props.placeHolder;
        
        this.id = this.props.id;
        this.name = this.props.name;

    }

    /**
     * Constrói o componente do ícone em razão do nome passado
     * 
     * @param {String} iconName Nome do ícone
     * @returns Retorna o componente do ícone do FontAwesome
     * @author Rafael Furtado
     */
    getIcon(iconName){
        let iconComponent;

        switch (iconName) {
            case "user":
                iconComponent = <FontAwesomeIcon icon={faUser} color={this.iconColor}/>;
                
                break;
            case "key":
                iconComponent = <FontAwesomeIcon icon={faKey} color={this.iconColor}/>;

                break;
            
            case "email":
                iconComponent = <FontAwesomeIcon icon={faEnvelope} color={this.iconColor}/>;

                break;
            default:
                iconComponent = <FontAwesomeIcon icon={faUser} color={this.iconColor}/>;

                break;
        }

        return iconComponent;
    }

    /**
     * Constrói o componente adequadamente considerando os atributos passados para ele
     * 
     * @returns Retorna o componente do input simpes
     * @author Rafael Furtado
     */
    getComponent(){
        let icon = this.getIcon(this.iconName);

        let component = (
            <div className="relative right-3 mt-2 mb-2">
                {icon}
                <input id={this.id} name={this.name} type={this.type} className={this.inputStyle} placeholder={this.placeHolder}/>
            </div>
        );

        return component;
    }

    /**
     * Método obrigatório herdado da classe React.Component
     *
     * Renderiza o input na página
     *
     * @returns Retorna o elemento a ser renderizado na janela
     * @author Rafael Furtado
     */
    render(){
        let simpleInputComponent = this.getComponent();

        return simpleInputComponent;
    }
    
}

export default SimpleInput;