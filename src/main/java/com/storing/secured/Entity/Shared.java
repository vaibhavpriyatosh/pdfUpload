package com.storing.secured.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Data
@NoArgsConstructor
public class Shared {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name="uuid" ,strategy = "uuid2" )
    private String id;

    private String attachmentId;

    private String toUserId;

}
