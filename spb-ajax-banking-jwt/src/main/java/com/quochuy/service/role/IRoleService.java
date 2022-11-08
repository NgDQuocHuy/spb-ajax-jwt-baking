package com.quochuy.service.role;

import com.quochuy.model.Role;
import com.quochuy.service.IGeneralService;

public interface IRoleService extends IGeneralService<Role> {
    Role findByName(String name);
}
