package pl.lodz.p.it.zzpj.botsite.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ObjectMapping {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
