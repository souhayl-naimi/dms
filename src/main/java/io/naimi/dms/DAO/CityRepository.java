package io.naimi.dms.DAO;

import io.naimi.dms.Entities.City;
import io.naimi.dms.Entities.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface CityRepository extends JpaRepository<City,Long> {
    public Page<City> findByNameContainsIgnoreCase(String nom, Pageable pageable);
}
