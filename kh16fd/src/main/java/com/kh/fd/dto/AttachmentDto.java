package com.kh.fd.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttachmentDto {
    private int attachmentNo;
    private String attachmentName;
    private int attachmentSize;
    private String attachmentType;
    private java.sql.Timestamp attachmentTime;
}
