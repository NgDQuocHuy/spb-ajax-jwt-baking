package com.quochuy.service.user;

import com.quochuy.model.User;
import com.quochuy.model.dto.UserRegisterDTO;
import com.quochuy.service.IGeneralService;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface IUserService extends IGeneralService<User>, UserDetailsService {

    User getByUsername(String username);

    Optional<User> findByUsername(String username);

    Optional<UserRegisterDTO> findUserDTOByUsername(String username);

    Boolean existsByUsername(String email);
}
