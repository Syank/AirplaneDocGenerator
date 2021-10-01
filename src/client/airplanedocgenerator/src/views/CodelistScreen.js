import React from "react";
import { getBackgroundImage } from "../utils/pagesUtils";

/**
 * Classe de componente que representa a tela para visualizar a codelist de um manual
 *
 * @author Rafael Furtado
 */
class CodelistScreen extends React.Component {

     getCodelistScreen() {
          let codelistScreen = (
               <div id="contentDisplay" className="w-full h-full">
                    {getBackgroundImage()}
                    <div id="codelistScreen" className="w-full h-full flex justify-center items-center">
                         <div className="bg-white w-10/12 h-5/6 relative flex justify-start text-center flex-col border-r-8 border-b-8 border-accent">
                              ABC-1234
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