package com.shop.order;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 订单金额计算。
 *
 * 注意：主分支上这里用的是 RoundingMode.DOWN（截断），
 * 这就是 Issue #201「下单金额偶发多算 0.01」的根因。
 */
public class PriceService {

    public BigDecimal calc(BigDecimal price, int qty) {
        if (price == null || qty <= 0) {
            throw new IllegalArgumentException("price/qty invalid");
        }
        BigDecimal amount = price.multiply(BigDecimal.valueOf(qty));
        return amount.setScale(2, RoundingMode.DOWN);   // BUG: 应为 HALF_UP
    }
}
