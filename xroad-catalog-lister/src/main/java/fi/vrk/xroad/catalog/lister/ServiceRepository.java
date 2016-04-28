package fi.vrk.xroad.catalog.lister;

import fi.vrk.xroad.xroad_catalog_lister.*;
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

public class ServiceRepository {
	private static final List<Member> members = new ArrayList<Member>();

	@PostConstruct
	public void initData() {
		Member member = new Member();
		member.setMemberClass("ABC");
		member.setMemberCode("122");
		member.setName("palvelu");
		member.setChanged(toXMLGregorianCalendar(LocalDate.now()));

		Subsystem ss = new Subsystem();
		ss.setSubsystemCode("sub1");

		Service s = new Service();
		s.setServiceVersion("V1");
		s.setServiceCode("getRandom");
		WSDL w = new WSDL();
		w.setExternalId("EXTID1");


		s.setWsdl(w);
		ServiceList sl = new ServiceList();
		sl.getService().add(s);
		ss.setServices(sl);

		SubsystemList ssl = new SubsystemList();
		ssl.getSubsystem().add(ss);
		member.setSubsystems(ssl);

		members.add(member);

		member = new Member();
		member.setMemberClass("WEE");
		member.setMemberCode("22233");
		member.setName("toinen palvelu");
		member.setChanged(toXMLGregorianCalendar(LocalDate.now()));

		ss = new Subsystem();
		ss.setSubsystemCode("sub1");

		s = new Service();
		s.setServiceVersion("V1");
		s.setServiceCode("getRandom");
		w = new WSDL();
		w.setExternalId("EXTID2");


		s.setWsdl(w);
		sl = new ServiceList();
		sl.getService().add(s);
		ss.setServices(sl);

		ssl = new SubsystemList();
		ssl.getSubsystem().add(ss);
		member.setSubsystems(ssl);


		members.add(member);

		member = new Member();
		member.setMemberClass("FOOBAR");
		member.setMemberCode("333");
		member.setName("mikä tämä on");
		member.setChanged(toXMLGregorianCalendar(LocalDate.now()));

		members.add(member);


	}

	private XMLGregorianCalendar toXMLGregorianCalendar(LocalDate date) {
		try {
			GregorianCalendar gcal = GregorianCalendar.from(date.atStartOfDay(ZoneId.systemDefault()));
			return DatatypeFactory.newInstance().newXMLGregorianCalendar(gcal);
		} catch (Exception e){
			throw new RuntimeException(e);
		}
	}

	public List<Member> listServices(LocalTime name) {
		Assert.notNull(name);

		return members;
	}		
}
