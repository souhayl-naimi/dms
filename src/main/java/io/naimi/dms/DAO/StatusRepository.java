package io.naimi.dms.DAO;

import io.naimi.dms.Entities.Comment;
import io.naimi.dms.Entities.Package;
import io.naimi.dms.Entities.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface StatusRepository extends JpaRepository<Status,Long> {
    public Page<Status> findByStatusContainsAndApackage_Vendor_Id(String status, Long id, Pageable pageable);
}
