package fi.vrk.xroad.catalog.lister;

import fi.vrk.xroad.catalog.persistence.CatalogService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

//@SpringBootApplication(scanBasePackageClasses = {ListerApplication.class, CatalogService.class})
//@SpringBootApplication
//@SpringBootApplication(scanBasePackages = {"fi.vrk.xroad.catalog.persistence","fi.vrk.xroad.catalog.lister"})
@ComponentScan(basePackageClasses = {ListerApplication.class, CatalogService.class})
//@EnableAutoConfiguration
public class ListerApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(ListerApplication.class, args);
    }
}


