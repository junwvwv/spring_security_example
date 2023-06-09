package com.securityexample.config;

import com.securityexample.filter.AuthenticationLoggingFilter;
import com.securityexample.filter.CsrfTokenLogger;
import com.securityexample.filter.RequestValidationFilter;
import com.securityexample.repository.CsrfTokenRepositoryCustom;
import com.securityexample.service.AuthenticationProviderService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;
import java.util.List;

@Configuration
public class SecurityConfig {

    //시큐리티 5.7 부터 WebSecurityConfigurerAdapter Deprecated, Bean 등록으로 구성 해야함

    /**
     * ProviderManager - AuthenticationManager 의 구현체로, AuthenticationProvider 를 사용하여 사용자 인증 처리
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationProviderService service) throws Exception {
        return new ProviderManager(Collections.singletonList(service));
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http.httpBasic();
//        http.formLogin().defaultSuccessUrl("/");
        http.authorizeHttpRequests().anyRequest().permitAll();
//        http.authorizeHttpRequests().anyRequest().authenticated();
//        http.authorizeHttpRequests()
//                .requestMatchers("/authority/read").hasAuthority("read")
//                .requestMatchers("/authority/write").hasAnyAuthority("read", "write")
//                .requestMatchers("/role/admin").hasRole("ADMIN")
//                .anyRequest().authenticated();
//        http.authorizeHttpRequests()
//                .requestMatchers(HttpMethod.GET, "/item").permitAll()
//                .requestMatchers(HttpMethod.POST, "/item").authenticated()
//                .requestMatchers(HttpMethod.DELETE, "/item").denyAll();

        //인증 필터 전 후로 커스텀 필터 추가
//        http.addFilterBefore(new RequestValidationFilter(), BasicAuthenticationFilter.class)
//            .addFilterAfter(new AuthenticationLoggingFilter(), BasicAuthenticationFilter.class);
        /**
         * 스프링부트 3.0 부터(시큐리티 6.0)
         * authorizeRequests => authorizeHttpRequests
         * antMatchers, mvcMatchers, regexMatchers => requestMatchers
         */
//        http.authorizeHttpRequests()
//                .requestMatchers("/v1/item/{param}").permitAll() //파라미터가 포함된 요청만 허가
//                .requestMatchers("/v2/item/{param:^[0-9]*$}").permitAll() //파라미터에 숫자가 포함된 요청만 허가
//                .requestMatchers("/v1/order").authenticated()
//                .anyRequest().denyAll();

//
//        http.csrf(c -> { //Customizer<CsrfConfigurer<HttpSecurity>> 객체를 이용해 새로운 CsrfTokenRepository 구현을 CSRF 보호 매커니즘에 연결
//            c.csrfTokenRepository(csrfTokenRepository());
//            c.ignoringRequestMatchers("/v2/product");
//        });
//        http.cors(c -> { //CORS 구성 정의, 허용되는 출처 및 메소드를 설정하는 CorsConfiguration 객체를 생성. 컨트롤러에서 @CrossOrigin 으로 엔드포인트 구성 가능
//            CorsConfigurationSource source = request -> {
//                CorsConfiguration config = new CorsConfiguration();
//                config.setAllowedOrigins( //허용 Origin 설정
//                        List.of("api.example.com", "api.example.org")
//                );
//                config.setAllowedMethods( //허용 Http Method 설정
//                        List.of("GET", "POST", "PUT", "DELETE")
//                );
//                return config;
//            };
//            c.configurationSource(source);
//        });
//        http.csrf().disable();

        return http.build();
    }

    @Bean
    public CsrfTokenRepository csrfTokenRepository(){
        return new CsrfTokenRepositoryCustom();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        //BCryptPasswordEncoder - bcrypt 강력 해싱 함수로 암호를 인코딩
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SCryptPasswordEncoder sCryptPasswordEncoder(){
        return new SCryptPasswordEncoder(16384, 9, 1, 64, 16);
    }

//    @Bean
//    public UserDetailsService userDetailsService(){
//        String encodedPwd = new BCryptPasswordEncoder().encode("1234");
//        List<UserDetails> users = List.of( User.withUsername("junwvwv")
//                .password(encodedPwd)
//                .authorities("read")
//                .build());
//
//        return new InMemoryUserDetailsService(users);
//    }

}
