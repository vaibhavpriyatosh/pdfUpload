package com.storing.secured.Repositories;

import com.storing.secured.Entity.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment,String> {
    @Modifying
    @Query(value = "UPDATE attachment SET comments = ?1 WHERE id = ?2 AND user_id=?3",nativeQuery = true)
    public int addComment(String []comment,String attachmentId,String userId);

    @Query(value="SELECT * FROM attachment where user_id=?1",nativeQuery = true)
    public List<Attachment> getByUserId(String usrId);

}
