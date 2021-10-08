import React from "react";

import clouds from "../assets/misc/images/cloud.jpg";

/**
 * Previne que imagens sejam "arrastáveis"
 *
 * Esta função deve ser passada para o atributo "onDragStart" do elemento
 *
 * @param {Event} event Evento recebido como parâmetro quando a ação de arrastar é iniciada pelo usuário
 * @author Rafael Furtado
 */
export function preventImageDrag(event) {
     event.preventDefault();
}

/**
 * Constrói e configura o elemento para ser a imagem de fundo da tela de login
 *
 * @returns Retorna o elemento da imagem de fundo da tela de login
 * @author Rafael Furtado
 */
export function getBackgroundImage() {
     let backgroundImage = (
          <img
               src={clouds}
               alt="Nuvens"
               onDragStart={preventImageDrag}
               className="w-full select-none absolute filter opacity-40  blur-blurLogin"
          />
     );

     return backgroundImage;
}

export function isValidProjectName(supposedProjectName) {
     let partLetter = supposedProjectName.split("-")[0];
     let partNumber = supposedProjectName.split("-")[1];

     if (partLetter === undefined || partNumber === undefined || !isValidPartLetter(partLetter) || !isValidPartNumber(partNumber)) {
          return false;
     }

     return true;
}

/**
* Verifica se a parte de letras de um nome de projeto contêm apenas letras
*
* @param {String} supposedPartLetter String da parte de letras do nome do projeto
* @author Rafael Furtado
*/
export function isValidPartLetter(supposedPartLetter) {
     if (supposedPartLetter === undefined || !isNaN(supposedPartLetter) || supposedPartLetter.length !== 3) {
          return false;
     }

     let regex = /[a-zA-Z]/;

     let letters = supposedPartLetter.split("");

     for (let i = 0; i < letters.length; i++) {
          const letter = letters[i];

          if (!regex.test(letter)) {
               return false;
          }
     }

     return true;
}

/**
* Verifica se a parte de números do nome de um projeto contêm apenas números
*
* @param {String} supposedPartNumber String da parte de números do nome de um projeto
* @author Rafael Furtado
*/
export function isValidPartNumber(supposedPartNumber) {
     if (supposedPartNumber === undefined || supposedPartNumber.length !== 4 || supposedPartNumber.includes(".")) {
          return false;
     }

     let isNumber = +supposedPartNumber;

     if (isNaN(isNumber)) {
          return false;
     }

     return true;
}