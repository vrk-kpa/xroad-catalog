/**
 * The MIT License
 *
 * Copyright (c) 2023- Nordic Institute for Interoperability Solutions (NIIS)
 * Copyright (c) 2016-2023 Finnish Digital Agency
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package fi.vrk.xroad.catalog.lister;

import fi.vrk.xroad.catalog.lister.util.JaxbServiceUtil;
import fi.vrk.xroad.catalog.persistence.entity.Service;
import fi.vrk.xroad.catalog.persistence.entity.Subsystem;
import fi.vrk.xroad.xroad_catalog_lister.*;
import org.springframework.stereotype.Component;
import javax.xml.datatype.XMLGregorianCalendar;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class JaxbServiceConverter implements JaxbServiceConversion {

    @Override
    public Collection<Member> convertMembers(Iterable<fi.vrk.xroad.catalog.persistence.entity.Member> members,
                                             boolean onlyActiveChildren)  {
        List<Member> converted = new ArrayList<>();
        for (fi.vrk.xroad.catalog.persistence.entity.Member member: members) {
            Member cm = new Member();
            cm.setChanged(JaxbServiceUtil.toXmlGregorianCalendar(member.getStatusInfo().getChanged()));
            cm.setCreated(JaxbServiceUtil.toXmlGregorianCalendar(member.getStatusInfo().getCreated()));
            cm.setFetched(JaxbServiceUtil.toXmlGregorianCalendar(member.getStatusInfo().getFetched()));
            cm.setRemoved(JaxbServiceUtil.toXmlGregorianCalendar(member.getStatusInfo().getRemoved()));
            cm.setMemberCode(member.getMemberCode());
            cm.setMemberClass(member.getMemberClass());
            cm.setName(member.getName());
            cm.setXRoadInstance(member.getXRoadInstance());
            cm.setSubsystems(new SubsystemList());
            Iterable<Subsystem> subsystems;
            if (onlyActiveChildren) {
                subsystems = member.getActiveSubsystems();
            } else {
                subsystems = member.getAllSubsystems();
            }
            cm.getSubsystems().getSubsystem().addAll(convertSubsystems(subsystems, onlyActiveChildren));
            converted.add(cm);
        }
        return converted;
    }

    @Override
    public Collection<fi.vrk.xroad.xroad_catalog_lister.Subsystem> convertSubsystems(Iterable<Subsystem> subsystems,
                                                                                     boolean onlyActiveChildren) {
        List<fi.vrk.xroad.xroad_catalog_lister.Subsystem> converted = new ArrayList<>();
        for (Subsystem subsystem: subsystems) {
            fi.vrk.xroad.xroad_catalog_lister.Subsystem cs = new fi.vrk.xroad.xroad_catalog_lister.Subsystem();
            cs.setChanged(JaxbServiceUtil.toXmlGregorianCalendar(subsystem.getStatusInfo().getChanged()));
            cs.setCreated(JaxbServiceUtil.toXmlGregorianCalendar(subsystem.getStatusInfo().getCreated()));
            cs.setFetched(JaxbServiceUtil.toXmlGregorianCalendar(subsystem.getStatusInfo().getFetched()));
            cs.setRemoved(JaxbServiceUtil.toXmlGregorianCalendar(subsystem.getStatusInfo().getRemoved()));
            cs.setSubsystemCode(subsystem.getSubsystemCode());
            cs.setServices(new ServiceList());
            Iterable<Service> services;
            if (onlyActiveChildren) {
                services = subsystem.getActiveServices();
            } else {
                services = subsystem.getAllServices();
            }
            cs.getServices().getService().addAll(convertServices(services, onlyActiveChildren));
            converted.add(cs);
        }
        return converted;
    }

    @Override
    public Collection<fi.vrk.xroad.xroad_catalog_lister.Service> convertServices(Iterable<Service> services,
                                                                                 boolean onlyActiveChildren) {
        List<fi.vrk.xroad.xroad_catalog_lister.Service> converted = new ArrayList<>();
        for (Service service: services) {
            converted.add(JaxbServiceUtil.convertService(service, onlyActiveChildren));
        }
        return converted;
    }

    @Override
    public LocalDateTime toLocalDateTime(XMLGregorianCalendar calendar) {
        return calendar.toGregorianCalendar().toZonedDateTime().toLocalDateTime();
    }

    @Override
    public Collection<ErrorLog> convertErrorLog(Iterable<fi.vrk.xroad.catalog.persistence.entity.ErrorLog> errorLogEntries)  {
        List<ErrorLog> converted = new ArrayList<>();
        for (fi.vrk.xroad.catalog.persistence.entity.ErrorLog errorLog: errorLogEntries) {
            ErrorLog er = new ErrorLog();
            er.setCreated(JaxbServiceUtil.toXmlGregorianCalendar(errorLog.getCreated()));
            er.setMessage(errorLog.getMessage());
            er.setCode(errorLog.getCode());
            er.setXRoadInstance(errorLog.getXRoadInstance());
            er.setMemberClass(errorLog.getMemberClass());
            er.setMemberCode(errorLog.getMemberCode());
            er.setSubsystemCode(errorLog.getSubsystemCode());
            er.setGroupCode(errorLog.getGroupCode());
            er.setServiceCode(errorLog.getServiceCode());
            er.setServiceVersion(errorLog.getServiceVersion());
            er.setSecurityCategoryCode(errorLog.getSecurityCategoryCode());
            er.setServerCode(errorLog.getServerCode());
            converted.add(er);
        }
        return converted;
    }
}
