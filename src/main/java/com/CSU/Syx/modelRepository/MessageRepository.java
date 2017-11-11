package com.CSU.Syx.modelRepository;

import com.CSU.Syx.model.Message;
import org.springframework.data.repository.CrudRepository;

public interface MessageRepository extends CrudRepository<Message,Long> {
}
