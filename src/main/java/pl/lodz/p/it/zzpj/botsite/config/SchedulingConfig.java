package pl.lodz.p.it.zzpj.botsite.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@ComponentScan("pl.lodz.p.it.zzpj.botsite.tasking")
public class SchedulingConfig {
}
