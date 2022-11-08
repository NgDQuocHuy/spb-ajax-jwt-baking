package com.quochuy.model.dto;

import com.quochuy.model.Customer;
import com.quochuy.model.LocationRegion;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class CustomerUpdateDTO {

    private long id;
    private String fullName;
    private String email;
    private String phone;
    private String balance;
    private LocationRegionDTO locationRegion;

    public CustomerUpdateDTO(long id, String fullName, String email, String phone, BigDecimal balance, LocationRegion locationRegion) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.locationRegion = locationRegion.toLocationRegionDTO();
    }

    public Customer toCustomer(){
        return new Customer()
                .setId(id)
                .setFullName(fullName)
                .setEmail(email)
                .setPhone(phone)
                .setBalance(new BigDecimal(balance))
                .setLocationRegion(locationRegion.toLocationRegion());
    }
}
