package craft.app.api;

import craft.app.db.ProjectRepository;
import craft.app.db.UserRepository;
import craft.app.models.Project;
import craft.app.models.ProjectViewModel;
import craft.app.models.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@RestController
@RequestMapping(value = "/projects")
@CrossOrigin
public class ProjectController {
    private ProjectRepository projectRepository;
    private UserRepository userRepository;
    
    
    
    public ProjectController(ProjectRepository projectRepository, UserRepository userRepository) {
        this.projectRepository = projectRepository;
    
    
        this.userRepository = userRepository;
    }
    
    @GetMapping
    public List<ProjectViewModel> all(){
        List<Project> projects = this.projectRepository.findAll();
        List<ProjectViewModel> pvm = new ArrayList<>();
        for (Project project : projects) {
            pvm.add(new ProjectViewModel(project));
        }
        return pvm;
    }
   
    //todo check if this is required: i think this is solved in frontend filter
//    @RequestMapping(value = "/type/{type}", method = RequestMethod.GET)
//    public List<Project> getByType (@PathVariable String type){
//        return this.projectRepository.findByType(type);
//
//    }

    //todo get this working; location is a property of the client not the project
//    @RequestMapping(value = "/location/{location}", method = RequestMethod.GET)
//    public List<Project> getByLocation (@PathVariable String location){
//        return listingRepository.findByLocation(location);
//    }

    
    
    
    @PostMapping
    public Project addListing(@RequestBody Project newProject){
        List<User> temp = userRepository.findAllById(Arrays.asList(newProject.getNewCreatorId()));
        
        
        User projectCreator = temp.get(0);
        
        newProject.setCreator(projectCreator);
        
        this.projectRepository.save(newProject);
        //changed this to just return the new project, so that id can be extracted
        return newProject;
    }

    
    
    
    @DeleteMapping(value = "/{projectId}")
    public List<Project> deleteListing(@PathVariable Long projectId){
        projectRepository.deleteById(projectId);
        return projectRepository.findAll();
    }
    
    @PostMapping(value = "/{projectId}/addImage")
    //todo use a multipart form here???
    public Project addImage(@PathVariable long projectId,
                            @RequestParam("file") MultipartFile imageToAdd){
        //this needs an .orelse for if it can't find that project id
        //todo proper error handling?
        //https://stackoverflow.com/questions/49316751/spring-data-jpa-findone-change-to-optional-how-to-use-this
        Project currentProject = this.projectRepository.findById(projectId).orElse(null);
        if(currentProject != null) {
//            imageToAdd.getContentType();
//
//            BufferedImage bImage = ImageIO.read(imageToAdd.getBytes());
            
            try {
                currentProject.setProjectImage(imageToAdd.getBytes());
            } catch (IOException e){
                System.out.println("an io exception while writing image");
                e.printStackTrace();
            }
            projectRepository.save(currentProject);
        }
        return currentProject;
    }
}
