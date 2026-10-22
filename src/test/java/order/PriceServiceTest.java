package com.shop.order;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PriceServiceTest {

    private final PriceService service = new PriceService();

    @Test
    void testRounding() {
        // 10.005 按 HALF_UP 应该是 10.01；用 DOWN 会得到 10.00
        BigDecimal amount = service.calc(new BigDecimal("3.335"), 3);
        assertEquals(new BigDecimal("10.01"), amount);
    }
}
