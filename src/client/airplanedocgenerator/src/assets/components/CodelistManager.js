import React from "react";
import Button from "./Button";
import { faSearch, faPen, faFileAlt, faCheck, faTimes } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { addCodelist, addFile, notification } from "../components/Notifications";
import ServerRequester from "../../utils/ServerRequester";

/**
 * Classe de componente que representa a tela para visualizar a codelist de um manual
 *
 * @author Rafael Furtado
 */
class CodelistManager extends React.Component {
    constructor(props) {
        super(props);
        
        this.filter = this.props.filter;

        this.id = "codelistManager";

        this.close = this.close.bind(this);

        this.toggleEditLine = this.toggleEditLine.bind(this);
        this.updateLine = this.updateLine.bind(this);
        this.createLinesSituationMap = this.createLinesSituationMap.bind(this);
        this.getLineActions = this.getLineActions.bind(this);
        this.addFileToLine = this.addFileToLine.bind(this);

        let projectData = this.props.projectData;

        this.state = {
            projectData: projectData
        }

        let linesSituationMap = this.createLinesSituationMap();

        this.state["linesSituation"] = linesSituationMap;

    }

    createLinesSituationMap(){
        let map = {};

        let codelistLines = this.state["projectData"]["codelist"]["linhas"];

        for (let i = 0; i < codelistLines.length; i++) {
            const line = codelistLines[i];
            
            let lineId = line["id"];

            let filePath = line["filePath"];

            let hasFile;

            if(filePath !== null){
                hasFile = true;

            }else{
                hasFile = false;

            }

            map[lineId] = {
                editing: false,
                hasFile: hasFile
            };

        }

        return map;
    }

    search() {
        
    }

