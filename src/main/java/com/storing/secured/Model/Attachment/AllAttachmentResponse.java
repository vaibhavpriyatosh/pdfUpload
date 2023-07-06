package com.storing.secured.Model.Attachment;

import com.storing.secured.Entity.Attachment;
import com.storing.secured.Entity.Shared;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AllAttachmentResponse {

    private List<AttachmentResponse> myList;

    private List<AttachmentResponse> sharedList;

    public void setMyList(List<AttachmentResponse> myList) {
        this.myList = myList;
    }

    public void setSharedList(List<AttachmentResponse> sharedList) {
        this.sharedList = sharedList;
    }
}
