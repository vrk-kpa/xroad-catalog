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
package fi.vrk.xroad.catalog.persistence.repository;

import fi.vrk.xroad.catalog.persistence.entity.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * Basic CRUD for member
 */
public interface MemberRepository extends CrudRepository<Member, Long> {

    @EntityGraph(value = "member.full-tree.graph",
            type = EntityGraph.EntityGraphType.FETCH)
    Set<Member> findAll();

    @EntityGraph(value = "member.full-tree.graph",
            type = EntityGraph.EntityGraphType.FETCH)
    @Query("SELECT m FROM Member m WHERE m.statusInfo.removed IS NULL")
    Set<Member> findAllActive();

    @Query("SELECT m FROM Member m WHERE m.memberClass = :memberClass")
    Set<Member> findAllByClass(@Param("memberClass") String memberClass);

    // uses named query Member.findAllChangedSince
    Set<Member> findAllChangedSince(@Param("since") LocalDateTime since);

    // uses named query Member.findActiveChangedSince
    Set<Member> findActiveChangedSince(@Param("since") LocalDateTime since);

    /**
     * Returns only active items (non-deleted)
     * @param xRoadInstance X-Road instance parameter, for example FI
     * @param memberClass X-Road member class, for example GOF
     * @param memberCode X-Road member class, for example Company code
     * @return Member found
     */
    @Query("SELECT m FROM Member m WHERE m.xRoadInstance = :xRoadInstance "
            + "AND m.memberClass = :memberClass "
            + "AND m.memberCode = :memberCode "
            + "AND m.statusInfo.removed IS NULL")
    Member findActiveByNaturalKey(@Param("xRoadInstance") String xRoadInstance,
                                  @Param("memberClass") String memberClass,
                                  @Param("memberCode") String memberCode);

}
