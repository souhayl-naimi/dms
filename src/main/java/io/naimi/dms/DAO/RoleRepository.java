package io.naimi.dms.DAO;

import io.naimi.dms.Entities.Role;
import io.naimi.dms.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface RoleRepository extends JpaRepository<Role,String> {
}
