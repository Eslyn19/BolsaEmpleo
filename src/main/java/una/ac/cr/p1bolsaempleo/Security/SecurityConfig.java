package una.ac.cr.p1bolsaempleo.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth

                        // Públicas (sin login)
                        .requestMatchers(
                                "/", "/inicio",
                                "/login", "/login/**", "/registro",
                                "/userform",
                                "/empresa", "/empresa/", "/empresa/**",
                                "/css/**", "/js/**", "/images/**", "/webjars/**", "/favicon.ico"
                        ).permitAll()

                        // Controlado por sesión (no Spring Security roles)
                        .requestMatchers("/admin/**", "/Dashboard", "/dashboard/**", "/DashboardOferente", "/oferente/**").permitAll()

                        // Cualquier otra requiere autenticación
                        .anyRequest().authenticated()
                )

                .formLogin(login -> login
                        .loginPage("/login")
                        .defaultSuccessUrl("/dashboard", true)
                        .permitAll()
                )

                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/inicio")
                );

        return http.build();
    }
}
