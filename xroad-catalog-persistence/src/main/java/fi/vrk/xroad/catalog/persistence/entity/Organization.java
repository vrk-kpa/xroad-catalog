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
@NamedEntityGraph(
        name = "organization.full-tree.graph",
        attributeNodes = {
                @NamedAttributeNode(value = "organizationNames"),
                @NamedAttributeNode(value = "organizationDescriptions"),
                @NamedAttributeNode(value = "emails"),
                @NamedAttributeNode(value = "phoneNumbers"),
                @NamedAttributeNode(value = "webPages"),
                @NamedAttributeNode(value = "addresses", subgraph = "addresses.streetAddresses.graph"),
                @NamedAttributeNode(value = "addresses", subgraph = "addresses.postOfficeBoxAddresses.graph"),
        },
        subgraphs = {
                @NamedSubgraph(
                        name = "addresses.streetAddresses.graph",
                        attributeNodes = {
                                @NamedAttributeNode(value = "streetAddresses",
                                        subgraph = "streetAddresses.streets.graph"),
                                @NamedAttributeNode(value = "streetAddresses",
                                        subgraph = "streetAddresses.postOffices.graph"),
                                @NamedAttributeNode(value = "streetAddresses",
                                        subgraph = "streetAddresses.municipalities.graph"),
                                @NamedAttributeNode(value = "streetAddresses",
                                        subgraph = "streetAddresses.additionalInformation.graph")
                        }),
                @NamedSubgraph(
                        name = "addresses.postOfficeBoxAddresses.graph",
                        attributeNodes = {
                                @NamedAttributeNode(value = "postOfficeBoxAddresses",
                                        subgraph = "postOfficeBoxAddresses.postOffices.graph"),
                                @NamedAttributeNode(value = "postOfficeBoxAddresses",
                                        subgraph = "postOfficeBoxAddresses.postOfficesBoxes.graph"),
                                @NamedAttributeNode(value = "postOfficeBoxAddresses",
                                        subgraph = "postOfficeBoxAddresses.additionalInformation.graph")
                        }),
                @NamedSubgraph(
                        name = "streetAddresses.streets.graph",
                        attributeNodes = @NamedAttributeNode(value = "streets")),
                @NamedSubgraph(
                        name = "streetAddresses.postOffices.graph",
                        attributeNodes = @NamedAttributeNode(value = "postOffices")),
                @NamedSubgraph(
                        name = "streetAddresses.municipalities.graph",
                        attributeNodes = @NamedAttributeNode(value = "municipalities")),
                @NamedSubgraph(
                        name = "streetAddresses.additionalInformation.graph",
                        attributeNodes = @NamedAttributeNode(value = "additionalInformation")),
                @NamedSubgraph(
                        name = "postOfficeBoxAddresses.postOffices.graph",
                        attributeNodes = @NamedAttributeNode(value = "postOffices")),
                @NamedSubgraph(
                        name = "postOfficeBoxAddresses.postOfficesBoxes.graph",
                        attributeNodes = @NamedAttributeNode(value = "postOfficesBoxes")),
                @NamedSubgraph(
                        name = "postOfficeBoxAddresses.additionalInformation.graph",
                        attributeNodes = @NamedAttributeNode(value = "additionalInformation"))
        }
)
@NamedQueries({
        @NamedQuery(name = "Organization.findAllChangedSince",
                query = Organization.FIND_ALL_CHANGED_QUERY)
})
public class Organization {

    private static final String FIND_CHANGED_QUERY_PART_1 =
            "SELECT DISTINCT mem " +
                    "FROM Member mem " +
                    "LEFT JOIN FETCH mem.subsystems fetchedSubs " +
                    "LEFT JOIN FETCH fetchedSubs.services fetchedSers " +
                    "LEFT JOIN FETCH fetchedSers.wsdls fetchedWsdls " +
                    "WHERE ";
    private static final String FIND_CHANGED_QUERY_PART_2 =
            "mem.statusInfo.changed > :since " +
                    "OR EXISTS ( " +
                    "SELECT sub " +
                    "FROM Subsystem sub " +
                    "WHERE sub.member = mem " +
                    "AND sub.statusInfo.changed > :since)" +
                    "OR EXISTS ( " +
                    "SELECT service " +
                    "FROM Service service " +
                    "WHERE service.subsystem.member = mem " +
                    "AND service.statusInfo.changed > :since)" +
                    "OR EXISTS ( " +
                    "SELECT wsdl " +
                    "FROM Wsdl wsdl " +
                    "WHERE wsdl.service.subsystem.member = mem " +
                    "AND wsdl.statusInfo.changed > :since) ";
    static final String FIND_ALL_CHANGED_QUERY =
            FIND_CHANGED_QUERY_PART_1 + FIND_CHANGED_QUERY_PART_2;

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
    @Embedded
    private StatusInfo statusInfo = new StatusInfo();
    @Getter(AccessLevel.NONE) // do not create default getter, we provide the substitute
    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<OrganizationName> organizationNames = new HashSet<>();
    @Getter(AccessLevel.NONE) // do not create default getter, we provide the substitute
    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<OrganizationDescription> organizationDescriptions = new HashSet<>();
    @Getter(AccessLevel.NONE) // do not create default getter, we provide the substitute
    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Email> emails = new HashSet<>();
    @Getter(AccessLevel.NONE) // do not create default getter, we provide the substitute
    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<PhoneNumber> phoneNumbers = new HashSet<>();
    @Getter(AccessLevel.NONE) // do not create default getter, we provide the substitute
    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Webpage> webPages = new HashSet<>();
    @Getter(AccessLevel.NONE) // do not create default getter, we provide the substitute
    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Address> addresses = new HashSet<>();

}
