package io.qcheng.cloud.authentication.security;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

/*
 * The OAuth2Config class defines what applications and 
 * the user credentials the OAuth2 service knows about.
 */
@Configuration
public class JWTOAuth2Config extends AuthorizationServerConfigurerAdapter {

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserDetailsService userDetailsService;

	@Autowired
	TokenStore tokenStore;
	
    @Autowired
    private DefaultTokenServices tokenServices;

	@Autowired
	JwtAccessTokenConverter jwtAccessTokenConverter;

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(jwtAccessTokenConverter));
		endpoints.tokenStore(tokenStore) // The token store we defined will be injected here
				.accessTokenConverter(jwtAccessTokenConverter) // This is the hook to tell the Spring Security OAuth2
																// code to use JWT
				.tokenEnhancer(tokenEnhancerChain)
				.authenticationManager(authenticationManager).userDetailsService(userDetailsService);
	}

	/*
	 * This defines which clients are going to be registered with your
	 * authentication service
	 * 
	 * @see org.springframework.security.oauth2.config.annotation.web.configuration.
	 * AuthorizationServerConfigurerAdapter#configure(org.springframework.security.
	 * oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer)
	 */
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		/*
		 * authorizedGrantTypes() is passed a comma-separated list of the authorization
		 * grant types that will be supported by your OAuth2 service The scopes() method
		 * is used to define the boundaries that the calling application can operate in
		 * when they’re asking your OAuth2 server for an access token. By defining the
		 * scope, you can write authorization rules specific to the scope the client
		 * application is working in.
		 */
		clients.inMemory().withClient("device").secret("thisissecret")
				.authorizedGrantTypes("refresh_token", "password", "client_credentials")
				.scopes("webclient", "mobileclient");
	}

}
