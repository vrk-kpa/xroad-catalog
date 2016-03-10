package fi.vrk.xroad.catalog.persistence;

import fi.vrk.xroad.catalog.persistence.entity.Member;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(Application.class)
@Transactional
@Slf4j
public class MemberTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    TestUtil testUtil;

    @Test
    public void testGetActiveSubsystems() {
        // 1,2 are active, 12 is removed
        Member member = memberRepository.findOne(1L);
        assertEquals(Arrays.asList(1L,2L),
                new ArrayList<Long>(testUtil.getIds(member.getActiveSubsystems())));
    }

    @Test
    public void testGetAllSubsystems() {
        Member member = memberRepository.findOne(1L);
        assertEquals(Arrays.asList(1L,2L,12L),
                new ArrayList<Long>(testUtil.getIds(member.getAllSubsystems())));
    }

}