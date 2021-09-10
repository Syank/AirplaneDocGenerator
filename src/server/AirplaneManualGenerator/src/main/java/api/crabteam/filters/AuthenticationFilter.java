package api.crabteam.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Classe de filtro de requisições que verifica se a requisição recebida vem de um usuário autenticado, ou seja,
 * logado no sistema
 * 
 * @author Rafael Furtado
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class AuthenticationFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpSession session = ((HttpServletRequest) request).getSession(false);
		
		// Caso a sessão seja nula, envia uma resposta de erro ao cliente e impede que a requisição prossiga
		if(session == null) {
			HttpServletResponse httpResponse = (HttpServletResponse) response;
			
			httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Solicitação de serviço negada, autentique-se e tente novamente");
			
		// Caso a sessão não seja nula, continua o fluxo normal da requisição
		}else {
			chain.doFilter(request, response);
			
		}
		
	}

}
