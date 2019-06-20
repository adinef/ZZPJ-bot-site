package pl.lodz.p.it.zzpj.botsite.web.dto;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import pl.lodz.p.it.zzpj.botsite.entities.User;

import java.util.Collection;

public class MyUserDetails implements UserDetails {
    @Getter
    private final User user;

    private MyUserDetails(User user) {
        this.user = user;
    }

    public static UserDetails of(User user) {
        return new MyUserDetails(user);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return AuthorityUtils.createAuthorityList(
                user
                        .getRoles()
                        .stream()
                        .map((elem) -> "ROLE_" + elem.getRoleName())
                        .toArray(String[]::new)
        );
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getLogin();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.isActive();
    }

    public Long getId() { return user.getId(); }
}
