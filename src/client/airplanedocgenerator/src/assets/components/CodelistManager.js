import React from "react";
import Button from "./Button";



/**
 * Classe de componente que representa a tela para visualizar a codelist de um manual
 *
 * @author Rafael Furtado
 */
class CodelistManager extends React.Component {
    constructor(props) {
        super(props);

        this.projectData = this.props.projectData;
        this.filter = this.props.filter;

        this.id = "codelistManager";

        this.close = this.close.bind(this);

    }

    getNomeCodelist() {
        let name = this.projectData["nome"];

        if (this.filter !== "all") {
            name += "-" + this.filter;

        }

        return name;
    }

    getCodelistManagerComponent() {
        let component = (
            <div id={this.id} className="z-10 w-full h-full absolute flex flex-row items-center justify-center backdrop-filter backdrop-blur-blurLogin" onClick={this.close}>
                <div className="h-5/6 w-5/6 bg-white">
                    {this.getContent()}
                </div>
            </div>
        );

        return component;
    }

    getContent() {
        let content = (
            <div className="flex flex-row justify-around">
                <div className="flex flex-col">
                    <div>
                        <Button text="Nova linha" type="confirm"></Button>
                    </div>
                    <div className="flex flex-col ">
                        <Button text="Exportar Codelist" type="confirm"></Button>
                        <Button text="Importar Codelist" type="confirm"></Button>
                    </div>
                </div>
                <div>
                    <h1 className="font-bold">{this.getNomeCodelist()}</h1>
                    <table className="border-collapse border border-gray-300">
                        <thead>
                            <th className="border border-gray-300">Nº seção</th>
                            <th className="border border-gray-300">Nº subseção</th>
                            <th className="border border-gray-300">Nº bloco</th>
                            <th className="border border-gray-300">Nome do bloco</th>
                            <th className="border border-gray-300">Código</th>
                            <th className="border border-gray-300">Remarks</th>
                            {/*a partir daqui pode ser que tenham colunas de acordo com os dados */}
                        </thead>
                        <tbody id="codelist-lines">
                            <tr>
                                <td className="border border-gray-300">00</td>
                                <td className="border border-gray-300"></td>
                                <td className="border border-gray-300">00</td>
                                <td className="border border-gray-300">Letter</td>
                                <td className="border border-gray-300">50</td>
                                <td className="border border-gray-300">-50</td>
                            </tr>
                        </tbody>
                    </table>
                </div>
                <div>
                    Direito
                    <p>Lado esquerdo</p>
                </div>
            </div>
        );

        return content;
    }

    /**
     * Fecha o formulário de registro de usuário ao clicar fora dele
     * 
     * @param {Event} event Evento ao clicar fora do formulário de registro
     * @author Rafael Furtado
     */
    close(event) {
        let clickTargetId = event.target.id;

        if (clickTargetId === this.id) {
            this.props.hide();

        }

    }

    /**
    * Renderiza a página de criação de um novo projeto
    * @returns Elemento a ser renderizado
    * @author Bárbara Port
    */
    render() {
        let codelistScreen = this.getCodelistManagerComponent();

        return codelistScreen;
    }

}

export default CodelistManager;