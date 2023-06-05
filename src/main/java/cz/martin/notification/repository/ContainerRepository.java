package cz.martin.notification.repository;

import cz.martin.notification.entity.Container;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContainerRepository extends JpaRepository<Long, Container> {
}