    getNomeCodelist() {
        let name = this.state["projectData"]["nome"];

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

    importCodelist(event) {
        let name = document.getElementById("nomeProjeto").textContent;
        addCodelist(name);
    }

    getManageButtons() {
        let component = (
            <div className="flex flex-row w-full">
                <div>
                    <Button text="Nova Linha" type="codelistControl"></Button>
                </div>
                <Button text="Exportar Codelist" type="codelistControl"></Button>
                <Button text="Importar Codelist" type="codelistControl" onClick={this.importCodelist}></Button>
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

    getRemarksText(remarks, editingId) {
        let remarkText = "";

        if(editingId === undefined){
            for (let i = 0; i < remarks.length; i++) {
                const remark = remarks[i];
                remarkText += "-" + remark["traco"] + ", ";

            }

        }else{
            for (let i = 0; i < remarks.length; i++) {
                const remark = remarks[i];
                remarkText += "-" + remark["traco"] + " (" + remark["apelido"] + "), ";

            }

        }

        if(remarkText.endsWith(", ")){
            remarkText = remarkText.slice(0, remarkText.length - 2);

        }

        return remarkText;
    }

    async addFileToLine (event) {
        let lineId = event.currentTarget.parentElement.id;
        let justId = lineId.split("-").pop();

        let file = await addFile(justId);

        let serverRequester = new ServerRequester("http://localhost:8080");

        let formData = new FormData();
        formData.append("file", file);
        formData.append("line", justId);

        let response = await serverRequester.doPost(
            "/codelistLine/attachFile",
            formData,
            "multipart/form-data"
        );

        if (response.status === 200) {
            notification("success", "Sucesso! 😄", "O arquivo foi associado com sucesso!");

            let newData = await this.props.reloadData();

            let linesSituationMap = this.createLinesSituationMap();

            this.setState({linesSituation: linesSituationMap, projectData: newData});

        }else {
            notification("error", "Ops 🙁", "Não foi possível associar o arquivo a essa linha.");

        }
       
    }

    toggleEditLine(event){
        let lineId = this.getLineId(event);

        let linesSituation = this.state["linesSituation"]

        // Inverte o valor booleano
        linesSituation[lineId]["editing"] = !linesSituation[lineId]["editing"];

        this.setState(linesSituation);

    }

    getLineId(event){
        let lineId = event.target.parentElement.parentElement.id.split("-")[2];

        return lineId
    }

    getLineActions(lineId){
        let linesSituationMap = this.createLinesSituationMap();

        let lineSituation = linesSituationMap[lineId];

        let actions = [];

        if(lineSituation["editing"] === false){
            let editButton = (<FontAwesomeIcon key={"edit-line-" + lineId} onClick={this.toggleEditLine} icon={faPen} color={"#5E74D6"} className="cursor-pointer mr-3"/>);

            let hasFile = lineSituation["hasFile"];

            let iconColor;

            if(hasFile === true){
                iconColor = "#32da1f";

            }else{
                iconColor = "#f43a3a";
            }

            let fileButton = (<FontAwesomeIcon key={"file-line-" + lineId} icon={faFileAlt} color={iconColor} className="cursor-pointer" onClick={this.addFileToLine}/> );
            
            actions.push(editButton);
            actions.push(fileButton);

        }else{
            let confirmButton = (<FontAwesomeIcon key={"confirm-line-" + lineId} onClick={this.updateLine} icon={faCheck} color="#18cb26" className="cursor-pointer mr-3"/>);
            let discardButton = (<FontAwesomeIcon key={"discard-line-" + lineId} onClick={this.toggleEditLine} icon={faTimes} color="#ef2c2c" className="cursor-pointer"/>);

            actions.push(confirmButton);
            actions.push(discardButton);

        }

        return actions;
    }

    checkIsValidRemarksText(remarksText){
        if(remarksText === ""){
            return false;
        }

        let remarksParts = remarksText.split(",");

        for (let i = 0; i < remarksParts.length; i++) {
            const part = remarksParts[i];
            
            let remarkInfo = part.split("(");

            if(remarkInfo.length !== 2){
                return false;
            }

            if(!remarkInfo[0].includes("-")){
                return false;
            }
            
            if(remarkInfo[1].charAt(remarkInfo[1].length - 1) !== ")"){
                return false;
            }

        }

        return true;
    }

    async updateLine(event){
        let lineId = this.getLineId(event);

        let newSection = document.getElementById("line-sectionNumber-" + lineId).value;
        let newSubSection = document.getElementById("line-subsectionNumber-" + lineId).value;
        let newBlockNumber = document.getElementById("line-blockNumber-" + lineId).value;
        let newBlockName = document.getElementById("line-blockName-" + lineId).value;
        let newCode = document.getElementById("line-code-" + lineId).value;
        let newRemarks = document.getElementById("line-remarks-" + lineId).value;

        let isValidRemarks = this.checkIsValidRemarksText(newRemarks);

        if(!isValidRemarks){
            notification("error", "Texto de Remarks (traços) inválido! 😤",
                "Para atualizar o campo de remark, o texto deve ter o seguinte formato: -XX (APELIDO). "
                + "Onde X são os números do traço. Múltiplos remarks devem ser separados por vírgula, como: "
                + "-XX (APELIDO), -XX (APELIDO)");

        }else{
            let updatedLine = {
                id: lineId,
                sectionNumber: newSection,
                subsectionNumber: newSubSection,
                blockNumber: newBlockNumber,
                blockName: newBlockName,
                code: newCode,
                remarksText: newRemarks
            }

            let serverRequester = new ServerRequester("http://localhost:8080");

            let response = await serverRequester.doPost("/codelistLine/update", updatedLine);

            if(response["responseJson"] === true){
                notification("success", "Sucesso! 😀", "Os dados da linha foram atualizados");

                let newData = await this.props.reloadData();

                let situationsMap = this.state["linesSituation"];

                let editing = situationsMap[lineId]["editing"];

                situationsMap[lineId]["editing"] = !editing;

                this.setState({linesSituation:situationsMap, projectData: newData});

            }else{
                notification("error", "Ops 🤨", "Algo deu errado durante a atualização dos dados da linha");

            }

        }

    }

    getRows() {
        let linhas = [];
        let linhasProjectData = this.state["projectData"]["codelist"]["linhas"];

        for (let i = 0; i < linhasProjectData.length; i++) {
            let linhaData = linhasProjectData[i];

            let needRender = this.needRenderRow(linhaData["remarks"]);

            if (needRender) {
                let id = linhaData["id"];

                let actions = this.getLineActions(id);

                let lineId = "codelist-line-" + id;

                let component;

                let editing = this.state["linesSituation"][id]["editing"];

                if(editing === false){
                    let remarks = this.getRemarksText(linhaData["remarks"]);

                    component = (
                        <tr id={lineId} key={"id-linha-" + lineId}>
                            <td className="border border-gray-300">{linhaData["sectionNumber"]}</td>
                            <td className="border border-gray-300">{linhaData["subsectionNumber"]}</td>
                            <td className="border border-gray-300">{linhaData["blockNumber"]}</td>
                            <td className="border border-gray-300">{linhaData["blockName"]}</td>
                            <td className="border border-gray-300">{linhaData["code"]}</td>
                            <td className="border border-gray-300">{remarks}</td>
                            <td id={"actions-line-" + id} className="border border-gray-300">{actions}</td>
                        </tr>
                    );

                }else{
                    let remarks = this.getRemarksText(linhaData["remarks"], id);

                    component = (
                        <tr id={lineId} key={"id-linha-" + lineId}>
                            <td className="border border-gray-300">
                                <input className="w-full text-center" type="text" id={"line-sectionNumber-" + id} placeholder={linhaData["sectionNumber"]}></input>
                            </td>
                            <td className="border border-gray-300">
                                <input className="w-full text-center" type="text" id={"line-subsectionNumber-" + id} placeholder={linhaData["subsectionNumber"]}></input>
                            </td>
                            <td className="border border-gray-300">
                                <input className="w-full text-center" type="text" id={"line-blockNumber-" + id} placeholder={linhaData["blockNumber"]}></input>
                            </td>
                            <td className="border border-gray-300">
                                <input className="w-full text-center" type="text" id={"line-blockName-" + id} placeholder={linhaData["blockName"]}></input>
                            </td>
                            <td className="border border-gray-300">
                                <input className="w-full text-center" type="text" id={"line-code-" + id} placeholder={linhaData["code"]}></input>
                            </td>
                            <td className="border border-gray-300">
                                <textarea className="w-full text-center" type="text" id={"line-remarks-" + id} placeholder={remarks}></textarea>
                            </td>
                            <td id={"actions-line-" + id} className="border border-gray-300">{actions}</td>
                        </tr>
                    );

                }

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
                    <h1 id="nomeProjeto" className="text-2xl	font-bold text-center leading-loose">{this.getNomeCodelist()}</h1>
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