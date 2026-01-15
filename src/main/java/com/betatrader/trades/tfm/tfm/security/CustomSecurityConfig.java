package com.betatrader.trades.tfm.tfm.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static org.springframework.security.config.Customizer.withDefaults;


@Configuration
@EnableWebSecurity
public class CustomSecurityConfig{


    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.withUsername("user")
                .password("{noop}pass")
                .roles("USER")
                .build();
        UserDetails support = User.withUsername("supportuser")
                .password("{noop}pass")
                .roles("SUPPORT")
                .build();
        UserDetails upload = User.withUsername("uploaduser")
                .password("{noop}pass")
                .roles("UPLOAD")
                .build();
        return new InMemoryUserDetailsManager(user,support,upload);
    }



    /*@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

     *//*http.authorizeHttpRequests(
             auth ->{
                 auth.requestMatchers("/").permitAll();
                 auth.requestMatchers("/error","/error/**").permitAll();
                 //auth.anyRequest().authenticated();
             }

                             //.requestMatchers("/tfm/upload").hasRole("UPLOAD")
                             //.requestMatchers("/tfm/support").hasRole("SUPPORT")
                             //.requestMatchers("/tfm/user").hasRole("USER")
                             //.requestMatchers("/").permitAll()

        ).oauth2ResourceServer(oauth2 -> oauth2
             .jwt(Customizer.withDefaults()));*//*

        // .formLogin(withDefaults());

       *//* http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/public/**").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(Customizer.withDefaults())
                );
*//*

        return http.build();
    }*/

    // ===== 1) API security: JWT resource server on /api/** =====
   /* @Bean
    @Order(1)
    public SecurityFilterChain apiSecurity(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/**")
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/token").permitAll()   // <-- EXCLUDED FROM API CHAIN
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(
                        oauth2 -> oauth2.jwt(Customizer.withDefaults())
                )
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
    // ===== 2) Web security: GitHub OAuth2 login for everything else =====
    @Bean
    @Order(2)
    public SecurityFilterChain webSecurity(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/login", "/error").permitAll()
                        .requestMatchers("/api/token").authenticated()  // <-- GitHub login required
                        .anyRequest().authenticated()
                )
                .oauth2Login(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable());


        return http.build();
    }*/

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configure(http))
                .authorizeHttpRequests( auth -> auth
                //Public endpoints
                .requestMatchers("/","/login","/error").permitAll()
                // Must be logged in with Google to get a token
                .requestMatchers("/api/token").authenticated()
                 //All other /api/** need JWT or session auth
                .requestMatchers("/api/**").authenticated()
                 //Everything else also requires auth
                  .anyRequest().authenticated()
                )
                //Browser loging via google /gihub through Angular UI
                .oauth2Login(oauth -> oauth
                        .defaultSuccessUrl("http://localhost:4200/", true)
                )
                //Disabling all other forms of login
                //.oauth2Login(o -> o.disable())
                .formLogin(f -> f.disable())
                .httpBasic(h -> h.disable())

                //Allow both session-based auth and JWT based auth
                .oauth2ResourceServer(oauth2->
                        oauth2.jwt(Customizer.withDefaults())
                );
        return http.build();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                        .allowedOrigins("http://localhost:4200")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*");
            }
        };
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("http://localhost:4200");
        config.setAllowCredentials(true);
        config.addAllowedMethod("*");
        config.addAllowedHeader("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }


}
