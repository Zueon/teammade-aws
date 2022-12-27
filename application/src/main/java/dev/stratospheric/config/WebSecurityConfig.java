package dev.stratospheric.config;



import dev.stratospheric.security.JwtAuthenticationFilter;
import dev.stratospheric.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.jwk.JwkTokenStore;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Collections;


@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE + 10)
@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final JwtTokenProvider jwtTokenProvider;

    private final ResourceServerProperties resource;



    @Bean
    public SecurityFilterChain filterChain(HttpSecurity security) throws Exception {
        security.cors() // WebMvcConfig에서 이미 설정했으므로 기본 cors 설정.
                .and()
                .csrf()// csrf는 현재 사용하지 않으므로 disable
                .disable()
                .httpBasic()// token을 사용하므로 basic 인증 disable
                .disable()
                .sessionManagement()  // session 기반이 아님을 선언
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests() // /와 /auth/** 경로는 인증 안해도 됨.
                .antMatchers("/","/api/**","/auth/**", "/project/list", "/study").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);

        return security.build();

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
  public TokenStore jwtTokenStore(){
      return new JwkTokenStore(
        Collections.singletonList(resource.getJwk().getKeySetUri()),
        new CognitoAccessTokenConverter(),
        null
      );
    }
}
