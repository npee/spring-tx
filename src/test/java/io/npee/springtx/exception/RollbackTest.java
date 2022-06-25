package io.npee.springtx.exception;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@SpringBootTest
public class RollbackTest {

    @Autowired
    RollbackService rollbackService;

    @Test
    void runtime_exception() {
        rollbackService.runtimeException();
    }

    @Test
    void checked_exception() throws MyException {
        rollbackService.checkedException();
    }

    @Test
    void rollback_for() throws MyException {
        rollbackService.rollbackFor();
    }

    @TestConfiguration
    static class RollBackTestConfig {

        @Bean
        RollbackService rollbackService() {
            return new RollbackService();
        }
    }

    @Slf4j
    static class RollbackService {

        //런타임 예외 -> rollback
        @Transactional
        public void runtimeException() {
            log.info("call RuntimeException");
            throw new RuntimeException();
        }

        //체크 예외 -> commit
        @Transactional
        public void checkedException() throws MyException {
            log.info("call CheckedException");
            throw new MyException();
        }

        //체크 에외, rollbackFor -> rollback
        @Transactional(rollbackFor = MyException.class)
        public void rollbackFor() throws MyException {
            log.info("call CheckedException");
            throw new MyException();
        }
    }

    static class MyException extends Exception {

    }
}
