package craft.app.api;

import craft.app.db.ProjectRepository;
import craft.app.models.Project;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
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
    public Project addListing(@RequestBody Project newProject){
        this.projectRepository.save(newProject);
        //changed this to just return the new project, so that id can be extracted
        return newProject;
    }

    @RequestMapping(value = "/delete/{listingId}", method =  RequestMethod.DELETE)
    public List<Project> deleteListing(@PathVariable Long listingId){
        projectRepository.deleteById(listingId);
        return projectRepository.findAll();
    }
    
    @PostMapping(value = "/{listingId}/addImage")
    //todo use a multipart form here???
    public Project addImage(@PathVariable long listingId,
                            @RequestParam("file") MultipartFile imageToAdd){
        //this needs an .orelse for if it can't find that project id
        //todo proper error handling?
        //https://stackoverflow.com/questions/49316751/spring-data-jpa-findone-change-to-optional-how-to-use-this
        Project currentProject = this.projectRepository.findById(listingId).orElse(null);
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
