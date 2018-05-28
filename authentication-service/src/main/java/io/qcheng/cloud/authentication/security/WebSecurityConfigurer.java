package io.qcheng.cloud.authentication.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

import io.qcheng.cloud.authentication.service.DatabaseUserDetailsService;

/*
 * To configure your OAuth2 server to authenticate user IDs
 * 
 * Customize Spring Security’s default behavior and securing a URI 
 * by creating a WebSecurityConfigurer configuration
 * WebSecurityConfigurerAdapter class provides a helper method to configure Spring Security
 */
@Configuration
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {
	
	@Autowired
	DatabaseUserDetailsService databaseUserDetailService;

	// TODO:
	// 403: Could not verify the provided CSRF token because your session was not found
	//
	//CSRF is a type of security
	//vulnerability whereby a malicious web site forces users to execute unwanted commands on another web site
	//in which they are currently authenticated. By default, Spring Security enables CSRF protection and is highly
	//recommended when requests are submitted by a user via a browser. For services (REST) that are used by
	//nonbrowser clients, CSRF can be disabled.
//	@Override
//	protected void configure(HttpSecurity http) throws Exception {
//		http
//		    .csrf().disable()
//		    .authorizeRequests()
//		         .antMatchers(HttpMethod.POST, "/v1.0/user").permitAll()
//		         .antMatchers("/v1.0/user*").hasAnyRole(RoleType.ADMIN.toString(), RoleType.USER.toString())
//		         .antMatchers("/h2/**").permitAll();
//		
//		//https://stackoverflow.com/questions/45382328/connecting-to-h2-database-from-h2-console
//		//Disables header security. This allows the use of the h2 console.
//		http.headers().frameOptions().disable();
//	}

	/*
	 * The AuthenticationManagerBean is used by Spring Security to handle
	 * authentication
	 * 
	 * @see org.springframework.security.config.annotation.web.configuration.
	 * WebSecurityConfigurerAdapter#authenticationManagerBean()
	 */
	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	@Bean
	@Primary
	public UserDetailsService userDetailsServiceBean() throws Exception {
		return super.userDetailsServiceBean();
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
 //In memory
//		auth
//		.inMemoryAuthentication()
//		.withUser("qcheng")
//		.password("123456")
//		.roles("USER", "ADMIN");
		
		//AuthenticationManagerBuilder is a helper class that implements the Builder 
		//pattern and provides a way of assembling an AuthenticationManager.
		auth.userDetailsService(databaseUserDetailService);
	}

}
