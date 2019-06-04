package org.nn.iface.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Star {
    private Long id;
    private String name;
    private String introduce;
    private String pic;
    private String works;
    private Date created;
    private Date updated;
    private String feature;
}
