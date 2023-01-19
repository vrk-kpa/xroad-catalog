/**
 * The MIT License
 *
 * Copyright (c) 2023- Nordic Institute for Interoperability Solutions (NIIS) Copyright (c) 2016-2022 Finnish Digital Agency
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package fi.vrk.xroad.catalog.persistence.repository;

import fi.vrk.xroad.catalog.persistence.entity.ErrorLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Set;

public interface ErrorLogRepository extends CrudRepository<ErrorLog, Long>, PagingAndSortingRepository<ErrorLog, Long> {

    @Query("SELECT e FROM ErrorLog e WHERE e.created >= :startDate AND e.created <= :endDate")
    Set<ErrorLog> findAny(@Param("startDate") LocalDateTime startDate,
                          @Param("endDate") LocalDateTime endDate);

    @Query("SELECT e FROM ErrorLog e WHERE e.created >= :startDate AND e.created <= :endDate "
            + "AND e.xRoadInstance = :xRoadInstance "
            + "AND e.memberClass = :memberClass "
            + "AND e.memberCode = :memberCode "
            + "AND e.subsystemCode = :subsystemCode "
            + "ORDER BY e.created")
    Page<ErrorLog> findAnyByAllParameters(@Param("startDate") LocalDateTime startDate,
                                          @Param("endDate") LocalDateTime endDate,
                                          @Param("xRoadInstance") String xRoadInstance,
                                          @Param("memberClass") String memberClass,
                                          @Param("memberCode") String memberCode,
                                          @Param("subsystemCode") String subsystemCode,
                                          Pageable pageable);

    @Query("SELECT e FROM ErrorLog e WHERE e.created >= :startDate AND e.created <= :endDate "
            + "AND e.xRoadInstance = :xRoadInstance "
            + "AND e.memberClass = :memberClass "
            + "AND e.memberCode = :memberCode "
            + "ORDER BY e.created")
    Page<ErrorLog> findAnyByMemberCode(@Param("startDate") LocalDateTime startDate,
                                       @Param("endDate") LocalDateTime endDate,
                                       @Param("xRoadInstance") String xRoadInstance,
                                       @Param("memberClass") String memberClass,
                                       @Param("memberCode") String memberCode,
                                       Pageable pageable);

    @Query("SELECT e FROM ErrorLog e WHERE e.created >= :startDate AND e.created <= :endDate "
            + "AND e.xRoadInstance = :xRoadInstance "
            + "AND e.memberClass = :memberClass "
            + "ORDER BY e.created")
    Page<ErrorLog> findAnyByMemberClass(@Param("startDate") LocalDateTime startDate,
                                        @Param("endDate") LocalDateTime endDate,
                                        @Param("xRoadInstance") String xRoadInstance,
                                        @Param("memberClass") String memberClass,
                                        Pageable pageable);

    @Query("SELECT e FROM ErrorLog e WHERE e.created >= :startDate AND e.created <= :endDate "
            + "AND e.xRoadInstance = :xRoadInstance ORDER BY e.created")
    Page<ErrorLog> findAnyByInstance(@Param("startDate") LocalDateTime startDate,
                                     @Param("endDate") LocalDateTime endDate,
                                     @Param("xRoadInstance") String xRoadInstance,
                                     Pageable pageable);

    @Query("SELECT e FROM ErrorLog e WHERE e.created >= :startDate AND e.created <= :endDate ORDER BY e.created")
    Page<ErrorLog> findAnyByCreated(@Param("startDate") LocalDateTime startDate,
                                    @Param("endDate") LocalDateTime endDate,
                                    Pageable pageable);

    @Modifying
    @Query("DELETE FROM ErrorLog e WHERE e.created < :oldDate")
    void deleteEntriesOlderThan(@Param("oldDate") LocalDateTime oldDate);
}
