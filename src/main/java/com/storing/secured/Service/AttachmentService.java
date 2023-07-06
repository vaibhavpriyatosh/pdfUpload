package com.storing.secured.Service;

import com.storing.secured.Entity.Attachment;
import com.storing.secured.Entity.Shared;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AttachmentService {
    Attachment saveAttachment(MultipartFile file) throws Exception;

    Attachment getAttachment(String fileId) throws Exception;

    int addComment(String comment, String attachmentId);

    Shared saveShared(Shared shared);

    List<Attachment> getAttachment();

    List<Shared> getShared();

}
