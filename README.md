<html>
       <head></head>
       <body>
              <h1 align="center">AirplaneDocGenerator :airplane::card_index_dividers:</h1>
              <p align="center">
                     <img src="https://img.shields.io/badge/Electron-2B2E3A?style=for-the-badge&logo=electron&logoColor=9FEAF9">
                     <img src="https://img.shields.io/badge/React-20232A?style=for-the-badge&logo=react&logoColor=61DAFB">
                     <img src="https://img.shields.io/badge/HTML-239120?style=for-the-badge&logo=html5&logoColor=white">
                     <img src="https://img.shields.io/badge/Tailwind_CSS-38B2AC?style=for-the-badge&logo=tailwind-css&logoColor=white">
                     <img src="https://img.shields.io/badge/Spring_Boot-F2F4F9?style=for-the-badge&logo=spring-boot">
                     <img src="https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white">
              </p>
              <section id="introducao">
                     <h2>AirplaneDocGenerator :airplane::card_index_dividers::computer:</h2>
                     <p align="justify">Nosso cliente possui a necessidade de automatizar a geração de manuais para as suas aeronaves, pois, atualmente, o processo é feito de forma manual, o que pode tomar muito tempo em razão da complexidade para a montagem deles e, também, pode aumentar as chances de erros. Nossa aplicação tem como objetivo resolver esses problemas através de uma interface simples e intuitiva, que agilizará o processo.</p>
              </section>
              <h3>:pushpin: Índice</h3>
              <ul>
                     <li><a href="#introducao">O que é o AirplaneDocGenerator?</a>
                     <ul>
                            <li><a href="#entrega">Terceira entrega</a>
                            <li><a href="#funcionamento">AirplaneDocGenerator em funcionamento</a>
                            <li><a href="#backlogEntrega"><i>User stories</i> entregues</a>
                            <li><a href="#mockups"><i>Mockups</i></a>
                            <li><a href="#banco-conceitual">Modelo conceitual do banco de dados</a>
                            <li><a href="#banco-logico">Modelo lógico do banco de dados</a>
                            <li><a href="#banco-dic-dados">Dicionário de dados</a>
                            <li><a href="#burndown"><i>Burndown</i></a>
                     </ul>
                     <li><a href="#equipe">Integrantes da equipe</a>
              </ul>
              <section id="entrega">
                     <h2 align="center">:rainbow::spiral_calendar: Terceira entrega :star:</h3>
                     <h3>:question: O que fizemos?</h2>
                     <p align="justify">Na terceira entrega desenvolvemos a funcionalidade de criar revisões de um projeto de manual, exportar e importar projetos como um todo (com toda a sua estrutura de pastas, revisões etc.) e, também, a possibilidade de exportar a <i>Codelist</i> do projeto de manual.</p>
                     <img src="https://raw.githubusercontent.com/Syank/AirplaneDocGenerator/main/doc/cards/sprint%203/card03.png" width="550px" height="300px">
                     <h3>:grey_question: Por quê?</h2>
                     <p align="justify">Muitos projetos de manual encontram-se prontos e, por conta disso, para oferecer maior portabilidade ao nosso cliente, desenvolvemos as funcionalidades de importação e exportação de projetos e, também, a exportação da <i>Codelist</i> do projeto de manual. O usuário pode escolher um projeto, que tenha uma estrutura válida, e todo o nosso sistema se encarregará de importá-lo para que a sua gerência, muito mais intuitiva e rápida, seja permitida. Além disso, as revisões são importantes para a posterior geração da Lista de Páginas Efetivas (LEP), que mostra todas as alterações entre as revisões de um projeto de manual. Dessa forma, o usuário, em poucos cliques, otimizará muito tempo e evitará erros!</p>
              </section>
              <section id="funcionamento">
                     <h2 align="center">:desktop_computer::computer_mouse: AirplaneDocGenerator em funcionamento :checkered_flag:</h3>
                     <p align="center">
                            <img src="https://github.com/Syank/AirplaneDocGenerator/blob/documentation/doc/gifs/sprint3/Apresenta%C3%A7%C3%A3o%20Sprint%203%20GIF.gif">
                     </p>
              </section>
              <section id="backlogEntrega">
                     <h2 align="center"><i>User stories</i> entregues :pilot:</h3>
                     <p align="justify">A terceira sprint contou com o desenvolvimento do cadastro de revisões, a exportação de uma <i>Codelist</i> e a importação/exportação de um projeto de manual.</p>
                     <img src="https://raw.githubusercontent.com/Syank/AirplaneDocGenerator/main/doc/backlog/sprint%203/Sprint%203%20cropped.png">
              </section>
              <section id="mockups">
                     <h2 align="center">:bookmark_tabs: <i>Mockups</i> :memo:</h3>
                     <img src="https://raw.githubusercontent.com/Syank/AirplaneDocGenerator/main/doc/mockups/sprint%203/UploadProjeto.png">
              </section>
              <section id="banco-conceitual">
                     <h2 align="center">Modelo conceitual do banco de dados :open_file_folder:</h3>
                     <img src="https://raw.githubusercontent.com/Syank/AirplaneDocGenerator/main/doc/database/sprint%203/conceitual/conceitual.png">
              </section>
              <section id="banco-logico">
                     <h2 align="center">:jigsaw: Modelo lógico do banco de dados :open_file_folder:</h3>
                     <img src="https://raw.githubusercontent.com/Syank/AirplaneDocGenerator/main/doc/database/sprint%203/logico/logico.png">
              </section>
              <section id="banco-dic-dados">
                     <h2 align="center">:notebook_with_decorative_cover: Dicionário de dados :open_file_folder:</h3>
                     <p align="justify">Caso necessite entender melhor sobre algum atributo, relacionamento ou tabela do nosso banco de dados, você pode consultar o nosso dicionário de dados <a href="https://github.com/Syank/AirplaneDocGenerator/blob/main/doc/database/sprint%203/dicionario/dicion%C3%A1rio%20de%20dados.pdf">aqui</a>!</p>
              </section>
              <section id="burndown">
                     <h2 align="center"><i>Burndown</i> :date::chart_with_downwards_trend:</h3>
                     <p align="justify">Tínhamos planejado, também, para a terceira sprint, entregar a geração da LEP, mas como alguns imprevistos ocorreram, o entendimento dessa funcionalidade foi um pouco mais trabalhoso e quando entendemos como fazer, já estávamos nos últimos dias da <i>sprint</i>. Como conclusão, entregaremos essa funcionalidade na 4ª sprint, pois priorizamos a qualidade.</p>
		     <p align="center"><img src="https://raw.githubusercontent.com/Syank/AirplaneDocGenerator/main/doc/burndown/sprint%203/burndown-s3.png"></p>
              </section>
              <section id="equipe">
                     <h2>Integrantes da equipe :girl::boy:</h2>
                     <ul>
                            <li><a href="https://www.linkedin.com/in/b%C3%A1rbara-port-402158198/">Bárbara dos Santos Port</a> (<i>Scrum Master</i>)
                            <li><a href="https://www.linkedin.com/in/rafael-furtado-613a9712a/">Rafael Furtado Rodrigues dos Santos</a> (<i>Product Owner</i>)
                            <li><a href="https://www.linkedin.com/in/alberto-de-mattos-piedade-neto-2b758035/">Alberto de Mattos Piedade Neto</a> (<i>Development Team</i>)
			    <li><a href="https://www.linkedin.com/in/anna-yukimi-yamada-6ba23b149/">Anna Yukimi Yamada</a> (<i>Development Team</i>)
                            <li><a href="https://www.linkedin.com/in/carolina-margiotti-703897193/">Carolina Margiotti de Abreu</a> (<i>Development Team</i>)
                            <li><a href="https://www.linkedin.com/in/francisco-cardoso-1954651b2/">Francisco Norberto Cardoso Neto</a> (<i>Development Team</i>)
                     </ul>
              </section>
       </body>
</html>
