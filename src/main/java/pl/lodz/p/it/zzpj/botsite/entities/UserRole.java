package pl.lodz.p.it.zzpj.botsite.entities;

public enum UserRole {
    ADMIN("ADMIN"), USER("USER");

    private final String roleName;

    UserRole(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }
}
