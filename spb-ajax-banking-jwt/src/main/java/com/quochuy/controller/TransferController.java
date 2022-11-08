package com.quochuy.controller;


import com.quochuy.model.Customer;
import com.quochuy.model.Deposit;
import com.quochuy.model.Transfer;
import com.quochuy.service.transfer.ITransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/transfers")
public class TransferController {

    @Autowired
    private ITransferService transferService;

    @GetMapping
    public ModelAndView showListHistory() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("transfer/history-transfer");

        List<Transfer> transfers = transferService.findAll();

        BigDecimal getAllFeesAmount = transferService.getSumFeesAmount();

        modelAndView.addObject("transfers", transfers);
        modelAndView.addObject("allFeesAmount", getAllFeesAmount);

        return modelAndView;
    }
}
