package org.nn.iface.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    private Date time;
    private String name;
    private String contentText;
    private String contentPhotos;
    private String avatar;
    private Long heartCount;
}

