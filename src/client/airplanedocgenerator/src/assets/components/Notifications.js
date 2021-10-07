import Swal from 'sweetalert2';
import ServerRequester from '../../utils/ServerRequester';

/**
 * Função que notificará o usuário de acordo com os parâmetros passados
 * @param {String} status Tipos: https://sweetalert2.github.io/#icons
 * @param {String} title 
 * @param {String} message 
 * 
 * @author Bárbara Port
 */
export function notification(nStatus, nTitle, nMessage) {
     Swal.fire({
          icon: nStatus,
          title: nTitle,
          text: nMessage,
          confirmButtonColor: '#56EA6D',
     })
}

/**
 * 
 * @param {String} cTitle Título quando está confirmando
 * @param {String} cMessage Mensagem para exibir ao usuário na confirmação
 * @param {String} cIcon Tipos: https://sweetalert2.github.io/#icons
 * @param {String} sOption O que fazer após clicar no OK?
 */
export function withConfirmation(cTitle, cMessage, cIcon, sOption) {
     Swal.fire({
          title: cTitle,
          text: cMessage,
          icon: cIcon,
          showCancelButton: true,
          confirmButtonColor: '#56EA6D',
          cancelButtonColor: '#d33',
          cancelButtonText: "Cancelar",
          confirmButtonText: "OK!"
     }).then((result) => {
          if (result.isConfirmed) {
               switch (sOption) {
                    case "example":
                         notification("success", "Uhu!", "Exemplo exibido!");
                         break;

                    default:
                         break;
               }
          }
     })
}
/**
 *  withConfirmation (
          "Criar o projeto?",
          "Quer mesmo criar o projeto?",
          "warning",
          "example"
     );
 */

export async function addFile(lineId) {
     const { value: uploadedFile } = await Swal.fire({
          title: 'Escolha um arquivo!',
          input: 'file',
          inputAttributes: {
               autocapitalize: 'off',
               'accept': 'application/pdf',
          },
          showCancelButton: true,
          confirmButtonText: 'Pronto',
          confirmButtonColor: '#56EA6D',
          showLoaderOnConfirm: true,
     })

     if (uploadedFile) {
          let serverRequester = new ServerRequester("http://localhost:8080");

          let formData = new FormData();
          formData.append("file", uploadedFile);
          formData.append("line", lineId);

          let response = await serverRequester.doPost(
               "/line/attachFile",
               formData,
               "multipart/form-data"
          );

          console.log(response);

          if (response.status === "ok") {
               notification("success", "Sucesso! 😄", "O arquivo foi associado com sucesso!");
          }
          else {
               notification("error", "Ops 🙁", "Não foi possível associar o arquivo a essa linha.");
          }
     }
}