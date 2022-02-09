/**
 * The MIT License
 * Copyright (c) 2022, Population Register Centre (VRK)
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
package fi.vrk.xroad.catalog.persistence.entity;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {
        "organizationNames",
        "organizationDescriptions",
        "emails",
        "phoneNumbers",
        "webPages",
        "addresses"})
@EqualsAndHashCode(exclude = {
        "id",
        "statusInfo",
        "organizationNames",
        "organizationDescriptions",
        "emails",
        "phoneNumbers",
        "webPages",
        "addresses"})
@NamedQueries({@NamedQuery(name = "Organization.findAllByBusinessCode", query = Organization.FIND_ALL_BY_BUSINESS_CODE)})
public class Organization {

    static final String FIND_ALL_BY_BUSINESS_CODE =
            "SELECT DISTINCT org FROM Organization org WHERE org.businessCode = :businessCode";

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ORGANIZATION_GEN")
    @SequenceGenerator(name = "ORGANIZATION_GEN", sequenceName = "ORGANIZATION_ID_SEQ", allocationSize = 1)
    private long id;
    @Column(nullable = false)
    private String organizationType;
    @Column(nullable = false)
    private String publishingStatus;
    @Column(nullable = false)
    private String businessCode;
    @Column(nullable = false)
    private String guid;
    @Builder.Default
    @Embedded
    private StatusInfo statusInfo = new StatusInfo();
    @Builder.Default
    @Getter(AccessLevel.NONE) // do not create default getter, we provide the substitute
    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<OrganizationName> organizationNames = new HashSet<>();
    @Builder.Default
    @Getter(AccessLevel.NONE) // do not create default getter, we provide the substitute
    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<OrganizationDescription> organizationDescriptions = new HashSet<>();
    @Builder.Default
    @Getter(AccessLevel.NONE) // do not create default getter, we provide the substitute
    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Email> emails = new HashSet<>();
    @Builder.Default
    @Getter(AccessLevel.NONE) // do not create default getter, we provide the substitute
    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<PhoneNumber> phoneNumbers = new HashSet<>();
    @Builder.Default
    @Getter(AccessLevel.NONE) // do not create default getter, we provide the substitute
    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<WebPage> webPages = new HashSet<>();
    @Builder.Default
    @Getter(AccessLevel.NONE) // do not create default getter, we provide the substitute
    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Address> addresses = new HashSet<>();

    /**
     * This collection can be used to add new items
     *
     * @return Set of all organizationNames
     */
    public Set<OrganizationName> getAllOrganizationNames() {
        return organizationNames;
    }

    /**
     * This collection can be used to add new items
     *
     * @return Set of all organizationDescriptions
     */
    public Set<OrganizationDescription> getAllOrganizationDescriptions() {
        return organizationDescriptions;
    }

    /**
     * This collection can be used to add new items
     *
     * @return Set of all emails
     */
    public Set<Email> getAllEmails() {
        return emails;
    }

    /**
     * This collection can be used to add new items
     *
     * @return Set of all phoneNumbers
     */
    public Set<PhoneNumber> getAllPhoneNumbers() {
        return phoneNumbers;
    }

    /**
     * This collection can be used to add new items
     *
     * @return Set of all webPages
     */
    public Set<WebPage> getAllWebPages() {
        return webPages;
    }

    /**
     * This collection can be used to add new items
     *
     * @return Set of all addresses
     */
    public Set<Address> getAllAddresses() {
        return addresses;
    }

}
