package org.nn.iface.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    private Long id;
    private Long starId;
    private Long userId;
    private String picture;
    private String video;
    private String context;
    private Date created;
    private Date updated;
    private Long starCount;

}
