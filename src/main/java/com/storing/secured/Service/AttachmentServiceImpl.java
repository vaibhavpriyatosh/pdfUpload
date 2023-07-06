package com.storing.secured.Service;

import com.mysql.cj.util.StringUtils;
import com.storing.secured.Entity.Attachment;
import com.storing.secured.Entity.Shared;
import com.storing.secured.Entity.User;
import com.storing.secured.Repositories.AttachmentRepository;
import com.storing.secured.Repositories.SharedRepository;
import com.storing.secured.Security.JwtHelper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class AttachmentServiceImpl implements AttachmentService{

    @Autowired
    private SharedRepository sharedRepository;

    @Autowired
    private AttachmentRepository attachmentRepository;

    @Autowired
    HttpServletRequest request;

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private UserService userService;

    private User user;
    public void setUser (){
        String requestHeader = request.getHeader("Authorization");
        String token = requestHeader.substring(7);
        String email= (jwtHelper.getUsernameFromToken(token));
        user= userService.getUserByEmail(email);
    }




    @Override
    public Attachment saveAttachment(MultipartFile file) throws Exception {
        String fileName= StringUtils.urlEncode(file.getOriginalFilename());
        setUser();
        System.out.println(user);
        try{
            if(fileName.contains("..")){
                throw new Exception("Filename contains invalid path sequence "+fileName);
            }
            Attachment attachment=
                    new Attachment(fileName,file.getContentType(),file.getBytes());
            attachment.setUserId(user.getUserId());
            attachment.setComments(new String[0]);
            return attachmentRepository.save(attachment);
        }catch (Exception e) {
            throw new Exception("Could not save File: "+ fileName);
        }
    }

    @Override
    public Attachment getAttachment(String fileId) throws Exception {
        return attachmentRepository.findById(fileId)
                .orElseThrow(()->new Exception("File not found with Id: "+fileId));
    }

    @Transactional
    @Override
    public int addComment(String comment, String attachmentId) {
        setUser();
        Attachment attachment =  attachmentRepository.findById(attachmentId).get();
        String currentArray[]=(attachment.getComments());

        String[] newArray = new String[currentArray.length + 1];

        // Copy the elements from the current array to the new array
        System.arraycopy(currentArray, 0, newArray, 0, currentArray.length);
        newArray[newArray.length - 1] =comment;
        System.out.println(newArray[0]);

        return attachmentRepository.addComment(newArray,attachmentId,user.getUserId());
    }

    @Override
    public Shared saveShared(Shared shared) {
        setUser();
        shared.setToUserId(user.getUserId());
        return sharedRepository.save(shared);
    }

    @Override
    public List<Attachment> getAttachment() {
        setUser();
        System.out.println("Inside getAttachment()"+user);
        List<Attachment> attachments=  (List<Attachment>) attachmentRepository.getByUserId(user.getUserId());
        System.out.println(attachments+" "+user.getUserId());
        return attachments;
    }

    @Override
    public List<Shared> getShared() {
        setUser();
        return (List<Shared>) sharedRepository.getByUserId(user.getUserId());
    }

}
