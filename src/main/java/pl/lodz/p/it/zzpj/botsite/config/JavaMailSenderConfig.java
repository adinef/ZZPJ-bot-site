package pl.lodz.p.it.zzpj.botsite.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

@Component
public class JavaMailSenderConfig {

    @Value("${spring.mail.smtp.port}")
    private String host;

    @Value("${spring.mail.smtp.port}")
    private int port;

    @Bean
    public JavaMailSender getJavaMailSender() {

        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();

        javaMailSender.setProtocol(JavaMailSenderImpl.DEFAULT_PROTOCOL);
        javaMailSender.setHost(host);
        javaMailSender.setPort(port);

        return javaMailSender;
    }

}
