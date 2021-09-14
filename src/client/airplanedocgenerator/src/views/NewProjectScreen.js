import React from "react";

import Button from "../assets/components/Button";

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faQuestionCircle } from "@fortawesome/free-solid-svg-icons";

import { getBackgroundImage } from "../utils/pagesUtils";
import ServerRequester from "../utils/ServerRequester";

/**
 * Classe que representa a página de criação de um projeto
 * @author Bárbara Port
 */
class NewProjectScreen extends React.Component {

     /**
      * Criar um novo projeto
      * 
      * @param {Event} event
      * @author Rafael Furtado
      */
     async sendFormData(event) {
          event.preventDefault();

          let serverRequester = new ServerRequester("http://localhost:8080");

          //let fileInput = document.getElementById("codelist-file");
          let nameInput = document.getElementById("project-name");

          let projectName = nameInput.value;

          let newProjectForm = {
               nome: projectName
          }

          let response = await serverRequester.doPost("/project/create", newProjectForm);
          
          if(response["responseJson"] === true){
               alert("Projeto criado com sucesso");

          }else{
               alert("Não foi possível criar o projeto, contate os administradores");

          }
          
     }

     /**
      * Função que cria a página
      * @author Bárbara Port
      * @returns Página de criar um novo projeto
      */
     getNewProjectScreen() {
          let newProjectScreen = (
               <div id="contentDisplay" className="w-full h-full">
                    {getBackgroundImage()}
                    <div id="newProjectScreen" className="w-full h-full flex justify-center items-center">
                         <div className="bg-white lg:w-5/12 md:w-10/12 h-5/6 relative flex justify-start text-center flex-col border-r-8 border-b-8 border-accent">
                              <div className="m-8">
                                   <p className="text-2xl font-bold">Crie um novo projeto de manual</p>
                                   <hr></hr>
                                   <p className="text-lg mt-4">Esta sessão irá lhe auxiliar na criação de um novo projeto de manual, preencha os campos e siga as instruções!</p>
                              </div>
                              <form onSubmit={this.sendFormData}>
                                   <div className="m-10">
                                        <label htmlFor="project-name" className="text-lg">Nome: </label>
                                        <input type="text" id="project-name" className="border-b border-black focus:bg-gray-200 outline-none" placeholder="XXX-YYYY"></input>
                                        <FontAwesomeIcon icon={faQuestionCircle} color="black" />
                                   </div>
                                   <div className="m-8">
                                        <label className="text-lg">Codelist: </label>
                                        <label htmlFor="codelist-file" className="w-68 p-1 px-4 rounded-lg bg-inputFileColor text-white cursor-pointer hover:bg-blue-300 active:bg-blue-300">Selecionar Codelist em sua máquina</label>
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
      * Renderiza a página de criação de um novo projeto
      * @returns Elemento a ser renderizado
      * @author Bárbara Port
      */
     render() {
          return this.getNewProjectScreen();
     }
}

export default NewProjectScreen;