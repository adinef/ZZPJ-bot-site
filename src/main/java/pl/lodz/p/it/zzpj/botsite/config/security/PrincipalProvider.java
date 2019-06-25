package pl.lodz.p.it.zzpj.botsite.config.security;

import pl.lodz.p.it.zzpj.botsite.entities.User;

import java.security.Principal;

public interface PrincipalProvider {
    Principal getPrincipal();
    String getName();
    Long getUserId();
    User getUser();
}
