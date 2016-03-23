package fi.vrk.xroad.catalog.lister;

import fi.vrk.xroad.xroad_catalog_lister.*;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
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
		w.setData("<wsdl:definitions name=\"testService\" targetNamespace=\"http://vrk-test.x-road.fi/producer\" " +
				"xmlns:soap=\"http://schemas.xmlsoap.org/wsdl/soap/\" xmlns:tns=\"http://vrk-test.x-road.fi/producer\"" +
				" xmlns:wsdl=\"http://schemas.xmlsoap.org/wsdl/\" xmlns:xrd=\"http://x-road.eu/xsd/xroad.xsd\" " +
				"xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:id=\"http://x-road.eu/xsd/identifiers\">\n" +
				"   <wsdl:types>\n" +
				"      <!--Schema for identifiers (reduced)-->\n" +
				"      <xsd:schema elementFormDefault=\"qualified\" targetNamespace=\"http://x-road" +
				".eu/xsd/identifiers\" xmlns=\"http://x-road.eu/xsd/identifiers\">\n" +
				"         <xsd:simpleType name=\"XRoadObjectType\">\n" +
				"            <xsd:annotation>\n" +
				"               <xsd:documentation>Enumeration for X-Road identifier\n" +
				"                        types that can be used in requests.</xsd:documentation>\n" +
				"            </xsd:annotation>\n" +
				"            <xsd:restriction base=\"xsd:string\">\n" +
				"               <xsd:enumeration value=\"MEMBER\"/>\n" +
				"               <xsd:enumeration value=\"SUBSYSTEM\"/>\n" +
				"               <xsd:enumeration value=\"SERVICE\"/>\n" +
				"            </xsd:restriction>\n" +
				"         </xsd:simpleType>\n" +
				"         <xsd:element name=\"xRoadInstance\" type=\"xsd:string\">\n" +
				"            <xsd:annotation>\n" +
				"               <xsd:documentation>Identifies the X-Road instance.\n" +
				"                        This field is applicable to all identifier\n" +
				"                        types.</xsd:documentation>\n" +
				"            </xsd:annotation>\n" +
				"         </xsd:element>\n" +
				"         <xsd:element name=\"memberClass\" type=\"xsd:string\">\n" +
				"            <xsd:annotation>\n" +
				"               <xsd:documentation>Type of the member (company,\n" +
				"                        government institution, private person, etc.)</xsd:documentation>\n" +
				"            </xsd:annotation>\n" +
				"         </xsd:element>\n" +
				"         <xsd:element name=\"memberCode\" type=\"xsd:string\">\n" +
				"            <xsd:annotation>\n" +
				"               <xsd:documentation>Code that uniquely identifies a\n" +
				"                        member of given member type.</xsd:documentation>\n" +
				"            </xsd:annotation>\n" +
				"         </xsd:element>\n" +
				"         <xsd:element name=\"subsystemCode\" type=\"xsd:string\">\n" +
				"            <xsd:annotation>\n" +
				"               <xsd:documentation>Code that uniquely identifies a\n" +
				"                        subsystem of given SDSB member.</xsd:documentation>\n" +
				"            </xsd:annotation>\n" +
				"         </xsd:element>\n" +
				"         <xsd:element name=\"serviceCode\" type=\"xsd:string\">\n" +
				"            <xsd:annotation>\n" +
				"               <xsd:documentation>Code that uniquely identifies a\n" +
				"                        service offered by given SDSB member or\n" +
				"                        subsystem.</xsd:documentation>\n" +
				"            </xsd:annotation>\n" +
				"         </xsd:element>\n" +
				"         <xsd:element name=\"serviceVersion\" type=\"xsd:string\">\n" +
				"            <xsd:annotation>\n" +
				"               <xsd:documentation>Version of the service.</xsd:documentation>\n" +
				"            </xsd:annotation>\n" +
				"         </xsd:element>\n" +
				"         <xsd:attribute name=\"objectType\" type=\"XRoadObjectType\"/>\n" +
				"         <xsd:complexType name=\"XRoadClientIdentifierType\">\n" +
				"            <xsd:sequence>\n" +
				"               <xsd:element ref=\"xRoadInstance\"/>\n" +
				"               <xsd:element ref=\"memberClass\"/>\n" +
				"               <xsd:element ref=\"memberCode\"/>\n" +
				"               <xsd:element minOccurs=\"0\" ref=\"subsystemCode\"/>\n" +
				"            </xsd:sequence>\n" +
				"            <xsd:attribute ref=\"objectType\" use=\"required\"/>\n" +
				"         </xsd:complexType>\n" +
				"         <xsd:complexType name=\"XRoadServiceIdentifierType\">\n" +
				"            <xsd:sequence>\n" +
				"               <xsd:element ref=\"xRoadInstance\"/>\n" +
				"               <xsd:element ref=\"memberClass\"/>\n" +
				"               <xsd:element ref=\"memberCode\"/>\n" +
				"               <xsd:element minOccurs=\"0\" ref=\"subsystemCode\"/>\n" +
				"               <xsd:element ref=\"serviceCode\"/>\n" +
				"               <xsd:element minOccurs=\"0\" ref=\"serviceVersion\"/>\n" +
				"            </xsd:sequence>\n" +
				"            <xsd:attribute ref=\"objectType\" use=\"required\"/>\n" +
				"         </xsd:complexType>\n" +
				"      </xsd:schema>\n" +
				"      <!--Schema for request headers-->\n" +
				"      <xsd:schema targetNamespace=\"http://x-road.eu/xsd/xroad.xsd\" elementFormDefault=\"qualified\"" +
				" xmlns=\"http://www.w3.org/2001/XMLSchema\">\n" +
				"         <xsd:element name=\"client\" type=\"id:XRoadClientIdentifierType\"/>\n" +
				"         <xsd:element name=\"service\" type=\"id:XRoadServiceIdentifierType\"/>\n" +
				"         <xsd:element name=\"userId\" type=\"xsd:string\"/>\n" +
				"         <xsd:element name=\"id\" type=\"xsd:string\"/>\n" +
				"         <xsd:element name=\"protocolVersion\" type=\"xsd:string\"/>\n" +
				"      </xsd:schema>\n" +
				"      <!--Schema for requests (reduced)-->\n" +
				"      <xsd:schema targetNamespace=\"http://vrk-test.x-road.fi/producer\">\n" +
				"         <xsd:element name=\"getRandom\" nillable=\"true\"/>\n" +
				"         <xsd:element name=\"getRandomResponse\">\n" +
				"            <xsd:complexType>\n" +
				"               <xsd:sequence>\n" +
				"                  <xsd:element name=\"response\">\n" +
				"                     <xsd:complexType>\n" +
				"                        <xsd:sequence>\n" +
				"                           <xsd:element name=\"data\" type=\"xsd:string\">\n" +
				"                              <xsd:annotation>\n" +
				"                                 <xsd:documentation>Service response</xsd:documentation>\n" +
				"                              </xsd:annotation>\n" +
				"                           </xsd:element>\n" +
				"                        </xsd:sequence>\n" +
				"                     </xsd:complexType>\n" +
				"                  </xsd:element>\n" +
				"               </xsd:sequence>\n" +
				"            </xsd:complexType>\n" +
				"         </xsd:element>\n" +
				"         <xsd:element name=\"helloService\">\n" +
				"            <xsd:complexType>\n" +
				"               <xsd:sequence>\n" +
				"                  <xsd:element name=\"request\">\n" +
				"                     <xsd:complexType>\n" +
				"                        <xsd:sequence>\n" +
				"                           <xsd:element name=\"name\" type=\"xsd:string\">\n" +
				"                              <xsd:annotation>\n" +
				"                                 <xsd:documentation>Name</xsd:documentation>\n" +
				"                              </xsd:annotation>\n" +
				"                           </xsd:element>\n" +
				"                        </xsd:sequence>\n" +
				"                     </xsd:complexType>\n" +
				"                  </xsd:element>\n" +
				"               </xsd:sequence>\n" +
				"            </xsd:complexType>\n" +
				"         </xsd:element>\n" +
				"         <xsd:element name=\"helloServiceResponse\">\n" +
				"            <xsd:complexType>\n" +
				"               <xsd:sequence>\n" +
				"                  <xsd:element name=\"request\">\n" +
				"                     <xsd:complexType>\n" +
				"                        <xsd:sequence>\n" +
				"                           <xsd:element name=\"name\" nillable=\"true\" type=\"xsd:string\"/>\n" +
				"                        </xsd:sequence>\n" +
				"                     </xsd:complexType>\n" +
				"                  </xsd:element>\n" +
				"                  <xsd:element name=\"response\">\n" +
				"                     <xsd:complexType>\n" +
				"                        <xsd:sequence>\n" +
				"                           <xsd:element name=\"message\" type=\"xsd:string\">\n" +
				"                              <xsd:annotation>\n" +
				"                                 <xsd:documentation>Service response</xsd:documentation>\n" +
				"                              </xsd:annotation>\n" +
				"                           </xsd:element>\n" +
				"                        </xsd:sequence>\n" +
				"                     </xsd:complexType>\n" +
				"                  </xsd:element>\n" +
				"               </xsd:sequence>\n" +
				"            </xsd:complexType>\n" +
				"         </xsd:element>\n" +
				"      </xsd:schema>\n" +
				"   </wsdl:types>\n" +
				"   <wsdl:message name=\"requestheader\">\n" +
				"      <wsdl:part name=\"client\" element=\"xrd:client\"/>\n" +
				"      <wsdl:part name=\"service\" element=\"xrd:service\"/>\n" +
				"      <wsdl:part name=\"userId\" element=\"xrd:userId\"/>\n" +
				"      <wsdl:part name=\"id\" element=\"xrd:id\"/>\n" +
				"      <wsdl:part name=\"protocolVersion\" element=\"xrd:protocolVersion\"/>\n" +
				"   </wsdl:message>\n" +
				"   <wsdl:message name=\"getRandom\">\n" +
				"      <wsdl:part name=\"body\" element=\"tns:getRandom\"/>\n" +
				"   </wsdl:message>\n" +
				"   <wsdl:message name=\"getRandomResponse\">\n" +
				"      <wsdl:part name=\"body\" element=\"tns:getRandomResponse\"/>\n" +
				"   </wsdl:message>\n" +
				"   <wsdl:message name=\"helloService\">\n" +
				"      <wsdl:part name=\"body\" element=\"tns:helloService\"/>\n" +
				"   </wsdl:message>\n" +
				"   <wsdl:message name=\"helloServiceResponse\">\n" +
				"      <wsdl:part name=\"body\" element=\"tns:helloServiceResponse\"/>\n" +
				"   </wsdl:message>\n" +
				"   <wsdl:portType name=\"testServicePortType\">\n" +
				"      <wsdl:operation name=\"getRandom\">\n" +
				"         <wsdl:input message=\"tns:getRandom\"/>\n" +
				"         <wsdl:output message=\"tns:getRandomResponse\"/>\n" +
				"      </wsdl:operation>\n" +
				"      <wsdl:operation name=\"helloService\">\n" +
				"         <wsdl:input message=\"tns:helloService\"/>\n" +
				"         <wsdl:output message=\"tns:helloServiceResponse\"/>\n" +
				"      </wsdl:operation>\n" +
				"   </wsdl:portType>\n" +
				"   <wsdl:binding name=\"testServiceBinding\" type=\"tns:testServicePortType\">\n" +
				"      <soap:binding style=\"document\" transport=\"http://schemas.xmlsoap.org/soap/http\"/>\n" +
				"      <wsdl:operation name=\"getRandom\">\n" +
				"         <soap:operation soapAction=\"\" style=\"document\"/>\n" +
				"         <id:version>v1</id:version>\n" +
				"         <wsdl:input>\n" +
				"            <soap:body parts=\"body\" use=\"literal\"/>\n" +
				"            <soap:header message=\"tns:requestheader\" part=\"client\" use=\"literal\"/>\n" +
				"            <soap:header message=\"tns:requestheader\" part=\"service\" use=\"literal\"/>\n" +
				"            <soap:header message=\"tns:requestheader\" part=\"userId\" use=\"literal\"/>\n" +
				"            <soap:header message=\"tns:requestheader\" part=\"id\" use=\"literal\"/>\n" +
				"            <soap:header message=\"tns:requestheader\" part=\"protocolVersion\" use=\"literal\"/>\n" +
				"         </wsdl:input>\n" +
				"         <wsdl:output>\n" +
				"            <soap:body parts=\"body\" use=\"literal\"/>\n" +
				"            <soap:header message=\"tns:requestheader\" part=\"client\" use=\"literal\"/>\n" +
				"            <soap:header message=\"tns:requestheader\" part=\"service\" use=\"literal\"/>\n" +
				"            <soap:header message=\"tns:requestheader\" part=\"userId\" use=\"literal\"/>\n" +
				"            <soap:header message=\"tns:requestheader\" part=\"id\" use=\"literal\"/>\n" +
				"            <soap:header message=\"tns:requestheader\" part=\"protocolVersion\" use=\"literal\"/>\n" +
				"         </wsdl:output>\n" +
				"      </wsdl:operation>\n" +
				"      <wsdl:operation name=\"helloService\">\n" +
				"         <soap:operation soapAction=\"\" style=\"document\"/>\n" +
				"         <id:version>v1</id:version>\n" +
				"         <wsdl:input>\n" +
				"            <soap:body parts=\"body\" use=\"literal\"/>\n" +
				"            <soap:header message=\"tns:requestheader\" part=\"client\" use=\"literal\"/>\n" +
				"            <soap:header message=\"tns:requestheader\" part=\"service\" use=\"literal\"/>\n" +
				"            <soap:header message=\"tns:requestheader\" part=\"userId\" use=\"literal\"/>\n" +
				"            <soap:header message=\"tns:requestheader\" part=\"id\" use=\"literal\"/>\n" +
				"            <soap:header message=\"tns:requestheader\" part=\"protocolVersion\" use=\"literal\"/>\n" +
				"         </wsdl:input>\n" +
				"         <wsdl:output>\n" +
				"            <soap:body parts=\"body\" use=\"literal\"/>\n" +
				"            <soap:header message=\"tns:requestheader\" part=\"client\" use=\"literal\"/>\n" +
				"            <soap:header message=\"tns:requestheader\" part=\"service\" use=\"literal\"/>\n" +
				"            <soap:header message=\"tns:requestheader\" part=\"userId\" use=\"literal\"/>\n" +
				"            <soap:header message=\"tns:requestheader\" part=\"id\" use=\"literal\"/>\n" +
				"            <soap:header message=\"tns:requestheader\" part=\"protocolVersion\" use=\"literal\"/>\n" +
				"         </wsdl:output>\n" +
				"      </wsdl:operation>\n" +
				"   </wsdl:binding>\n" +
				"   <wsdl:service name=\"testService\">\n" +
				"      <wsdl:port binding=\"tns:testServiceBinding\" name=\"testServicePort\">\n" +
				"         <soap:address location=\"http://localhost:8080/Adapter/Endpoint\"/>\n" +
				"      </wsdl:port>\n" +
				"   </wsdl:service>\n" +
				"</wsdl:definitions>");

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
