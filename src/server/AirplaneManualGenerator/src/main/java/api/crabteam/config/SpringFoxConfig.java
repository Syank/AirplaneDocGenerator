package api.crabteam.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;




@Configuration
@EnableSwagger2
public class SpringFoxConfig extends WebMvcConfigurationSupport{


    @Bean
    public Docket swagger(){
        return new Docket(DocumentationType.SWAGGER_2)
        .useDefaultResponseMessages(false)
        .select()
        .apis(RequestHandlerSelectors.basePackage("api.crabteam.controllers"))
        .paths(PathSelectors.any())
        .build()
        .apiInfo(apiInfo());
    }

    
    private ApiInfo apiInfo(){
        return new ApiInfoBuilder()
            .title("AirplaneDocGenerator API")
            .description("API criada para o projeto integrador do grupo CRAB do 4º Semestre de ADS da FATEC São José dos Campos - Prof. Jessen Vidal")
            .version("1.0")
            .build();
    }

   
}