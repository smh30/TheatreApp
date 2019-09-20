package craft.app.db;

import craft.app.models.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * The repository which is used to persist the Projects which have a Long as ID into the database.
 * Methods are inherited from JpaRepository (adding, deleting, findAll etc)
 *
 */
@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

}