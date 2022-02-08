/**
 * The MIT License
 * Copyright (c) 2021, Population Register Centre (VRK)
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
package fi.vrk.xroad.catalog.persistence.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
public class CompanyData implements Serializable {

    private static final long serialVersionUID = 5049961369268846285L;

    private String businessCode;

    private LocalDateTime created;

    private LocalDateTime changed;

    private LocalDateTime fetched;

    private LocalDateTime removed;

    private String companyForm;

    private String detailsUri;

    private String name;

    private LocalDateTime registrationDate;

    private List<BusinessAddressData> businessAddresses;

    private List<BusinessAuxiliaryNameData> businessAuxiliaryNames;

    private List<BusinessIdChangeData> businessIdChanges;

    private List<BusinessLineData> businessLines;

    private List<BusinessNameData> businessNames;

    private List<CompanyFormData> companyForms;

    private List<ContactDetailData> contactDetails;

    private List<LanguageData> languages;

    private List<LiquidationData> liquidations;

    private List<RegisteredEntryData> registeredEntries;

    private List<RegisteredOfficeData> registeredOffices;
}
