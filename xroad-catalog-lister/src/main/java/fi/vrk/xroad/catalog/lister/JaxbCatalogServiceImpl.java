package fi.vrk.xroad.catalog.lister;

import fi.vrk.xroad.catalog.persistence.CatalogService;
import fi.vrk.xroad.xroad_catalog_lister.Member;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.Collection;

@Component
@Slf4j
public class JaxbCatalogServiceImpl implements JaxbCatalogService {

    @Autowired
    @Setter
    private CatalogService catalogService;

    @Autowired
    @Setter
    private JaxbConverter jaxbConverter;

    @Override
    public Iterable<Member> getAllMembers(XMLGregorianCalendar changedAfter) throws DatatypeConfigurationException {
        log.info("getAllMembers changedAfter:{}", changedAfter);
        Iterable<fi.vrk.xroad.catalog.persistence.entity.Member> entities = catalogService.getAllMembers(jaxbConverter.toLocalDateTime(changedAfter));
        Collection<Member> jaxbMembers = jaxbConverter.convertMembers(entities);
        return jaxbMembers;
    }
}
