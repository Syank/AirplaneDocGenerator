import React from "react";
import Button from "./Button";
import { faSearch, faPen } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";

/**
 * Classe de componente que representa a tela para visualizar a codelist de um manual
 *
 * @author Rafael Furtado
 */
class CodelistManager extends React.Component {
    constructor(props) {
        super(props);

        this.projectName = window.sessionStorage.getItem("selectedProject");

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

    getSearchSide() {
        let component = (
            <div>
                <div className="text-center flex flex-row">
                    <input id="codelistSearch" type="text" placeholder="Pesquisar" className="text-center outline-none border-b-2 mr-2"></input>
                    <FontAwesomeIcon onClick={this.search} icon={faSearch} className="cursor-pointer"></FontAwesomeIcon><br></br>
                </div>
                <div className="w-full flex flex-row">
                    <div className="w-3/6">
                        <div>
                            <input value="sessionNumber" type="radio" id="sessionNumber" name="searchCriteria"></input>
                            <label htmlFor="sessionNumber" className="pl-1">Nº seção</label>
                        </div>
                        <div>
                            <input value="blockName" type="radio" id="blockName" name="searchCriteria"></input>
                            <label htmlFor="blockName" className="pl-1">Nome do bloco</label>
                        </div>
                        <div>
                            <input value="remark" type="radio" id="remark" name="searchCriteria"></input>
                            <label htmlFor="remark" className="pl-1">Remark</label>
                        </div>
                    </div>
                    <div className="w-3/6">
                        <div>
                            <input value="blockNumber" type="radio" id="blockNumber" name="searchCriteria"></input>
                            <label htmlFor="blockNumber" className="pl-1">Nº bloco</label>
                        </div>
                        <div>
                            <input value="code" type="radio" id="code" name="searchCriteria"></input>
                            <label htmlFor="code" className="pl-1">Código</label>
                        </div>
                    </div>
                </div>
            </div>
        );

        return component;
    }

    getManageButtons() {
        let component = (
            <div className="flex flex-row w-full">
                <div>
                    <Button text="Nova Linha" type="codelistControl"></Button>
                </div>
                <Button text="Exportar Codelist" type="codelistControl"></Button>
                <Button text="Importar Codelist" type="codelistControl"></Button>
            </div>
        );

        return component;
    }

    needRenderRow(remarks) {
        if (this.filter === "all") {
            return true;
        }
        else {
            for (let i = 0; i < remarks.length; i++) {
                const remark = remarks[i];

                let traco = remark["traco"];

                if (traco === this.filter) {
                    return true;
                }
            }
        }

        return false;
    }

    getRemarksText(remarks) {
        let remarkText = "";

        for (let i = 0; i < remarks.length; i++) {
            const remark = remarks[i];
            remarkText += "-" + remark["traco"] + "\n";
        }

        return remarkText;
    }

    getRows() {
        let linhas = [];
        let linhasProjectData = this.projectData["codelist"]["linhas"];

        for (let i = 0; i < linhasProjectData.length; i++) {
            let linhaData = linhasProjectData[i];

            let needRender = this.needRenderRow(linhaData["remarks"]);

            if (needRender) {
                let remarks = this.getRemarksText(linhaData["remarks"]);

                let id = linhaData["id"];

                let actions = this.getLineActions(id);

                let lineId = "codelist-line-" + id;

                let component = (
                    <tr key={lineId}>
                        <td className="border border-gray-300">{linhaData["sectionNumber"]}</td>
                        <td className="border border-gray-300">{linhaData["sectionNumber"]}</td>
                        <td className="border border-gray-300">{linhaData["blockNumber"]}</td>
                        <td className="border border-gray-300">{linhaData["blockName"]}</td>
                        <td className="border border-gray-300">{linhaData["code"]}</td>
                        <td className="border border-gray-300">{remarks}</td>
                        <td className="border border-gray-300"><FontAwesomeIcon icon={faPen} color={"#5E74D6"} className="cursor-pointer" /></td>
                    </tr>
                );

                linhas.push(component);

            }

        }

        return linhas;
    }

    getTable() {
        let tableLines = (
            <table className="w-full table-fixed border-collapse border border-gray-300 text-center">
                <thead>
                    <tr>
                        <th className="border border-gray-300 bg-yellow-200">Nº seção</th>
                        <th className="border border-gray-300 bg-yellow-200">Nº subseção</th>
                        <th className="border border-gray-300 bg-yellow-200">Nº bloco</th>
                        <th className="border border-gray-300 bg-yellow-200">Nome do bloco</th>
                        <th className="border border-gray-300 bg-yellow-200">Código</th>
                        <th className="border border-gray-300 bg-gray-300">Remarks</th>
                        <th className="border border-gray-300 bg-accent text-white">Ações</th>
                    </tr>
                </thead>
                <tbody>
                    {this.getRows()}
                </tbody>
            </table>
        );

        return tableLines;
    }

    getCodelistAndSearchComponents() {
        let component = (
            <div className="flex flex-row h-full overflow-auto">
                <div className="mr-5">
                    <h1 className="text-2xl	font-bold text-center leading-loose">{this.getNomeCodelist()}</h1>
                    {this.getTable()}
                </div>
                <div className="mt-12">
                    {this.getSearchSide()}
                </div>
            </div>
        );

        return component;
    }

    getContent() {
        let content = (
            <div className="h-full p-5">
                <div className="flex flex-col w-full h-full justify-between">
                    {this.getCodelistAndSearchComponents()}
                    {this.getManageButtons()}
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