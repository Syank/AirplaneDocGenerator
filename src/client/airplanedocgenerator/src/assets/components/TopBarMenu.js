import React from "react";
import MenuOption from "./MenuOption";

class TopBarMenu extends React.Component{
    constructor(props) {
        super(props);

        this.loggout = this.loggout.bind(this);

    }

    loggout(){
        this.props.loggoutFunction();

    }

    openRegisterUserMenu(){
        console.log("aaaaaaaa");
    }

    getTopBarMenu(){
        let menu = (
            <div className="z-20 w-topBarMenuW h-topBarMenuH min-h-topBarMenuMinH absolute bg-topBar top-8">
                {this.props.adminOptions &&
                    <MenuOption action={this.openRegisterUserMenu} text="Cadastrar usuários"></MenuOption>
                }
                
                <MenuOption action={this.loggout} text="Sair"></MenuOption>
            </div>
        );

        return menu
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
        let menu = this.getTopBarMenu();

        return menu;
    }
}

export default TopBarMenu;