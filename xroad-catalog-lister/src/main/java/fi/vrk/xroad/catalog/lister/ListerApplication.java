package fi.vrk.xroad.catalog.lister;

import fi.vrk.xroad.catalog.persistence.CatalogService;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackageClasses = {ListerApplication.class, CatalogService.class})
public class ListerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ListerApplication.class, args);
    }
}


