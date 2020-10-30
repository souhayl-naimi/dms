package io.naimi.dms.DAO;

import io.naimi.dms.Entities.Comment;
import io.naimi.dms.Entities.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface StatusRepository extends JpaRepository<Status,Long> {
}
