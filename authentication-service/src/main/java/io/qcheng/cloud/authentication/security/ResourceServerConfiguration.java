package io.qcheng.cloud.authentication.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

import io.qcheng.cloud.authentication.dto.Role;

@Configuration
@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true) //to enable method-level security
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {
	
	@Override
    public void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers("/h2/**").permitAll()
                .antMatchers(HttpMethod.POST, "/v1.0/user").permitAll()
                .antMatchers(HttpMethod.DELETE, "/v1.0/user/**").hasRole(Role.ADMIN.toString()).anyRequest().authenticated();
            
        //https://stackoverflow.com/questions/45382328/connecting-to-h2-database-from-h2-console
    	//Disables header security. This allows the use of the h2 console.
        http.headers().frameOptions().disable();
    }

}
