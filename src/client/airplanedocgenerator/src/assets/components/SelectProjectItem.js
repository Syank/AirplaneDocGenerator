import React from "react";

import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faTrash } from "@fortawesome/free-solid-svg-icons";



/**
 * Classe de componente para construir um elemento da lista de manuais a serem exibidos na página
 * SelectProjectScreen
 * 
 * @author Rafael Furtado
 */
class SelectProjectItem extends React.Component {
    constructor(props) {
        super(props);
        
        this.nome = this.props.projectName;

        this.setSelected = this.setSelected.bind(this);
        this.delete = this.delete.bind(this);

    }

    /**
     * Seleciona o manual para exibir suas informações
     * 
     * @author Rafael Furtado
     */
    setSelected() {
        this.props.setSelectedItem(this.nome);

    }

    /**
     * Delete o manual em questão do sistema
     */
    delete(){
        this.props.deleteProject(this.nome);

    }

    /**
     * Constrói o componente que representa um manual na lista de exibição de manuais
     * 
     * @returns Retorna o componente pronto para ser renderizado
     * @author Rafael Furtado
     */
    getSelectItem(){
        let item = (
            <div className="flex flex-row justify-between pr-3 pl-3 border-b-2 border-gray-400 mb-5">
                <label className="hover:text-green-600 cursor-pointer" onClick={this.setSelected}>{this.nome}</label>
                <FontAwesomeIcon
                    className="cursor-pointer hover:text-red-400 active:text-red-600 focus:text-red-400"
                    icon={faTrash}
                    color={"#FF0000"}
                    onClick={this.delete}
                />
            </div>
        );

        return item;
    }

    /**
     * Método obrigatório herdado da classe React.Component
     *
     * Renderiza o elemento na página
     *
     * @returns Retorna o elemento a ser renderizado na janela
     * @author Rafael Furtado
     */
    render() {
        let item = this.getSelectItem();

        return item;
    }

}

export default SelectProjectItem;