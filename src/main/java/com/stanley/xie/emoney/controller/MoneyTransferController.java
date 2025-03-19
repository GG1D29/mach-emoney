package com.stanley.xie.emoney.controller;

import com.stanley.xie.emoney.payload.CommonApiResponse;
import com.stanley.xie.emoney.payload.MoneyTransferRequest;
import com.stanley.xie.emoney.service.MoneyTransferService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class MoneyTransferController {
    private final MoneyTransferService moneyTransferService;

    @PostMapping("/money-transfer")
    public ResponseEntity<?> topUpUserBalance(@RequestHeader(name = "Authorization") String token,
                                              @RequestBody MoneyTransferRequest request) {
        moneyTransferService.transfer(token, request.getToUsername(), request.getAmount());
        return ResponseEntity.ok(new CommonApiResponse("Successful Money Transfer"));
    }
}
