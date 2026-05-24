package com.agroerp.controller;

import com.agroerp.response.ApiResponse;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/policies")
public class PolicyController {
    @GetMapping("/returns")
    public ApiResponse<Map<String, Object>> returnPolicy() {
        return ApiResponse.ok("Return policy rules", Map.of(
                "returnAllowedDays", 7,
                "batchExpiryValidation", true,
                "approvalRequired", true,
                "refundMethods", new String[]{"REFUND", "DUE_ADJUSTMENT", "EXCHANGE"}
        ));
    }
}
