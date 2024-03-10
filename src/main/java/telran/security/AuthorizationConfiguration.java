package telran.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
public class AuthorizationConfiguration {

	@Bean
	public SecurityFilterChain configure(HttpSecurity http) throws Exception {
		
		http.httpBasic();
		http.csrf().disable();
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		
		http.addFilterAfter(new RevokedAccountFilter(), BasicAuthenticationFilter.class);
		http.addFilterAfter(new ExpiredPasswordFilter(), RevokedAccountFilter.class);

		
		http.authorizeHttpRequests(authorize -> authorize
				
			//Accounting filters	
			.requestMatchers("/account/register")
				.permitAll()
			.requestMatchers("/account/user/*/role/*", "/account/revoke/*", "/account/activate/*")
				.hasRole("ADMIN")
			.requestMatchers(HttpMethod.PUT, "/account/user/{login}")
				.access(new WebExpressionAuthorizationManager("#login == authentication.name")) //только владелец записи
			.requestMatchers(HttpMethod.DELETE, "/account/user/{login}")
				.access(new WebExpressionAuthorizationManager("#login == authentication.name or hasRole('ADMIN')"))
			.requestMatchers(HttpMethod.GET, "/account/*/{login}")
				.access(new WebExpressionAuthorizationManager("#login == authentication.name or hasRole('ADMIN')"))
			.requestMatchers(HttpMethod.PUT, "/account/user/{login}")
				.access(new WebExpressionAuthorizationManager("@customWebSecurity.checkOwner(#login)"))
				
			//RentCompany filters
			.requestMatchers("/records/*/*")
				.hasRole("TECHNICIAN")
			.requestMatchers("/models/popular/*/*/*/*", "/drivers/active", "/models/profitable/*/*")
				.hasRole("STATIST")
			.requestMatchers("/car/*/drivers", "/driver/*", "/driver/*/cars")
				.hasAnyRole("DRIVER", "CLERK")
			.requestMatchers("/car/return", "/driver/add", "/model/*/cars", "/car/rent")
				.hasRole("CLERK")
			.requestMatchers("/model/remove/*", "/car/remove/*", "/car/add", "/model/add")
				.hasRole("MANAGER")
			
			.requestMatchers("/model", "/models")
				.permitAll()
			.requestMatchers("/car/*")
				.authenticated()	
			
				
			.anyRequest()
				.denyAll());
	
		
		return http.build();
	}
	
}
