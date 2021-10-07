import React from "react";
import MenuOption from "./MenuOption";



/**
 * Classe do componente de menu da TopBar
 *
 * O menu é dinâmico e se adaptará em razão do tipo de usuário logado
 *
 * @author Rafael Furtado
 */
class TopBarMenu extends React.Component{
    constructor(props) {
        super(props);

        this.loggout = this.loggout.bind(this);
        this.openRegisterUserMenu = this.openRegisterUserMenu.bind(this);

    }

    /**
     * Faz o logout do usuário do sistema
     *
     * @author Rafael Furtado
     */
    loggout(){
        this.props.toggleMenu();
        this.props.loggoutFunction();

    }

    /**
     * Abre o menu de registro de usuários
     *
     * @author Rafael Furtado
     */
    openRegisterUserMenu(){
        this.props.toggleMenu();
        this.props.setShowRegisterUser(true);
    }

    /**
     * Constrói o componente do menu
     *
     * @returns Retorna o elemento do menu
     */
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