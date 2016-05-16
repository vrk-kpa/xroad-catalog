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

import fi.vrk.xroad.catalog.persistence.CatalogService;
import fi.vrk.xroad.xroad_catalog_lister.Member;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.xml.datatype.XMLGregorianCalendar;

/**
 * XML interface for lister
 */
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
    public Iterable<Member> getAllMembers(XMLGregorianCalendar changedAfter)  {
        log.info("getAllMembers changedAfter:{}", changedAfter);
        Iterable<fi.vrk.xroad.catalog.persistence.entity.Member> entities;
        if (changedAfter != null) {
            entities = catalogService.getAllMembers(jaxbConverter.toLocalDateTime(changedAfter));
        } else {
            entities = catalogService.getAllMembers();
        }

        return jaxbConverter.convertMembers(entities, false);
    }
}
