package com.storing.secured.Repositories;

import com.storing.secured.Entity.Attachment;
import com.storing.secured.Entity.Shared;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SharedRepository extends JpaRepository<Shared,String> {

    @Query(value="SELECT * FROM shared where to_user_id=?1",nativeQuery = true)
    public List<Shared> getByUserId(String usrId);

}
