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
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"company"})
@EqualsAndHashCode(exclude = {"id", "company","statusInfo"})
@Builder
public class ContactDetail {

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CONTACT_DETAIL_GEN")
    @SequenceGenerator(name = "CONTACT_DETAIL_GEN", sequenceName = "CONTACT_DETAIL_ID_SEQ", allocationSize = 1)
    private long id;
    @Column
    private long source;
    @Column(nullable = false)
    private long version;
    @Column
    private String language;
    @Column(nullable = false)
    private String value;
    @Column(nullable = false)
    private String type;
    @Column(nullable = false)
    private LocalDateTime registrationDate;
    @Column
    private LocalDateTime endDate;
    @Builder.Default
    @Embedded
    private StatusInfo statusInfo = new StatusInfo();
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COMPANY_ID")
    private Company company;
}
