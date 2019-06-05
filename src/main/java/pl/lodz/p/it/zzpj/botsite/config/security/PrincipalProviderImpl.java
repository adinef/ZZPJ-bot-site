package pl.lodz.p.it.zzpj.botsite.config.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.security.Principal;

@Component
public class PrincipalProviderImpl implements PrincipalProvider {
    @Override
    public Principal getPrincipal() {
        return (Principal)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @Override
    public String getName() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
