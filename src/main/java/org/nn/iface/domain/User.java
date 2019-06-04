package org.nn.iface.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long id;
    private String name;
    private String pic;
    private String sign;
    private String password;
    private String sex;
    private Date created;
    private Date updated;
}
