package io.npee.springtx.propagation;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    LogRepository logRepository;

    /**
     * memberService    @Transaction: OFF
     * memberRepository @Transaction: ON
     * logRepository    @Transaction: ON
     */
    @Test
    void outer_tx_off_success() {
        String username = "outerTxOff_success";

        memberService.joinV1(username);

        assertTrue(memberRepository.find(username).isPresent());
        assertTrue(logRepository.find(username).isPresent());
    }

    /**
     * memberService    @Transaction: OFF
     * memberRepository @Transaction: ON
     * logRepository    @Transaction: ON, Exception
     */
    @Test
    void outer_tx_off_fail() {
        String username = "로그예외_outerTxOff_fail";

        assertThrows(RuntimeException.class, () -> memberService.joinV1(username));

        assertTrue(memberRepository.find(username).isPresent());
        assertTrue(logRepository.find(username).isEmpty());
    }


}