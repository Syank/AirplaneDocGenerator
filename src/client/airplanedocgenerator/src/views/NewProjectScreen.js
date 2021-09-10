import React from "react";

import clouds from "../assets/misc/images/cloud.jpg";
import Button from "../assets/components/Button";

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faQuestionCircle } from "@fortawesome/free-solid-svg-icons";

/**
 * Classe que representa a página de criação de um projeto
 * @author Bárbara Port
 */
class NewProjectScreen extends React.Component {
     constructor() {
          super();

          this.cardTitle = "Crie um novo projeto de manual";
          this.cardText = "Esta sessão irá lhe auxiliar na criação de um novo projeto de manual. Preencha os campos e siga as instruções.";
     }

     /**
      * Prevenção de imagens arrastáveis
      * @param {Event} event Evento recebido na tentativa de arrastar uma imagem
      * @author Rafael Furtado
      */
     preventImageDrag(event) {
          event.preventDefault();
     }

     /**
      * Imagem de fundo
      * @returns Elemento com a imagem de fundo
      * @author Rafael Furtado
      */
     getBackgroundImage() {
          let backgroundImage =
               <img src={clouds} alt="Clouds"
                    onDragStart={this.preventImageDrag}
                    className="w-full h-full select-none absolute filter blur-blurLogin opacity-20" />

          return backgroundImage;
     }

     /**
      * Função que cria a página
      * @author Bárbara Port
      * @returns Página de criar um novo projeto
      */
     getNewProjectScreen() {
          let newProjectScreen = (
               <div id="contentDisplay" className="w-full h-full">
                    {this.getBackgroundImage()}
                    <div id="newProjectScreen" className="w-screen h-screen flex justify-center items-center">
                         <div className="bg-white lg:w-5/12 md:w-10/12 h-5/6 relative flex justify-start text-center flex-col border-r-8 border-b-8 border-accent">
                              <div className="m-8">
                                   <p className="text-2xl font-bold">Crie um novo projeto de manual</p>
                                   <hr></hr>
                                   <p className="text-lg mt-4">Esta sessão irá lhe auxiliar na criação de um novo projeto de manual, preencha os campos e siga as instruções!</p>
                              </div>
                              <form>
                                   <div className="m-10">
                                        <label for="project-name" className="text-lg">Nome: </label>
                                        <input type="text" id="project-name" className="border-b border-black focus:bg-gray-200 outline-none" placeholder="XXX-YYYY"></input>
                                        <FontAwesomeIcon icon={faQuestionCircle} color="black" />
                                   </div>
                                   <div className="m-8">
                                        <label className="text-lg">Codelist: </label>
                                        <label for="codelist-file" className="w-68 p-1 px-4 rounded-lg bg-inputFileColor text-white cursor-pointer hover:bg-blue-300 active:bg-blue-300">Selecionar Codelist em sua máquina</label>
                                        <input type="file" id="codelist-file" className="hidden"></input>

                                        <p className="text-xs ml-12 mr-12 mt-4">A escolha de um Codelist no momento da criação não é obrigatória, ele pode ser importado ou criado no gerenciamento do documento.</p>
                                   </div>
                                   <div className="mt-20">
                                        <Button text="Criar" type="confirm"></Button>
                                   </div>
                              </form>
                         </div>
                    </div>

               </div>
          );

          return newProjectScreen;
     }

     /**
      * Criar o projeto
      * @author Bárbara Port
      */
     createProject() {
          console.log("Criando um projeto!!");
     }

     /**
      * Renderiza a página de criação de um novo projeto
      * @returns Elemento a ser renderizado
      * @author Bárbara Port
      */
     render() {
          return this.getNewProjectScreen();
     }
}

export default NewProjectScreen;