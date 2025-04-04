package co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;

/**
 * Configuration for customizing the HTTP Firewall to be more permissive
 * with special characters in request parameters and URLs.
 */
@Configuration
public class HttpFirewallConfig {

    @Bean
    public HttpFirewall allowUrlEncodedCharactersFirewall() {
        StrictHttpFirewall firewall = new StrictHttpFirewall();

        // Allow semicolons in URLs and parameters
        firewall.setAllowSemicolon(true);

        // Allow URL encoded slash (needed for complex JSON parameters)
        firewall.setAllowUrlEncodedSlash(true);

        // Allow backslash (often found in JSON strings)
        firewall.setAllowBackSlash(true);

        // Allow percent encoding (often used in complex request parameters)
        firewall.setAllowUrlEncodedPercent(true);

        // Allow potentially dangerous characters that might appear in valid JSON
        firewall.setAllowedHeaderNames(header -> true);
        firewall.setAllowedHeaderValues(header -> true);
        firewall.setAllowedParameterNames(parameter -> true);

        return firewall;
    }
}