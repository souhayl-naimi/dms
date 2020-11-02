package io.naimi.dms.DAO;

import io.naimi.dms.Entities.Vendor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface VendorRepository extends JpaRepository<Vendor,Long> {
    public Vendor findByCin(String cin);
    public Page<Vendor> findByUsernameContains(String username, Pageable pageable);
}
