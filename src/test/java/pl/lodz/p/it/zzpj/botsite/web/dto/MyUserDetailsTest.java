package pl.lodz.p.it.zzpj.botsite.web.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import pl.lodz.p.it.zzpj.botsite.entities.User;
import pl.lodz.p.it.zzpj.botsite.entities.UserRole;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;

class MyUserDetailsTest {

    @Test
    void getAuthoritiesReturnsRightValuesForUser() {
        User user = User
                .builder()
                .roles(Collections.singletonList(UserRole.ADMIN))
                .build();
        UserDetails details = MyUserDetails.of(user);
        Collection<? extends GrantedAuthority> authoritiesRetrieved = details.getAuthorities();
        List<GrantedAuthority> grantedAuthorities =
                AuthorityUtils.createAuthorityList("ROLE_" + UserRole.ADMIN.getRoleName());
        Assertions.assertIterableEquals(authoritiesRetrieved, grantedAuthorities);
        assertThat(Arrays.asList(authoritiesRetrieved.toArray(new GrantedAuthority[0])), not(hasItem(UserRole.USER)));
    }

    @Test
    void getAuthoritiesReturnsEmpyListOnNoAuthorities() {
        User user = new User();
        UserDetails details = MyUserDetails.of(user);
        Collection<? extends GrantedAuthority> authoritiesRetrieved = details.getAuthorities();
        Assertions.assertEquals(authoritiesRetrieved.size(), 0);
    }
}