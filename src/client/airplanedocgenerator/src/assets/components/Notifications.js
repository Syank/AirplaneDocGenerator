import Swal from 'sweetalert2';



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
 */
export async function withConfirmation(cTitle, cMessage, cIcon) {
     const { value: confirmation } =  await Swal.fire({
          title: cTitle,
          text: cMessage,
          icon: cIcon,
          showCancelButton: true,
          confirmButtonColor: '#56EA6D',
          cancelButtonColor: '#d33',
          cancelButtonText: "Cancelar",
          confirmButtonText: "OK!"
     });

     return confirmation;
}

export async function addFile(lineId) {
     const { value: uploadedFile } = await Swal.fire({
          title: 'Escolha um arquivo!',
          input: 'file',
          inputAttributes: {
               autocapitalize: 'off',
               'accept': 'application/pdf',
          },
          showCancelButton: true,
          confirmButtonText: 'Pronto!',
          confirmButtonColor: '#56EA6D'
     })

     return uploadedFile;
}

export async function addCodelist(projectName) {
     const { value: uploadedFile } = await Swal.fire({
          title: 'Escolha um arquivo!',
          input: 'file',
          inputAttributes: {
               autocapitalize: 'off',
               'accept': '.xls,.xlsx',
          },
          showCancelButton: true,
          confirmButtonText: 'Pronto!',
          confirmButtonColor: '#56EA6D'
     })

     return uploadedFile;
}