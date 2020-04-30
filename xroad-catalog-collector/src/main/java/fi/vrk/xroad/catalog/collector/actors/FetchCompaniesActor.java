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
package fi.vrk.xroad.catalog.collector.actors;

import fi.vrk.xroad.catalog.collector.util.OrganizationUtil;
import fi.vrk.xroad.catalog.collector.wsimport.ClientType;
import fi.vrk.xroad.catalog.persistence.CatalogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


/**
 * Actor which fetches companies with respective data
 */
@Component
@Scope("prototype")
@Slf4j
public class FetchCompaniesActor extends XRoadCatalogActor {

    @Value("${xroad-catalog.fetch-companies-url}")
    private String fetchCompaniesUrl;

    @Autowired
    protected CatalogService catalogService;

    @Override
    public void preStart() throws Exception {

    }

    @Override
    protected boolean handleMessage(Object message) {
        if (message instanceof ClientType) {

            log.info("Fetching companies from {}", fetchCompaniesUrl);

            ((ClientType) message).getId().setMemberCode("1710128-9"); // remove this line

            String businessCode = ((ClientType) message).getId().getMemberCode();
            String companyInfo = OrganizationUtil.getCompany(fetchCompaniesUrl, businessCode);
            return true;
        } else {
            return false;
        }
    }
}
