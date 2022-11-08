package com.quochuy.controller.rest;


import com.quochuy.exception.DataInputException;
import com.quochuy.model.*;
import com.quochuy.model.dto.*;
import com.quochuy.service.customer.ICustomerService;
import com.quochuy.service.locationRegion.ILocationRegionService;
import com.quochuy.util.AppUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/api/customers")
public class CustomerRestController {

    @Autowired
    private AppUtil appUtil;

    @Autowired
    private ICustomerService customerService;

    @Autowired
    private ILocationRegionService locationRegionService;

    @GetMapping
    public ResponseEntity<?> findAllByDeletedIsFalse() {

        List<CustomerDTO> customers = customerService.getAllCustomerDTOByDeletedIsFalse();

        return new ResponseEntity<>(customers, HttpStatus.OK);
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<?> getById(@PathVariable long customerId) {

        Optional<Customer> customerOptional = customerService.findById(customerId);

        if (!customerOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Customer customer = customerOptional.get();

        return new ResponseEntity<>(customer.toCustomerDTO(), HttpStatus.OK);
    }

    @GetMapping("/get-all-recipients-without-sender/{senderId}")
    public ResponseEntity<?> getAllRecipientsWithoutSender(@PathVariable long senderId) {

        Optional<Customer> senderOptional = customerService.findById(senderId);

        if (!senderOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

//        List<Customer> recipients = customerService.getAllByIdNot(senderId);
//
//        List<RecipientDTO> recipientDTOS = new ArrayList<>();
//
//        for (Customer item : recipients) {
//            recipientDTOS.add(item.toRecipientDTO());
//        }

        List<RecipientDTO> recipientDTOS = customerService.getAllRecipientDTO(senderId);


        return new ResponseEntity<>(recipientDTOS, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<CustomerCreateDTO> create(@RequestBody CustomerCreateDTO customerDTO) {

        customerDTO.setId(0L);
        Customer customer = customerDTO.toCustomer();

        Customer newCustomer = customerService.save(customer);

        customerDTO.setId(newCustomer.getId());
        customerDTO.setBalance("0");

        return new ResponseEntity<>(newCustomer.toCustomerCreateDTO(), HttpStatus.CREATED);
    }

    @PostMapping("/update/{customerId}")
    public ResponseEntity<CustomerDTO> update(@PathVariable long customerId, @RequestBody CustomerUpdateDTO customerUpdateDTO) {
        Optional<Customer> customerOptional = customerService.findById(customerId);
        Optional<LocationRegion> locationRegionOptional = locationRegionService.findById(customerOptional.get().getLocationRegion().getId());

        if (!customerOptional.isPresent()) {
            throw new DataInputException("Thông tin khách hàng cần sửa không hợp lệ");
        }

        if (!locationRegionOptional.isPresent()) {
            throw new DataInputException("Thông tin khách hàng cần sửa không hợp lệ");
        }

        try {
            customerUpdateDTO.setBalance(customerOptional.get().getBalance().toString());
            customerUpdateDTO.setId(customerId);

            LocationRegion locationRegion = customerUpdateDTO.getLocationRegion().toLocationRegion();

            Customer oldCustomer = customerService.save(customerUpdateDTO.toCustomer());

            locationRegionService.remove(locationRegionOptional.get().getId());

            return new ResponseEntity<>(oldCustomer.toCustomerDTO(), HttpStatus.ACCEPTED);
        } catch (Exception e) {
            e.printStackTrace();
            throw new DataInputException("Vui lòng liên hệ Administrator");
        }

    }

    @PostMapping("/deposit")
    public ResponseEntity<CustomerDTO> deposit(@RequestBody DepositDTO depositDTO) {

        long customerId = depositDTO.getCustomerId();

        Optional<Customer> customerOptional = customerService.findById(customerId);

        if (!customerOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Deposit deposit = new Deposit();
        BigDecimal transactionAmount = new BigDecimal(depositDTO.getTransactionAmount());
        deposit.setTransactionAmount(transactionAmount);
        deposit.setCustomer(customerOptional.get());

        Customer newCustomer = customerService.deposit(customerOptional.get(), deposit);

        return new ResponseEntity<>(newCustomer.toCustomerDTO(), HttpStatus.CREATED);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<CustomerDTO> withdraw(@RequestBody WithdrawDTO withdrawDTO) {

        long customerId = withdrawDTO.getCustomerId();

        Optional<Customer> customerOptional = customerService.findById(customerId);

        if (!customerOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        BigDecimal transactionAmount = new BigDecimal(Long.parseLong(withdrawDTO.getTransactionAmount()));

        if (customerOptional.get().getBalance().compareTo(transactionAmount) < 0){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Withdraw withdraw = new Withdraw();
        withdraw.setTransactionAmount(transactionAmount);
        withdraw.setCustomer(customerOptional.get());

        Customer newCustomer = customerService.withdraw(customerOptional.get(), withdraw);

        return new ResponseEntity<>(newCustomer.toCustomerDTO(), HttpStatus.CREATED);
    }

    @PostMapping("/transfer")
    public ResponseEntity<?> transfer(@Validated @RequestBody TransferDTO transferDTO, BindingResult bindingResult) {

        new TransferDTO().validate(transferDTO, bindingResult);

        Optional<Customer> senderOptional = customerService.findById(transferDTO.getSenderId());
        Optional<Customer> recipientOptional = customerService.findById(transferDTO.getRecipientId());


        if (bindingResult.hasFieldErrors()) {
            return appUtil.mapErrorToResponse(bindingResult);
        }

        if (!senderOptional.isPresent()) {
            throw new DataInputException("Thông tin người gửi không hợp lệ");
        }

        if (!recipientOptional.isPresent()) {
            throw new DataInputException("Thông tin người nhận không hợp lệ");
        }

        if (senderOptional.get().getId() == recipientOptional.get().getId()) {
            throw new DataInputException("Thông tin người gửi và nhận không hợp lệ");
        }

        Customer sender = senderOptional.get();

        BigDecimal currentBalanceSender = sender.getBalance();

        String transferAmountStr = transferDTO.getTransferAmount();
        BigDecimal transferAmount = new BigDecimal(Long.parseLong(transferAmountStr));
        long fees = 10;
        BigDecimal feesAmount = transferAmount.multiply(new BigDecimal(fees)).divide(new BigDecimal(100L));
        BigDecimal transactionAmount = transferAmount.add(feesAmount);

        if (currentBalanceSender.compareTo(transactionAmount) < 0) {
            throw new DataInputException("Số dư người gửi không đủ thực hiện giao dịch");
        }

        try {
            Transfer transfer = new Transfer();
            transfer.setId(0L);
            transfer.setSender(sender);
            transfer.setRecipient(recipientOptional.get());
            transfer.setTransferAmount(transferAmount);
            transfer.setFees(fees);
            transfer.setFeesAmount(feesAmount);
            transfer.setTransactionAmount(transactionAmount);

            Customer newSender = customerService.transfer(transfer);

            Optional<Customer> newRecipient = customerService.findById(transferDTO.getRecipientId());

            Map<String, CustomerDTO> results = new HashMap<>();
            results.put("sender", newSender.toCustomerDTO());
            results.put("recipient", newRecipient.get().toCustomerDTO());

            return new ResponseEntity<>(results, HttpStatus.CREATED);
        } catch (Exception e) {
            throw new DataInputException("Vui lòng liên hệ Administrator");
        }
    }

    @DeleteMapping("/delete/{customerId}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<CustomerDTO> delete(@PathVariable long customerId) {

        Optional<Customer> customerOptional = customerService.findById(customerId);

        if (!customerOptional.isPresent()) {
            throw new DataInputException("Thông tin khách hàng cần xóa không hợp lệ");
        }

        try {
            customerService.softDelete(customerId);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } catch (Exception e) {
            e.printStackTrace();
            throw new DataInputException("Vui lòng liên hệ Administrator");
        }
    }
}
