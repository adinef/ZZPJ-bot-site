package pl.lodz.p.it.zzpj.botsite.config.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import pl.lodz.p.it.zzpj.botsite.entities.User;
import pl.lodz.p.it.zzpj.botsite.web.dto.MyUserDetails;

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

    @Override
    public Long getUserId() { return ((MyUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId(); }

    @Override
    public User getUser() {
        return ((MyUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
    }
}
