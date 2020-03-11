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

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString(exclude = {"subsystem","wsdls","openApis"})
public class Service {
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SERVICE_GEN")
    @SequenceGenerator(name = "SERVICE_GEN", sequenceName = "SERVICE_ID_SEQ", allocationSize = 1)
    private long id;
    @ManyToOne
    @JoinColumn(name = "SUBSYSTEM_ID")
    private Subsystem subsystem;
    @Column(nullable = false)
    private String serviceCode;
    private String serviceVersion;
    @Embedded
    private StatusInfo statusInfo = new StatusInfo();
    // using OneToMany wsdls instead of simpler - first choice - OneToOne wsdl
    // due to OneToOne not working with lazy loading:
    // http://stackoverflow.com/questions/1444227/making-a-onetoone-relation-lazy
    // https://developer.jboss.org/wiki/SomeExplanationsOnLazyLoadingone-to-one
    @Getter(AccessLevel.NONE) // do not create default getter/setter, we provide a wrapper that hides the collection
    @OneToMany(mappedBy = "service", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Wsdl> wsdls = new HashSet<>();
    @Getter(AccessLevel.NONE) // do not create default getter/setter, we provide a wrapper that hides the collection
    @OneToMany(mappedBy = "service", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<OpenApi> openApis = new HashSet<>();


    public Service() {
        // Empty constructor
    }

    /**
     * Constructor for all args
     */
    public Service(Subsystem subsystem, String serviceCode, String serviceVersion) {
        this.subsystem = subsystem;
        this.serviceCode = serviceCode;
        this.serviceVersion = serviceVersion;
        statusInfo.setTimestampsForNew(LocalDateTime.now());
    }

    /**
     * Add given wsdl to set of wsdls. Create the set if needed.
     */
    public void setWsdl(Wsdl wsdl) {
        if (wsdls == null) {
            wsdls = new HashSet<>();
        }
        wsdls.clear();
        wsdls.add(wsdl);
    }
    public Wsdl getWsdl() { return wsdls.isEmpty() ? null : wsdls.iterator().next(); }

    /**
     * Add given openApi to set of openApis. Create the set if needed.
     */
    public void setOpenApi(OpenApi openApi) {
        if (openApis == null) {
            openApis = new HashSet<>();
        }
        openApis.clear();
        openApis.add(openApi);
    }
    public OpenApi getOpenApi() { return openApis.isEmpty() ? null : openApis.iterator().next(); }

    /**
     * @return comparable & equals-able natural key _within one subsystem_
     */
    public ServiceId createKey() {
        return new ServiceId(serviceCode, serviceVersion);
    }

}
