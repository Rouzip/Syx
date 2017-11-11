package com.CSU.Syx.model;

import lombok.Data;
import org.springframework.data.annotation.Id;

/**
 * @author Rouzip
 * @date 2017.11.8
 */
@Data
public class Role {
    @Id
    private Long uid;
    private String role;
    private String user;
}
