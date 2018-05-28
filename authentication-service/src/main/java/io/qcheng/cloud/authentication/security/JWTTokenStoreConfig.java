package io.qcheng.cloud.authentication.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import io.qcheng.cloud.authentication.config.ServiceConfig;

/*
 * Define how Spring will manage the creation, signing, and translation of a JWT token.
 */
@Configuration
public class JWTTokenStoreConfig {
	
	@Autowired
	private ServiceConfig serviceConfig;

	@Bean
	public TokenStore tokenStore() {
		return new JwtTokenStore(jwtAccessTokenConverter());
	}

	// Used to read data to and from a token presented to the service
	@Bean	
	@Primary
	public DefaultTokenServices tokenServices() {
		//use Spring security’s default token services implementation
		DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
		defaultTokenServices.setTokenStore(tokenStore());
		defaultTokenServices.setSupportRefreshToken(true);

		return defaultTokenServices;
	}

	//The method acts as the translator between JWT and OAuth2 server
	//
	//It defines how the token is going to be translated. The most important 
	//thing to note about this method is that you’re setting the signing key 
	//that will be used to sign your token.
	@Bean
	public JwtAccessTokenConverter jwtAccessTokenConverter() {
		JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
		
		//Defines the signing key that will be used to sign a token
		converter.setSigningKey(serviceConfig.getJwtSigningKey());
		
		return converter;
	}

//	@Bean
//	public TokenEnhancer jwtTokenEnhancer() {
//		return new JWTTokenEnhancer();
//	}

}
