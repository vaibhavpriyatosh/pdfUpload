package com.storing.secured.Controller;

import com.storing.secured.Entity.Attachment;
import com.storing.secured.Entity.Shared;
import com.storing.secured.Model.Attachment.AllAttachmentResponse;
import com.storing.secured.Model.Attachment.AttachmentResponse;
import com.storing.secured.Model.Comment.CommentRequest;
import com.storing.secured.Service.AttachmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/home")
@CrossOrigin("*")
public class AttachmentController {
    @Autowired
    private AttachmentService attachmentService;

    @CrossOrigin
    @PostMapping("/upload")
    public AttachmentResponse uploacFile(@RequestParam("file")MultipartFile file) throws Exception {
        Attachment attachment=null;
        attachment =attachmentService.saveAttachment(file);
        String downloadUrl= ServletUriComponentsBuilder.fromCurrentContextPath().replacePath("/home/download/").path(attachment.getId()).toUriString();
        return new AttachmentResponse(
                attachment.getFileName(),downloadUrl, file.getContentType(),attachment.getComments()
        );
    }

    @PostMapping("/add-comments")
    public int addComment(@RequestBody CommentRequest commentRequest){

        return attachmentService.addComment(commentRequest.getComment(),commentRequest.getAttachmentId());

    }

    @PostMapping("/shared")
    public Shared saveShared(@RequestBody Shared shared){
        return attachmentService.saveShared(shared);
    }


    @GetMapping("/download/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileId) throws Exception {
        Attachment attachment=null;
        attachment=attachmentService.getAttachment(fileId);
        return  ResponseEntity
                .ok()
                .contentType(MediaType.parseMediaType(attachment.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\""+attachment.getFileName()+"\"")
                .body(new ByteArrayResource(attachment.getData()));

    }

    @GetMapping("/getAttachments")
    public AllAttachmentResponse getAllAssignment() throws Exception {
        List<Attachment> mineAttachment=attachmentService.getAttachment();
        Attachment attachment=null;
        List<AttachmentResponse> attachmentResponseList = new ArrayList<>();
        List<AttachmentResponse> shareAttachmentResponseList = new ArrayList<>();

        for(int i=0;i<mineAttachment.size();i++){

            attachment=mineAttachment.get(i);

            String downloadUrl= ServletUriComponentsBuilder.fromCurrentContextPath().replacePath("/home/download/").path(attachment.getId()).toUriString();


            AttachmentResponse attachmentResponse=new AttachmentResponse();
            attachmentResponse.setComments(attachment.getComments());
            attachmentResponse.setFileName(attachment.getFileName());
            attachmentResponse.setFileType(attachment.getFileType());
            attachmentResponse.setDownloadURL(downloadUrl);

            attachmentResponseList.add(attachmentResponse);
        }

        List<Shared> sharedList=attachmentService.getShared();
        for(int i=0;i< sharedList.size();i++){
            Shared share=sharedList.get(i);
            attachment=attachmentService.getAttachment(share.getAttachmentId());
            String downloadUrl= ServletUriComponentsBuilder.fromCurrentContextPath().replacePath("/home/download/").path(attachment.getId()).toUriString();


            AttachmentResponse attachmentResponse=new AttachmentResponse();
            attachmentResponse.setComments(attachment.getComments());
            attachmentResponse.setFileName(attachment.getFileName());
            attachmentResponse.setFileType(attachment.getFileType());
            attachmentResponse.setDownloadURL(downloadUrl);

            shareAttachmentResponseList.add(attachmentResponse);
        }

        AllAttachmentResponse allAttachmentResponse= new AllAttachmentResponse();

        allAttachmentResponse.setMyList(attachmentResponseList);
        allAttachmentResponse.setSharedList(shareAttachmentResponseList);

        return allAttachmentResponse;

    }


}
