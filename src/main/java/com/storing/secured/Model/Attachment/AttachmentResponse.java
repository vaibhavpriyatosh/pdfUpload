package com.storing.secured.Model.Attachment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class AttachmentResponse {
    private String fileName;
    private String downloadURL;
    private String fileType;

    private String[] comments;
}
