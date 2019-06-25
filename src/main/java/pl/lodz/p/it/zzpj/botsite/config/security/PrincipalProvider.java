package pl.lodz.p.it.zzpj.botsite.config.security;

import java.security.Principal;

public interface PrincipalProvider {
    Principal getPrincipal();
    String getName();
    Long getUserId();
}
