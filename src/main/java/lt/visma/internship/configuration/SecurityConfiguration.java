package lt.visma.internship.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import lt.visma.internship.services.PersonService;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
	@Autowired
	PersonService personService;
	
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	public WebSecurityCustomizer webSecurityCustomizer(AuthenticationManagerBuilder auth) throws Exception {
		BCryptPasswordEncoder bc = new BCryptPasswordEncoder();
		auth.userDetailsService(this.personService)
		.passwordEncoder(bc);
		return (web) -> {
			try {
				web.build();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		};
	}
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests((authz) -> {
			try {
				authz
				.antMatchers("/").permitAll()
				.antMatchers("/js/**", "/css/**").permitAll()
				.antMatchers("/api/deleteMeetingAttendee/*").permitAll()
				.antMatchers("/register").permitAll()
				.antMatchers("/meetingNew").permitAll()
				.antMatchers("/meeting/*").permitAll()
				.antMatchers("/api/meetings").permitAll()
				.antMatchers("/api/newMeeting").permitAll()
				.antMatchers("/login").permitAll()
				.anyRequest().permitAll()
				.and()
				.formLogin()
				.loginPage("/login")
				.usernameParameter("email").permitAll()
				.defaultSuccessUrl("/")
				.and().logout().logoutUrl("/logout")
				.logoutSuccessUrl("/");
				
			}catch(Exception e) {
				e.printStackTrace();
			}
		}).httpBasic().disable();
		return http.build();
	}
}
