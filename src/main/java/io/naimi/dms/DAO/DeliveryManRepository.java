package io.naimi.dms.DAO;

import io.naimi.dms.Entities.DeliveryMan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface DeliveryManRepository extends JpaRepository<DeliveryMan,Long> {
}
