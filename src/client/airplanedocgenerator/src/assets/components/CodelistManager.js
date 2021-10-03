import React from "react";
import Button from "./Button";
import { faSearch } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";


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

    search() {

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
            <div className="h-full">
                <h1 className="text-2xl	font-bold text-center leading-loose">{this.getNomeCodelist()}</h1>
                <div className="h-5/6 flex flex-row justify-around">
                    <div className="w-1/5 flex flex-col place-content-between">
                        <div>
                            <Button text="Nova Linha" type="codelistControl"></Button>
                        </div>
                        <div className="flex flex-col place-content-end">
                            <Button text="Exportar Codelist" type="codelistControl"></Button>
                            <Button text="Importar Codelist" type="codelistControl"></Button>
                        </div>
                    </div>
                    <div className="w-6/12">
                        <table className="w-full table-fixed border-collapse border border-gray-300 text-center">
                            <thead>
                                <tr>
                                    <th className="border border-gray-300 bg-yellow-200">Nº seção</th>
                                    <th className="border border-gray-300 bg-yellow-200	">Nº subseção</th>
                                    <th className="border border-gray-300 bg-yellow-200	">Nº bloco</th>
                                    <th className="border border-gray-300 bg-yellow-200	">Nome do bloco</th>
                                    <th className="border border-gray-300 bg-yellow-200	">Código</th>
                                    <th className="border border-gray-300 bg-gray-300">Remarks</th>
                                    {/*a partir daqui pode ser que tenham colunas de acordo com os dados */}
                                </tr>
                            </thead>
                            <tbody id="codelist-lines">
                                <tr>
                                    <td className="border border-gray-300">00</td>
                                    <td className="border border-gray-300"></td>
                                    <td className="border border-gray-300">00</td>
                                    <td className="border border-gray-300">Letter</td>
                                    <td className="border border-gray-300">50</td>
                                    <td className="border border-gray-300">-50</td>
                                    {/*a partir daqui teremos linhas de acordo com os dados */}
                                </tr>
                            </tbody>
                        </table>
                    </div>
                    <div className="w-3/12">
                        <div className="text-center">
                            <input id="codelistSearch" type="text" placeholder="Pesquisar" className="text-center outline-none border-b-2 mr-2"></input>
                            <FontAwesomeIcon onClick={this.search} icon={faSearch} className="cursor-pointer"></FontAwesomeIcon><br></br>
                        </div>
                        <div className="w-full flex flex-row">
                            <div className="w-3/6">
                                <div>
                                    <input type="checkbox" id="sessionNumber" name="sessionNumber"></input>
                                    <label htmlFor="sessionNumber" className="pl-1">Nº seção</label>
                                </div>
                                <div>
                                    <input type="checkbox" id="blockName" name="blockName"></input>
                                    <label htmlFor="sessionNumber" className="pl-1">Nome do bloco</label>
                                </div>
                                <div>
                                    <input type="checkbox" id="remark" name="remark"></input>
                                    <label htmlFor="sessionNumber" className="pl-1">Remark</label>
                                </div>
                            </div>
                            <div className="w-3/6">
                                <div>
                                    <input type="checkbox" id="blockNumber" name="blockNumber"></input>
                                    <label htmlFor="sessionNumber" className="pl-1">Nº bloco</label>
                                </div>
                                <div>
                                    <input type="checkbox" id="code" name="code"></input>
                                    <label htmlFor="sessionNumber" className="pl-1">Código</label>
                                </div>
                            </div>
                        </div>
                    </div>
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