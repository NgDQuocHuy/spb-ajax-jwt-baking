package com.quochuy.controller.rest;


import com.quochuy.model.Transfer;
import com.quochuy.model.dto.CustomerDTO;
import com.quochuy.model.dto.TransferHistoryDTO;
import com.quochuy.model.dto.TransferHistoryWithSumFeesAmountDTO;
import com.quochuy.service.transfer.ITransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/transfers")
public class TransferRestController {

    @Autowired
    private ITransferService transferService;

    @GetMapping("/get-all-histories")
    public ResponseEntity<?> getAllHistories() {

        List<TransferHistoryDTO> transferHistoryDTOS = transferService.getAllHistories();

        return new ResponseEntity<>(transferHistoryDTOS, HttpStatus.OK);
    }


    @GetMapping("/get-sum-fees-amount")
    public ResponseEntity<?> getSumFeesAmount() {

        BigDecimal getAllFeesAmount = transferService.getSumFeesAmount();

        return new ResponseEntity<>(getAllFeesAmount, HttpStatus.OK);
    }

    @GetMapping("/get-all-histories-with-sum-fees-amount")
    public ResponseEntity<?> getAllHistoriesWithSumFeesAmount() {

        List<TransferHistoryDTO> transferHistoryDTOS = transferService.getAllHistories();

        BigDecimal getAllFeesAmount = transferService.getSumFeesAmount();

        TransferHistoryWithSumFeesAmountDTO transferHistoryWithSumFeesAmountDTO = new TransferHistoryWithSumFeesAmountDTO();
        transferHistoryWithSumFeesAmountDTO.setTransferHistories(transferHistoryDTOS);
        transferHistoryWithSumFeesAmountDTO.setSumFeesAmount(getAllFeesAmount);

        return new ResponseEntity<>(transferHistoryWithSumFeesAmountDTO, HttpStatus.OK);
    }

}