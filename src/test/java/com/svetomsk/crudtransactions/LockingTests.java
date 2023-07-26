package com.svetomsk.crudtransactions;

import com.svetomsk.crudtransactions.dao.UserDao;
import com.svetomsk.crudtransactions.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@SpringBootTest
public class LockingTests {
    private static final int NUMBER_OF_CONCURRENT_USERS = 5;

    private final CountDownLatch startLatch = new CountDownLatch(1);
    private final CountDownLatch requestLatch = new CountDownLatch(NUMBER_OF_CONCURRENT_USERS);
    private final ExecutorService requestPool = Executors.newFixedThreadPool(NUMBER_OF_CONCURRENT_USERS);

    @Autowired
    private UserDao userDao;

    @Test
    public void concurrentUserCreationTest() {
        long startNumber = userDao.count();
        IntStream.rangeClosed(1, NUMBER_OF_CONCURRENT_USERS).forEach((item) -> {
            requestPool.submit(() -> {
                await(startLatch);
                userDao.findByInfoOrCreate(UserDto.builder().phoneNumber("+1").build());
                log.info("Item " + item + " finished");
                requestLatch.countDown();
            });
        });

        startLatch.countDown();
        await(requestLatch);
        log.info("All operation finished");
        assertEquals(startNumber + 1, userDao.count());
    }

    @AfterEach
    public void tearDown() throws InterruptedException {
        requestPool.shutdown();
        requestPool.awaitTermination(10, TimeUnit.SECONDS);
        requestPool.shutdownNow();
    }

    private void await(CountDownLatch latch) {
        try {
            latch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

