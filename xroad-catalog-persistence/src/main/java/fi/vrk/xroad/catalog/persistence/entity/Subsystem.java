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
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@ToString(exclude = {"services"})
// identity is based on xroad identity (instance, member code...)
@EqualsAndHashCode(exclude = {"id", "services", "statusInfo"})
public class Subsystem {

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SUBSYSTEM_GEN")
    @SequenceGenerator(name = "SUBSYSTEM_GEN", sequenceName = "SUBSYSTEM_ID_SEQ", allocationSize = 1)
    private long id;
    @Column(nullable = false)
    private String subsystemCode;
    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;
    @Getter(AccessLevel.NONE) // do not create default getter, we provide the substitute
    @OneToMany(mappedBy = "subsystem", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Service> services = new HashSet<>();
    @Embedded
    private StatusInfo statusInfo = new StatusInfo();

    public Subsystem() {
        // Empty constructor
    }

    /**
     * create key based on xroad identifiers
     *
     * @return SubsystemId
     */
    public SubsystemId createKey() {
        return new SubsystemId(
                getMember().getXRoadInstance(),
                getMember().getMemberClass(),
                getMember().getMemberCode(),
                subsystemCode);
    }

    /**
     * Constructor for tests
     *
     */
    public Subsystem(Member member, String subsystemCode) {
        this.member = member;
        this.subsystemCode = subsystemCode;
        statusInfo.setTimestampsForNew(LocalDateTime.now());
    }

    /**
     * Note: Read-only collection, do not use this to modify collection
     *
     * @return Set of active services
     */
    public Set<Service> getActiveServices() {
        return Collections.unmodifiableSet(services.stream()
                .filter(service -> !service.getStatusInfo().isRemoved())
                .collect(Collectors.toSet()));
    }

    /**
     * This collection can be used to add new items
     *
     * @return Set of all services
     */
    public Set<Service> getAllServices() {
        return services;
    }

}
