/**
 * Classe responsável por administrar e facilitar todas as requisições RESTs feitas ao servidor
 * 
 * @author Rafael Furtado
 */
 class ServerRequester{
    constructor(){
        // URL do servidor
        this.serverURL = window.location.protocol + "//" + window.location.host;

    }
    


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
        return await this.fazerRequisicao(url, requestConfigs);
    }

    /**
     * Faz uma requisição POST para o servidor, para o serviço REST específicado e com os dados fornecidos
     * 
     * @author Rafael Furtado
     * @param {String} caminhoRest - Rota de acesso ao serviço REST, por exemplo /rest/texto
     * @param {Object} data - JSON com parâmetros para a requisição, no formato chave:valor
     * @returns JSON - Retorna um objeto JSON contendo a resposta do servidor para o serviço solicitado
     */
    async doPost(restPath, data = {}, contentType = "application/json"){
        let url = this.serverURL + restPath;

        let requestConfigs = {
                            method: "POST",
                            body: JSON.stringify(data),
                            headers: {
                                "Content-Type": contentType   
                                }
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
        let requisicao = await fetch(url, requestConfigs);

        let response = {};

        try{
            let responseJson = await requisicao.json();

            response["ok"] = requisicao.ok;
            response["status"] = requisicao.status;
            response["responseJson"] = responseJson;

        }catch(e){
            console.log("Requisição sem retorno");

        }

        return response;
    }

}

export default ServerRequester;