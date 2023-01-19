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

import fi.vrk.xroad.xroad_catalog_lister.ChangedValue;
import fi.vrk.xroad.xroad_catalog_lister.Organization;
import javax.xml.datatype.XMLGregorianCalendar;

public interface JaxbOrganizationService {

    /**
     * Returns all organizations
     *
     * All subitems of organization are always returned
     *
     * @param businessCode businessCode of an organization
     * @return Iterable of JAXB generated Organizations
     */
    Iterable<Organization> getOrganizations(String businessCode);

    /**
     * Returns whether some values of Organization have changed
     *
     * @param  guid guid of a company
     * @param startDateTime creation datetime from
     * @param endDateTime creation datetime to
     * @return Iterable of JAXB generated ChangedValues
     */
    Iterable<ChangedValue> getChangedOrganizationValues(String guid,
                                                        XMLGregorianCalendar startDateTime,
                                                        XMLGregorianCalendar endDateTime);
}
