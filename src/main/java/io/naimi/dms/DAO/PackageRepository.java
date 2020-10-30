package io.naimi.dms.DAO;

import io.naimi.dms.Entities.Package;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface PackageRepository extends JpaRepository<Package,Long> {

    public Page<Package> findByVendor_Cin(String cin, Pageable pageable);
}
