### Montando ambientes de desenvolvimento

O projeto pode ser dividido em dois: a parte do cliente e a do servidor. Este guia tem como finalidade guiar o preparo do ambiente para a execução de ambos.


#### Projeto do cliente

- Depedências necessárias para a execução:
  - Node.js
  - VSCode

- Nota: Para o projeto do cliente, foi adotada a IDE VSCode como padrão para seu desenvolvimento e execução;

##### Passos
- Após fazer o clone do repositório para sua máquina, navegue, dentro do repositório, para o seguinte diretório: ../AirplaneDocGenerator/src/client/airplanedocgenerator;

- Abra o diretório no VSCode;

- Na barra superior do VSCode, abra View > Terminal;

- É necessário instalar as dependências do projeto. O gerenciador de pacotes NPM fará o trabalho necessário. No terminal, execute o comando "npm install" e aguarde a instalação dos pacotes;

- Atenção: caso uma nova dependência seja adicionada ao projeto, será necessário executar novamente este comando para instalá-la (sempre verifique);
 
- Após a conclusão da instalação, novamente no terminal, digite o comando ***npm run dev***;

- Caso a aplicação abra uma janela, significa que o ambiente foi configurado com sucesso!

- Para encerrar a aplicação, aperte **Ctrl + C** no terminal, uma mensagem de confirmação irá aparecer;

- **Ressaltando**: após modificar algum arquivo e salvar, o resultado será visto imediatamente na aplicação, não há necessidade de sempre parar tudo e rodar novamente.


#### Projeto do servidor

- Dependências necessárias para a execução:
  - JDK 16
  - IDE Eclipse *(Configurada para JDK 16 como padrão)*
  - PostgreSQL 13
    - É necessário criar um banco de dados para o projeto

- Nota: Para o projeto do servidor, foi adotada a IDE Eclipse como padrão para seu desenvolvimento e execução;

- ***IMPORTANTE:*** O servidor necessita de um diretório no sistema para salvar os projetos do sistema e, para encontrá-lo, ele busca por uma **variável de ambiente** que contém o caminho para a pasta.
  - Crie uma **variável de ambiente** com o exato nome: **ProjectsWorkFolder** e em seu valor aponte para uma pasta de sua escolha.

##### Passos
- Na barra superior do Eclipse, clique em File > Open Projects from File System...;

- Clique em Directory, uma janela irá abrir. Navegue até o seguinte caminho: ..\AirplaneDocGenerator\src\server, você verá uma pasta chamada AirplaneManualGenerator, selecione-a e confirme;
 
- Clique em Finish;

- Como o projeto tem o Maven como gerenciador de dependências, após clicar em Finish você deve notar no canto inferior direito que o Eclipse estará baixando e instalando as dependências para você. Caso não apareça nada, selecione a parte do projeto no "Project Explorer" e aperte F5. Aguarde a instalação das dependências;

- Em ..\src\main\resources você encontrará o arquivo application.properties:
  - Na linha `spring.datasource.url=jdbc:postgresql://localhost:5432/<nome_do_banco>`, coloque o nome do banco de dados 
  - Na linha `spring.datasource.username=<nome_de_usuario>`, coloque seu nome de usuário do banco de dados
  - Na linha `spring.datasource.password=<senha>`, coloque sua senha do banco de dados

- No pacote "api.crabteam", execute o arquivo AirplaneManualGeneratorApplication;

- Você notará que diversas saídas aparecerão no console, caso nenhuma exceção seja lançada, o projeto foi configurado adequadamente!

- **Atenção**, o projeto exige a versão 16 do Java, certifique-se de que o padrão do seu Eclipse esteja configurado para o Java 16, caso contrário, vá ao Build Path do projeto e altere manualmente;


#### Considerações

Os projetos são, de certa forma, independentes, e podem ser executados separadamente, contudo, o cliente envia requisições ao servidor, então para o desenvolvimento e utilização adequada é necessário que ambos estejam em execução (o servidor no Eclipse e o cliente no VSCode).

No cliente, somente mudanças nos componentes do React são atualizados automaticamente e renderizados na tela após salvar os arquivos, caso alguma alteração seja feita em algo do Electron, será necessário parar a execução no terminal e iniciar novamente para que as mudanças sejam aplicadas.
