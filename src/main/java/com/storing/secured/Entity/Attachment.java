package com.storing.secured.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Data
@NoArgsConstructor
public class Attachment {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name="uuid" ,strategy = "uuid2" )
    private String id;

    private String fileName;
    private String fileType;
    @Lob
    @Column(name = "data",length = 100000)
    private byte[] data;

    private String userId;

//    @Column(column="comment",columnDefinition = "varchar[]")

    private String[] comments;

    public Attachment(String fileName,String fileType,byte[] data) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.data = data;
    }


}
