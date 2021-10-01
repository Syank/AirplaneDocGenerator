import React from "react";
import { getBackgroundImage } from "../utils/pagesUtils";
import Button from "../assets/components/Button";

/**
 * Classe de componente que representa a tela para visualizar a codelist de um manual
 *
 * @author Rafael Furtado
 */
class CodelistScreen extends React.Component {

     getNomeCodelist () {
          return "ABC-1234";
     }

     getCodelistScreen() {
          let codelistScreen = (
               <div id="contentDisplay" className="w-full h-full">
                    {getBackgroundImage()}
                    <div id="codelistScreen" className="w-full h-full flex justify-center items-center">
                         <div className="bg-white w-10/12 h-5/6 relative flex justify-start text-center flex-col border-r-8 border-b-8 border-accent">
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
                         </div>
                    </div>
               </div>
          );

          return codelistScreen;
     }

     /**
     * Renderiza a página de criação de um novo projeto
     * @returns Elemento a ser renderizado
     * @author Bárbara Port
     */
     render() {
          let codelistScreen = this.getCodelistScreen();

          return codelistScreen;
     }
}

export default CodelistScreen;