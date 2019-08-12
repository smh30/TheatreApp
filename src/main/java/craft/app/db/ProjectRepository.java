package craft.app.db;

import craft.app.models.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
//can add custom methods here if necessary
    List<Project> findByType(String type);
    
    //List<Project> findByLocation(String location);
}