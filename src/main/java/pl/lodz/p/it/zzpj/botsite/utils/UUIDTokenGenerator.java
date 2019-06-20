package pl.lodz.p.it.zzpj.botsite.utils;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UUIDTokenGenerator implements TokenGenerator {
    @Override
    public String generate() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
