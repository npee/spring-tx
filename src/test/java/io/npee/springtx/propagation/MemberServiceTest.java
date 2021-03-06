package io.npee.springtx.propagation;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.UnexpectedRollbackException;

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
     * {@link MemberService}    @Transaction: OFF
     * {@link MemberRepository} @Transaction: ON
     * {@link LogRepository}    @Transaction: ON
     */
    @Test
    void outer_tx_off_success() {
        String username = "outerTxOff_success";

        memberService.joinV1(username);

        assertTrue(memberRepository.find(username).isPresent());
        assertTrue(logRepository.find(username).isPresent());
    }

    /**
     * {@link MemberService}    @Transaction: OFF
     * {@link MemberRepository} @Transaction: ON
     * {@link LogRepository}    @Transaction: ON, Exception
     */
    @Test
    void outer_tx_off_fail() {
        String username = "로그예외_outerTxOff_fail";

        assertThrows(RuntimeException.class, () -> memberService.joinV1(username));

        assertTrue(memberRepository.find(username).isPresent());
        assertTrue(logRepository.find(username).isEmpty());
    }


    /**
     * {@link MemberService}    @Transaction: ON
     * {@link MemberRepository} @Transaction: OFF
     * {@link LogRepository}    @Transaction: OFF
     */
    @Test
    void single_tx() {
        String username = "outerTxOff_success";

        memberService.joinV1(username);

        assertTrue(memberRepository.find(username).isPresent());
        assertTrue(logRepository.find(username).isPresent());
    }

    /**
     * {@link MemberService}    @Transaction: ON
     * {@link MemberRepository} @Transaction: ON
     * {@link LogRepository}    @Transaction: ON
     */
    @Test
    void outer_tx_on_success() {
        String username = "outerTxOn_success";

        memberService.joinV1(username);

        assertTrue(memberRepository.find(username).isPresent());
        assertTrue(logRepository.find(username).isPresent());
    }

    /**
     * {@link MemberService}    @Transaction: ON
     * {@link MemberRepository} @Transaction: ON
     * {@link LogRepository}    @Transaction: ON, Exception
     */
    @Test
    void outer_tx_on_fail() {
        String username = "로그예외_outerTxOn_fail";

        assertThrows(RuntimeException.class, () -> memberService.joinV1(username));

        assertTrue(memberRepository.find(username).isEmpty());
        assertTrue(logRepository.find(username).isEmpty());
    }

    /**
     * {@link MemberService}    @Transaction: ON
     * {@link MemberRepository} @Transaction: ON
     * {@link LogRepository}    @Transaction: ON, Exception
     */
    @Test
    void recover_exception_fail() {
        String username = "로그예외_recoverException_fail";

        assertThrows(UnexpectedRollbackException.class, () -> memberService.joinV2(username));

        assertTrue(memberRepository.find(username).isEmpty());
        assertTrue(logRepository.find(username).isEmpty());
    }

    /**
     * {@link MemberService}    @Transaction: ON
     * {@link MemberRepository} @Transaction: ON
     * {@link LogRepository}    @Transaction: ON(REQUIRES_NEW), Exception
     */
    @Test
    void recover_exception_success() {
        String username = "로그예외_recoverException_success";

        memberService.joinV2(username);

        // member 저장, 로그 롤백
        assertTrue(memberRepository.find(username).isPresent());
        assertTrue(logRepository.find(username).isEmpty());
    }

}