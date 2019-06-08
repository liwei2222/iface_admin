package org.nn.iface.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    private Date time;
    private String name;
    private String contentText;
    private String avatar;
    private Long heartCount;
    private List<String> contentPhotos;
}

