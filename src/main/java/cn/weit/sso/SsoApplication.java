package cn.weit.sso;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;


/**
 * @author weitong
 */
@SpringBootApplication
public class SsoApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(SsoApplication.class).properties("spring.config.name=application").run(args);
    }
}
