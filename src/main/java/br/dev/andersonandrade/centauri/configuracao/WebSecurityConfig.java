package br.dev.andersonandrade.centauri.configuracao;

import br.dev.andersonandrade.centauri.repository.UsuarioRepository;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(requests -> requests

                        .requestMatchers(PathRequest
                                .toStaticResources()
                                .atCommonLocations()).permitAll()
                        .requestMatchers("/").permitAll()

                        .requestMatchers("/dislike/**", "/like/**").permitAll()
                        .requestMatchers("/imagens/publicacao/**").permitAll()
                        .requestMatchers("/static/**", "/css/**", "/js/**", "/fontaweasome/**").permitAll()
                        .requestMatchers("/cadastro", "/cadastro/ativar", "cadastro/verificarNomeUsuario").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form.loginPage("/login")
                        .successForwardUrl("/minha-pagina")
                        .permitAll()
                )
                .logout(LogoutConfigurer::permitAll);
        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DetalhesUsuarioService detalhesUsuarioService(UsuarioRepository usuarioRepository) {
        return new DetalhesUsuarioService(usuarioRepository);
    }

}


