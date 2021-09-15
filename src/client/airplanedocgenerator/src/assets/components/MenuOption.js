import React from "react";

class MenuOption extends React.Component{
    constructor(props) {
        super(props);

        this.text = this.props.text;

        this.callParentAction = this.callParentAction.bind(this);

    }

    callParentAction(){
        this.props.action();

    }

    getMenuOption(){
        let option = (
            <div onClick={this.callParentAction} className="hover:bg-hoverTopBarButton active:bg-activeTopBarButton flex items-center justify-center p-1 cursor-pointer">
                <label className="cursor-pointer text-white select-none"><b>{this.text}</b></label>
            </div>
        );

        return option;
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
        let option = this.getMenuOption();

        return option;
    }

}

export default MenuOption;