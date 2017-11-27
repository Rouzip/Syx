package com.CSU.Syx.modelRepository;

import com.CSU.Syx.model.Message;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MessageRepository extends CrudRepository<Message,Long> {
    /**
     * 根据来往名字找到所有的聊天记录，但是两个uid都是相同的
     * @param fromName 发送人的uid
     * @param toName 接收人的uid
     * @return 返回聊天列表
     */
    public List<Message> findByFromNameOrToNameOrderByDateAsc(String fromName,String toName);
}
