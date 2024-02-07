package com.example.registrationlogindemo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SpringSecurity {

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public static PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeHttpRequests((authorize) ->
                        authorize.requestMatchers("/register/**").permitAll() // Los dos asteriscos permiten ver t0do lo que hay detrás de register/LoQueSea
                                .requestMatchers("/index", "/", "/inicio", "/home").permitAll()
                                .requestMatchers("/detalle/**").permitAll()
                                .requestMatchers("/detalle/submit").permitAll()

                                .requestMatchers("/crud/articulos").authenticated()
                                .requestMatchers("/crud/articulos/altas").authenticated()
                                .requestMatchers("/crud/articulos/altas/submit").authenticated()

                                // .requestMatchers("/crud/articulos/modificar").authenticated() // estaba mal, corregido en la siguiente línea: con /** lo hace bien, mejor que /{id}
                                .requestMatchers("/crud/articulos/modificar/**").authenticated()
                                .requestMatchers("/crud/articulos/modificar/submit").authenticated()

                                // añadido el borrar (no estaba hecho, no se podían borrar los post, no accedía).
                                .requestMatchers("/crud/articulos/eliminar/**").authenticated()

                                .requestMatchers("/articulos-creados/**").authenticated()

                                // Añadido esto para las imágenes
                                // .requestMatchers("/files/**").authenticated() puesto el .permitAll() porque sino en el inicio no pueden ver las imágenes, solo si estás logeado.
                                .requestMatchers("/files/**").permitAll()

                                .requestMatchers("/users").hasRole("ADMIN")
                ).formLogin(
                        form -> form
                                .loginPage("/login")
                                .loginProcessingUrl("/login")
                                // Estaba esta ruta mal, que nos mandaba a /users cuando nos registrábamos (corregido a "/index")
                                // .defaultSuccessUrl("/users")
                                .defaultSuccessUrl("/index")
                                .permitAll()
                ).logout(
                        logout -> logout
                                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                                .permitAll()
                );
        return http.build();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }
}
