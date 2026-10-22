package com.shop.order;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 订单金额计算。
 *
 * 已按 Issue #201 修复：舍入模式改为 HALF_UP。
 */
public class PriceService {

    public BigDecimal calc(BigDecimal price, int qty) {
        if (price == null || qty <= 0) {
            throw new IllegalArgumentException("price/qty invalid");
        }
        BigDecimal amount = price.multiply(BigDecimal.valueOf(qty));
        return amount.setScale(2, RoundingMode.HALF_UP);
    }
}
