package com.dhenton9000.google.cal.api;

import java.security.Principal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.filter.CompositeFilter;

@SpringBootApplication
@RestController
//@EnableOAuth2Client
@EnableOAuth2Sso
public class GoogleCalApApplication extends WebSecurityConfigurerAdapter {

    @Autowired
    OAuth2ClientContext oauth2ClientContext;

    @RequestMapping({"/user", "/me"})
    public Map<String, String> user(Principal principal) {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("name", principal.getName());
        return map;
    }

    /**
     * configure security 
     * @param http
     * @throws Exception 
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
         
        http.antMatcher("/**").authorizeRequests().antMatchers( "/login**","/logout**","css/**", "/js/**", "/webjars/**").permitAll().anyRequest()
                .authenticated().and().exceptionHandling()
                .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login/google")) 
                .and()             
                .logout()
                .logoutSuccessUrl("/login/google").permitAll().and().csrf() 
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()).and()
                .addFilterBefore(ssoFilter(), BasicAuthenticationFilter.class);
     
         
         
    }


    public static void main(String[] args) {
        SpringApplication.run(GoogleCalApApplication.class, args);
    }


    @Bean
    @ConfigurationProperties("google")
    public ClientResources google() {
        return new ClientResources();
    }

    /**
    
     * @return 
     */
    private Filter ssoFilter() {
        /*
        used if there were more than one login provider
         CompositeFilter filter = new CompositeFilter();
         List<Filter> filters = new ArrayList<>();
         filters.add(ssoFilter(google(), "/login/google"));
        
         filter.setFilters(filters);
         return filter;
        */
        return ssoFilter(google(), "/login/google");
    }

    private Filter ssoFilter(ClientResources client, String path) {
        OAuth2ClientAuthenticationProcessingFilter oAuth2ClientAuthenticationFilter = new OAuth2ClientAuthenticationProcessingFilter(
                path);
        OAuth2RestTemplate oAuth2RestTemplate = new OAuth2RestTemplate(client.getClient(), oauth2ClientContext);
        oAuth2ClientAuthenticationFilter.setRestTemplate(oAuth2RestTemplate);
        UserInfoTokenServices tokenServices = new UserInfoTokenServices(client.getResource().getUserInfoUri(),
                client.getClient().getClientId());
        tokenServices.setRestTemplate(oAuth2RestTemplate);
        oAuth2ClientAuthenticationFilter.setTokenServices(tokenServices);
        return oAuth2ClientAuthenticationFilter;
    }

}

