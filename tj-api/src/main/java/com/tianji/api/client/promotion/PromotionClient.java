package com.tianji.api.client.promotion;

import com.tianji.api.client.promotion.fallback.PromotionClientFallback;
import com.tianji.api.dto.promotion.CouponDiscountDTO;
import com.tianji.api.dto.promotion.OrderCouponDTO;
import com.tianji.api.dto.promotion.OrderCourseDTO;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

//fallbackFactory:指明降级类对象
@FeignClient(value = "promotion-service", fallbackFactory = PromotionClientFallback.class)
public interface PromotionClient {
    @PostMapping("/user-coupons/available")
    List<CouponDiscountDTO> findDiscountSolution(@RequestBody List<OrderCourseDTO> orderCourses);

    @ApiOperation("退还指定优惠券")
    @PutMapping("/user-coupons/refund")
    void refundCoupon(@ApiParam("用户优惠券id集合") @RequestParam("couponIds") List<Long> userCouponIds);

    @PostMapping("/user-coupons/discount")
    CouponDiscountDTO queryDiscountDetailByOrder(@RequestBody OrderCouponDTO orderCouponDTO);

    @PutMapping("/user-coupons/use")
    void writeOffCoupon(@RequestParam("couponIds") List<Long> userCouponIds);
}
