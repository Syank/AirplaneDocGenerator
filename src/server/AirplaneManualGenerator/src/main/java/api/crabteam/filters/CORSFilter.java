package api.crabteam.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;



/**
 * Filtro de política de CORS, para permitir que a aplicação cliente receba a resposta do servidor
 * 
 * @author Rafael Furtado
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CORSFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		
		httpResponse.addHeader("Access-Control-Allow-Origin", "http://localhost:3000");
		httpResponse.addHeader("Access-Control-Allow-Headers", "Content-Type");
		httpResponse.addHeader("Access-Control-Allow-Headers", "Set-Cookie");
		httpResponse.addHeader("Access-Control-Allow-Credentials", "true");
		
		
		chain.doFilter(request, response);
		
	}

}
