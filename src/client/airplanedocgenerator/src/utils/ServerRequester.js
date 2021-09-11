/**
 * Classe responsável por administrar e facilitar todas as requisições RESTs feitas ao servidor
 * 
 * @author Rafael Furtado
 */
 class ServerRequester{
    constructor(){
        // URL do servidor
        this.serverURL = window.location.protocol + "//" + window.location.hostname + ":8080" ;

    }
    


    /**
     * Função para realizar requisições do tipo GET ao servidor da aplicação
     * 
     * @param {String} restPath Caminho do servidor REST para acessar o serviço, no formato "/caminho/do/servico"
     * @param {JSON} parameters JSON contendo os parâmetros para a requisição, onde a chave deve ser o nome do atributo
     *                          e o valor, o seu valor a ser associado na URL
     * @returns Retorna um objeto JSON contendo a resposta da requisição, com as seguintes chaves disponíveis:
     *  - ok -> Boolean - Informa se a requisição teve sucesso ou não, 
     *  - status -> Number - Código HTTP informando o status da requisição
     *  - responseJson -> JSON - Objeto JSON retornado do servidor
     */
    async doGet(restPath, parameters){
        let url = this.serverURL + restPath;

        let requestConfigs = {
            method: "GET"
        }

        // Verifica se foram passados parâmetros para a requisição
        if(parameters !== undefined){
            // Cria um array com as chaves do JSON parametros
            let parametersKeys = Object.keys(parameters);

            // Adiciona "?" após a URL, para iniciar a inserção dos parâmetros da requisição
            url += "?";

            // Itera pelos parâmetros e os adiciona à URL
            for (let i = 0; i < parametersKeys.length; i++) {
                const chave = parametersKeys[i];
                
                url += chave + "=" + parameters[chave] + "&";

            }

            // Retira o último "&" da url de requisição
            url = url.slice(0, url.length - 1);

        }

        // Faz a requisição para a URL construída e obtêm sua resposta como JSON
        return await this.doRequest(url, requestConfigs);
    }

    /**
     * Faz uma requisição POST para o servidor, para o serviço REST específicado e com os dados fornecidos
     * 
     * @author Rafael Furtado
     * @param {String} caminhoRest - Rota de acesso ao serviço REST, por exemplo /rest/texto
     * @param {JSON} data - JSON com parâmetros para a requisição, no formato chave:valor
     * @returns Retorna um objeto JSON contendo a resposta da requisição, com as seguintes chaves disponíveis:
     *  - ok -> Boolean - Informa se a requisição teve sucesso ou não, 
     *  - status -> Number - Código HTTP informando o status da requisição
     *  - responseJson -> JSON - Objeto JSON retornado do servidor
     */
    async doPost(restPath, data = {}){
        let url = this.serverURL + restPath;

        let requestConfigs = {
                            method: "POST",
                            body: JSON.stringify(data)
                        };

        // Faz a requisição para a URL construída e obtêm sua resposta como JSON
        return await this.doRequest(url, requestConfigs);
    }

    /**
     * MÉTODO PRIVADO
     * 
     * Utilizado pela classe ServerRequester para reaproveitar a parte igual que as
     * requisições tem, independente do método
     * 
     * @author Rafael Furtado
     * @param {String} url - URL para fazer a solicitação
     * @param {Object} configs - Configurações para o método de solicitação
     * @returns JSON - Retorna um objeto JSON contendo a resposta do servidor para o serviço solicitado 
     */
    async doRequest(url, requestConfigs){
        let request = await fetch(url, requestConfigs);

        let response = {};

        response["ok"] = request.ok;
        response["status"] = request.status;

        try {
            let responseJson = await request.json();

            response["responseJson"] = responseJson;

        } catch (error){
            response["responseJson"] = error.message;

        }

        return response;
    }

}

export default ServerRequester;