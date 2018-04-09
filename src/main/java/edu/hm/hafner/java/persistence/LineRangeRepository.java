package edu.hm.hafner.java.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * JPA repository to access LineRangeEntities.
 *
 * @author Michael Schmid
 */
public interface LineRangeRepository extends JpaRepository<LineRangeEntity, String> {
}
