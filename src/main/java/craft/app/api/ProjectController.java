package craft.app.api;

import craft.app.db.ProjectRepository;
import craft.app.models.Project;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping(value = "/listings")
@CrossOrigin
public class ProjectController {
    private ProjectRepository projectRepository;
    
    
    public ProjectController(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;

    }
    
    @GetMapping("/all")
    public List<Project> all(){
        return this.projectRepository.findAll();
    }
   
    
    @RequestMapping(value = "/type/{type}", method = RequestMethod.GET)
    public List<Project> getByType (@PathVariable String type){
        return this.projectRepository.findByType(type);

    }

    //todo get this working; location is a property of the client not the project
//    @RequestMapping(value = "/location/{location}", method = RequestMethod.GET)
//    public List<Project> getByLocation (@PathVariable String location){
//        return listingRepository.findByLocation(location);
//    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public List<Project> addListing(@RequestBody Project newProject){
        projectRepository.save(newProject);
        return projectRepository.findAll();
    }

    @RequestMapping(value = "/delete/{listingId}", method =  RequestMethod.DELETE)
    public List<Project> deleteListing(@PathVariable UUID listingId){
        projectRepository.deleteById(listingId);
        return projectRepository.findAll();
    }
}
