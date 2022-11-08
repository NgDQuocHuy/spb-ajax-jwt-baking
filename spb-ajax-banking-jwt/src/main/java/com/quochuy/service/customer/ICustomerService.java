package com.quochuy.service.customer;

import com.quochuy.model.Customer;
import com.quochuy.model.Deposit;
import com.quochuy.model.Transfer;
import com.quochuy.model.Withdraw;
import com.quochuy.model.dto.CustomerDTO;
import com.quochuy.model.dto.ICustomerDTO;
import com.quochuy.model.dto.RecipientDTO;
import com.quochuy.service.IGeneralService;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ICustomerService extends IGeneralService<Customer> {

    List<Customer> findAllByDeletedIsFalse();

    List<CustomerDTO> getAllCustomerDTOByDeletedIsFalse();

    List<ICustomerDTO> getAllICustomerDTOByDeletedIsFalse();

    List<Customer> findAllByIdNot(long id);

    Boolean existsByIdEquals(long id);

    List<Customer> getAllByIdNot(long senderId);

    List<RecipientDTO> getAllRecipientDTO(long senderId);

    Customer deposit(Customer customer, Deposit deposit);

    Customer withdraw(Customer customer, Withdraw withdraw);

    Customer transfer(Transfer transfer);

    void softDelete(@Param("customerId") long customerId);
}
