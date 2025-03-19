package com.stanley.xie.emoney.controller;

import com.stanley.xie.emoney.payload.CommonApiResponse;
import com.stanley.xie.emoney.payload.TopUpBalanceRequest;
import com.stanley.xie.emoney.payload.UserBalanceResponse;
import com.stanley.xie.emoney.service.UserBalanceService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class UserBalanceController {
    private final UserBalanceService userBalanceService;

    @GetMapping("/user-balance")
    public ResponseEntity<?> getUserBalance(@RequestHeader(name = "Authorization") String token) {
        int balance = userBalanceService.fetchBalance(token);
        return ResponseEntity.ok(new UserBalanceResponse(balance));
    }

    @PostMapping("/user-balance")
    public ResponseEntity<?> topUpUserBalance(@RequestHeader(name = "Authorization") String token,
                                              @RequestBody TopUpBalanceRequest request) {
        userBalanceService.topUp(token, request.getAmount());
        return ResponseEntity.ok(new CommonApiResponse("Successful Top Up"));
    }
}
