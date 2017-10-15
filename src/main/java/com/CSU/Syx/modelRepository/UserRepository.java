package com.CSU.Syx.modelRepository;

import com.CSU.Syx.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User,Long> {
}
