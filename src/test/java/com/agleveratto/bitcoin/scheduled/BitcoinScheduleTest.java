package com.agleveratto.bitcoin.scheduled;

import com.agleveratto.bitcoin.BitcoinSpringWebApplication;
import org.awaitility.Duration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;

@SpringJUnitConfig(BitcoinSpringWebApplication.class)
public class BitcoinScheduleTest {

    @SpyBean
    BitcoinSchedule bitcoinSchedule;

    @Test
    public void whenWaitOneMinute_thenScheduledIsCalledAtLeastSixTimes(){
        await().atMost(Duration.ONE_MINUTE)
                .untilAsserted(() -> verify(bitcoinSchedule, atLeast(1)).getBitcoinPrice());
    }

    @Test
    public void whenWaitTwoMinutes_thenScheduledIsCalledAtLeastOneTime(){
        await().atMost(Duration.TWO_MINUTES)
                .untilAsserted(() -> verify(bitcoinSchedule, atLeast(7)).getBitcoinPrice());
    }
}
