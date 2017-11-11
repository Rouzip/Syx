package com.CSU.Syx.modelRepository;

import com.CSU.Syx.model.Message;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.CSU.Syx.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Rouzip
 * @date 2017.11.11
 */
public interface UserRepository extends MongoRepository<User,Long>{
    /**
     * 此类通过用户姓名从数据库之中查出User的具体信息，然后自己通过get方法得到聊天记录
     *
     * @param name 用户姓名
     * @return  返回用户对象
     */
    public User findUserByName(String name);
}
