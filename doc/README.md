### Montando ambientes de desenvolvimento
O projeto pode ser dividido em dois, a parte do cliente e a do servidor. Este guia tem como finalidade guiar o preparo do ambiente para a execução de ambos.


#### Projeto do cliente
- Atenção: O Node.js é necessário para a realização dos passos abaixo;

- Nota: Para o projeto do cliente, foi adotada a IDE VSCode como padrão para seu desenvolvimento e execução;

- Após fazer o clone do repositório para sua máquina, navegue, dentro do repositório, para o seguinte diretório: ../AirplaneDocGenerator/src/client/airplanedocgenerator;

- Abra o diretório no VSCode;

- Na barra superior do VSCode, abra View > Terminal;

- É necessário instalar as dependências do projeto. O gerenciador de pacotes NPM fará o trabalho necessário. No terminal, execute o comando "npm install" e aguarde a instalação dos pacotes;

- Atenção: Caso uma nova dependência seja adicionada ao projeto, será necessário executar novamente este comando para instalá-la;
 
- Após a conclusão da instalação, novamente no terminal, digite o comando "npm run dev";

- Caso a aplicação abra uma janela, significa que o ambiente foi configurado com sucesso!

- Para encerrar a aplicação, aperte Ctrl + C no terminal, uma mensagem de confirmação irá aparecer;

- Ressaltando, após modificar algum arquivo e salvar, o resultado será visto imediatamente na aplicação, não há necessidade de sempre parar tudo e rodar novamente.


#### Projeto do servidor
O projeto do servidor ainda necessita de mais algumas configurações que ainda não entraram em vigor no repositório, então ainda está incompleto.

- Nota: Para o projeto do servidor, foi adotada a IDE Eclipse como padrão para seu desenvolvimento e execução;

- Na barra superior do Eclipse, clique em File > Open Projects from File System...;

- Clique em Directory, uma janela irá abrir. Navegue até o seguinte caminho: ..\AirplaneDocGenerator\src\server, você verá uma pasta chamada AirplaneManualGenerator, selecione-a e confirme;
 
- Clique em Finish;

- Como o projeto tem o Maven como gerenciador de dependências, após clicar em Finish você deve notar no canto inferior direito que o Eclipse estará baixando e instalando as dependências para você. Caso não apareça nada, selecione a parte do projeto no "Project Explorer" e aperte F5. Aguarde a instalação das dependências;

- No pacote "api.crabteam", execute o arquivo AirplaneManualGeneratorApplication;

- Com isso, o ambiente do servidor estará configurado!

- Atenção, o projeto exige a versão 16 do Java, certifique-se de que o padrão do seu Eclipse esteja configurado para o Java 16, caso contrário, vá ao Build Path do projeto e altere manualmente;
