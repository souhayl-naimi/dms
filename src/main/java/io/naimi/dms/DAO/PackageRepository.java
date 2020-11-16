package io.naimi.dms.DAO;

import io.naimi.dms.Entities.Package;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface PackageRepository extends JpaRepository<Package,Long> {
    public Page<Package> findByVendor_IdAndNotDeletableAndReferenceContains(Long id,Boolean deletable,String ref,Pageable pageable);
    public Page<Package> findByVendor_IdAndNotDeletableAndReferenceContainsAndCity_Id(Long id,Boolean deletable,String ref,Long cityID,Pageable pageable);
    public Page<Package> findByVendor_IdAndReferenceContains(Long id,String ref,Pageable pageable);
    public Page<Package> findByVendor_IdAndReferenceContainsAndCity_Id(Long id,String ref,Long cityID,Pageable pageable);
    public Page<Package> findByReferenceContains(String ref,Pageable pageable);
    public Page<Package> findByDeliveryMan_Id(Long id, Pageable pageable);
}
