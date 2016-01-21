package fi.vrk.xroad.catalog.lister;

import fi.vrk.xroad.xroad_catalog_lister.Service;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

@Component
public class ServiceRepository {
	private static final List<Service> services = new ArrayList<Service>();

	@PostConstruct
	public void initData() {
		Service s = new Service();
		s.setMemberClass("ABC");
		s.setMemberCode("122");
		s.setName("palvelu");
		s.setServiceVersion("v1");
		s.setUpdated(toXMLGregorianCalendar(LocalDate.now()));

		services.add(s);

		s = new Service();
		s.setMemberClass("WEE");
		s.setMemberCode("22233");
		s.setName("toinen palvelu");
		s.setServiceVersion("v1");
		s.setUpdated(toXMLGregorianCalendar(LocalDate.now()));

		services.add(s);

		s = new Service();
		s.setMemberClass("FOOBAR");
		s.setMemberCode("333");
		s.setName("mikä tämä on");
		s.setServiceVersion("v2");
		s.setUpdated(toXMLGregorianCalendar(LocalDate.now()));

		services.add(s);
	}

	private XMLGregorianCalendar toXMLGregorianCalendar(LocalDate date) {
		try {
			GregorianCalendar gcal = GregorianCalendar.from(date.atStartOfDay(ZoneId.systemDefault()));
			return DatatypeFactory.newInstance().newXMLGregorianCalendar(gcal);
		} catch (Exception e){
			throw new RuntimeException(e);
		}
	}

	public List<Service> listServices(LocalTime name) {
		Assert.notNull(name);

		return services;
	}
}
