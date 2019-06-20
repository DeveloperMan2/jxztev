package com.jxztev.swagger;

import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableWebMvc
//@ComponentScan(basePackages ={"com.jxztev.controller"})//启用Controller扫描,spring mvc配置文件中已经配置了，此处可以不需要再配置。
@EnableSwagger2
public class SwaggerConfig {

	@Bean
	public Docket buildDocket() {
		return new Docket(DocumentationType.SWAGGER_2).apiInfo(buildApiInf()).select()
				//.apis(RequestHandlerSelectors.basePackage("com.jxztev.controller"))// controller路径
				.paths(PathSelectors.any()).build();
	}

	private ApiInfo buildApiInf() {
		return new ApiInfoBuilder().title("JXSL改造API平台").termsOfServiceUrl("")
				.description("集API在线查看、测试于一体")
				.contact(new Contact("减灾事业部", "", "liangbimiao@piesat.cn")).build();

	}
}
