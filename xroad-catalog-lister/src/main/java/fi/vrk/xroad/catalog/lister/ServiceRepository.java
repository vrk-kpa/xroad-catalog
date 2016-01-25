package fi.vrk.xroad.catalog.lister;

import fi.vrk.xroad.xroad_catalog_lister.Member;
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
	private static final List<Member> members = new ArrayList<Member>();

	@PostConstruct
	public void initData() {
		Member member = new Member();
		member.setMemberClass("ABC");
		member.setMemberCode("122");
		member.setName("palvelu");
		member.setUpdated(toXMLGregorianCalendar(LocalDate.now()));

		members.add(member);

		member = new Member();
		member.setMemberClass("WEE");
		member.setMemberCode("22233");
		member.setName("toinen palvelu");
		member.setUpdated(toXMLGregorianCalendar(LocalDate.now()));

		members.add(member);

		member = new Member();
		member.setMemberClass("FOOBAR");
		member.setMemberCode("333");
		member.setName("mikä tämä on");
		member.setUpdated(toXMLGregorianCalendar(LocalDate.now()));

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
