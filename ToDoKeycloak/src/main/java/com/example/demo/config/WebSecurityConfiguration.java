package com.example.demo.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;

import java.util.*;
import java.util.stream.Collectors;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(authorizeRequests -> {
                    try {
                        authorizeRequests
                                .antMatchers("/v1/todo/login/**").permitAll()
                                .antMatchers("/v1/todo/create/**").permitAll()
                                .anyRequest().authenticated().and().csrf().disable();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                })
                .oauth2ResourceServer()
                .jwt()
                .jwtAuthenticationConverter(new JwtAuthenticationConverter()
                {
                    @Override
                    protected Collection<GrantedAuthority> extractAuthorities(final Jwt jwt)
                    {
                        Collection<GrantedAuthority> authorities = super.extractAuthorities(jwt);
                        Map<String, Object> resourceAccess = jwt.getClaim("realm_access");
                        Map<String, Object> resource = null;
                        List<String> list;
                        String s = resourceAccess.get("roles").toString().substring(1, resourceAccess.get("roles").toString().length() - 1);
                        String[] subStr;
                        String delimeter = ", "; // Разделитель
                        subStr = s.split(delimeter);
                        list = Arrays.stream(subStr).toList();
                        Collection<String> resourceRoles = list;
                        authorities.addAll(resourceRoles.stream()
                                .map(x -> new SimpleGrantedAuthority("ROLE_" + x))
                                .collect(Collectors.toSet()));
                        return authorities;
                    }
                });
    }

//    @Bean
//    public Converter<Jwt, AbstractAuthenticationToken> jwtAuthenticationConverter() {
//        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
//        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter());
//        return jwtAuthenticationConverter;
//    }
//
//    @Bean
//    public Converter<Jwt, Collection<GrantedAuthority>> jwtGrantedAuthoritiesConverter() {
//        JwtGrantedAuthoritiesConverter delegate = new JwtGrantedAuthoritiesConverter();
//
//        return new Converter<>() {
//            @Override
//            public Collection<GrantedAuthority> convert(Jwt jwt) {
//                Collection<GrantedAuthority> grantedAuthorities = delegate.convert(jwt);
//
//                if (jwt.getClaim("realm_access") == null) {
//                    return grantedAuthorities;
//                }
//                JSONObject realmAccess = jwt.getClaim("realm_access");
//                if (realmAccess.get("roles") == null) {
//                    return grantedAuthorities;
//                }
//                JSONArray roles = (JSONArray) realmAccess.get("roles");
//
//                final List<SimpleGrantedAuthority> keycloakAuthorities = roles.stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role)).collect(Collectors.toList());
//                grantedAuthorities.addAll(keycloakAuthorities);
//
//                return grantedAuthorities;
//            }
//        };
//    }
}
