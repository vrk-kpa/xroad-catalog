/**
 * The MIT License
 *
 * Copyright (c) 2023- Nordic Institute for Interoperability Solutions (NIIS) Copyright (c) 2016-2022 Finnish Digital Agency
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package fi.vrk.xroad.catalog.lister;

import fi.vrk.xroad.catalog.persistence.entity.Service;
import fi.vrk.xroad.catalog.persistence.entity.Subsystem;
import fi.vrk.xroad.xroad_catalog_lister.ErrorLog;
import fi.vrk.xroad.xroad_catalog_lister.Member;
import org.springframework.context.annotation.Profile;

import javax.xml.datatype.XMLGregorianCalendar;
import java.time.LocalDateTime;
import java.util.Collection;

@Profile({"default", "fi"})
public interface JaxbServiceConversion {

    /**
     * Convert entities to XML objects
     * @param members Iterable of Member entities
     * @param onlyActiveChildren if true, convert only active subsystems
     * @return Collection of Members (JAXB generated)
     */
    Collection<Member> convertMembers(Iterable<fi.vrk.xroad.catalog.persistence.entity.Member> members, boolean onlyActiveChildren);

    /**
     * Convert entities to XML objects
     * @param subsystems Iterable of Subsystem entities
     * @param onlyActiveChildren if true, convert only active subsystems
     * @return collection of XML objects
     */
    Collection<fi.vrk.xroad.xroad_catalog_lister.Subsystem> convertSubsystems(Iterable<Subsystem> subsystems, boolean onlyActiveChildren);

    /**
     * Convert entities to XML objects
     * @param services Iterable of Service entities
     * @param onlyActiveChildren if true, convert only active subsystems
     * @return collection of XML objects
     */
    Collection<fi.vrk.xroad.xroad_catalog_lister.Service> convertServices(Iterable<Service> services, boolean onlyActiveChildren);

    LocalDateTime toLocalDateTime(XMLGregorianCalendar calendar);

    /**
     * Convert entities to XML objects
     * @param errorLogEntries Iterable of ErrorLog entities
     * @return Collection of ErrorLog entries (JAXB generated)
     */
    Collection<ErrorLog> convertErrorLog(Iterable<fi.vrk.xroad.catalog.persistence.entity.ErrorLog> errorLogEntries);

}
