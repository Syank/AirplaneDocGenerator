import React from "react";
import { faPen } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { notification } from "../components/Notifications";
import ServerRequester from "../../utils/ServerRequester";

class Table extends React.Component {

     constructor(props) {
          super(props);

          this.projectName = window.sessionStorage.getItem("selectedProject");

          this.state = {
               codelistTable: []
          };
     }

     /**
      * Faz uma requisição ao servidor para obter todas as linhas de uma codelist
      * 
      * @returns Retorna a lista de linhas de uma codelist
      * @author Bárbara Port
      */
     async getCodelistLines() {
          let serverRequester = new ServerRequester("http://localhost:8080");
          let requestParameters = {
               codelist: this.projectName
          };
          let response = await serverRequester.doGet("/codelist/getLines", requestParameters);

          let lines = [];
          if (response.responseJson === false) {
               notification("error", "Algo deu errado 🙁", "Não foi possível carregar as linhas.");
          }
          else {
               lines = response.responseJson;
          }
          return lines;
     }

     renderTableData() {
          this.getCodelistLines().then((res) => {
               this.setState({
                    codelistTable:
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
                                   {res.forEach((line) => {
                                        <tr>
                                             <td className="border border-gray-300">{line[0]}</td>
                                             <td className="border border-gray-300">{"b"}</td>
                                             <td className="border border-gray-300">{"c"}</td>
                                             <td className="border border-gray-300">{"d"}</td>
                                             <td className="border border-gray-300">{"e"}</td>
                                             <td className="border border-gray-300">{"f"}</td>
                                             <td className="border border-gray-300"><FontAwesomeIcon icon={faPen} color={"#5E74D6"} className="cursor-pointer" /></td>
                                        </tr>
                                   })};
                              </tbody>
                         </table>
               });
          });
     }

     render() {
          let table = this.state.codelistTable;
          return table;
     }
}

export default Table;