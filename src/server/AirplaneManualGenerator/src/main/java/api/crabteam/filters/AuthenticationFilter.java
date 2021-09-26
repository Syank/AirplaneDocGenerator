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

import org.springframework.stereotype.Component;

/**
 * Classe de filtro de requisições que verifica se a requisição recebida vem de um usuário autenticado, ou seja,
 * logado no sistema
 * 
 * @author Rafael Furtado
 */
@Component
public class AuthenticationFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = ((HttpServletRequest) request);
		HttpSession session = httpRequest.getSession(false);
		
		String requestPath = httpRequest.getRequestURI();
		
		boolean authentication = requestPath.contains("/authentication/login");
		boolean checkLogged = requestPath.equals("/user/isLogged");
		boolean preflight = httpRequest.getMethod().equalsIgnoreCase("OPTIONS");
		boolean swagger = requestPath.contains("/swagger-ui");

		boolean allowRequest = false;
		
		if(session == null) {
			if(preflight) {
				allowRequest = true;
				
			}else if(authentication || checkLogged || swagger) {
				allowRequest = true;
				
			}
			
		}else {
			allowRequest = true;
			
		}
		
		if(allowRequest) {
			chain.doFilter(request, response);
			
		}else {
			HttpServletResponse httpResponse = (HttpServletResponse) response;
			
			httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Solicitação de serviço negada, autentique-se e tente novamente");

		}
		
	}

}
