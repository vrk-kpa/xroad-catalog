/**
 * The MIT License
 * Copyright (c) 2020, Population Register Centre (VRK)
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
@ToString(exclude = {"postOfficeBoxAddress","postOfficeBoxAddressMunicipalityNames"})
@EqualsAndHashCode(exclude = {"id","postOfficeBoxAddress","postOfficeBoxAddressMunicipalityNames","statusInfo"})
@Builder
public class PostOfficeBoxAddressMunicipality {
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "POST_OFFICE_BOX_ADDRESS_MUNICIPALITY_GEN")
    @SequenceGenerator(name = "POST_OFFICE_BOX_ADDRESS_MUNICIPALITY_GEN", sequenceName = "POST_OFFICE_BOX_ADDRESS_MUNICIPALITY_ID_SEQ", allocationSize = 1)
    private long id;
    @Column(nullable = false)
    private String code;
    @Builder.Default
    @Embedded
    private StatusInfo statusInfo = new StatusInfo();
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "POST_OFFICE_BOX_ADDRESS_ID")
    private PostOfficeBoxAddress postOfficeBoxAddress;
    @Builder.Default
    @Getter(AccessLevel.NONE) // do not create default getter, we provide the substitute
    @OneToMany(mappedBy = "postOfficeBoxAddressMunicipality", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<PostOfficeBoxAddressMunicipalityName> postOfficeBoxAddressMunicipalityNames = new HashSet<>();

    /**
     * This collection can be used to add new items
     *
     * @return Set of all PostOfficeBoxAddressMunicipalityName
     */
    public Set<PostOfficeBoxAddressMunicipalityName> getAllMunicipalityNames() {
        return postOfficeBoxAddressMunicipalityNames;
    }
}