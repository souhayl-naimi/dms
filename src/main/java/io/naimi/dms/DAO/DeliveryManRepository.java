package io.naimi.dms.DAO;

import io.naimi.dms.Entities.DeliveryMan;
import io.naimi.dms.Entities.Vendor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface DeliveryManRepository extends JpaRepository<DeliveryMan,Long> {
//    public Page<DeliveryMan> findByUsernameContains(String username, Pageable pageable);
    public Page<DeliveryMan> findByCity_Id(Long id, Pageable pageable);
    public List<DeliveryMan> findByCity_Id(Long id);
}
