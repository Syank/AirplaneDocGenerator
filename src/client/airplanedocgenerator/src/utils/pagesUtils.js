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

