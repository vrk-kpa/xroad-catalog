/**
 * The MIT License
 * Copyright (c) 2016, Population Register Centre (VRK)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package fi.vrk.xroad.catalog.lister;

import fi.vrk.xroad.xroad_catalog_lister.Member;

import javax.xml.datatype.XMLGregorianCalendar;

/**
 * Service which talks in JAXB objects.
 * Responsible for correctly mapping the trees of JAXB objects / JPA entities.
 */
public interface JaxbCatalogService {

    /**
     * Returns all members that have had some part of member->substem->service->wsdl graph
     * changed after <code>changedAfter</code>. If changedAfter = null, returns all members
     * altogether.
     *
     * All substem->service->wsdl items are always returned, whether they are removed items
     * or not, and whether they have been updated since changedAfter or not.
     *
     * @return
     */
    Iterable<Member> getAllMembers(XMLGregorianCalendar changedAfter);
}
