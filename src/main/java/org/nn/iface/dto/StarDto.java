package org.nn.iface.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StarDto {
    private String name;
    private String url;
    private String introduce;
    private String works;
    private List<CommentDto> commentList;
}
