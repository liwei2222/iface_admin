package org.nn.iface.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCommentDto {
    private String starName;
    private String userName;
    private String context;
    private MultipartFile[] files;
}
