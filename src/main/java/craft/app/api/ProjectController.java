package craft.app.api;

import craft.app.db.ProjectRepository;
import craft.app.db.UserRepository;
import craft.app.models.Project;
import craft.app.models.ProjectViewModel;
import craft.app.models.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Controls the API endpoints related to Projects.
 *
 * Has a general url format of /projects
 */
@RestController
@RequestMapping(value = "/projects")
@CrossOrigin
public class ProjectController {
    
    /**
     * An instance of the ProjectRepository to allow access to the database
     */
    private ProjectRepository projectRepository;
    /**
     * An instance of the UserRepository to allow access to the database
     */
    private UserRepository userRepository;
    
    /**
     * Constructor, takes the project and user repositories as parameters so they will be
     * available to the class
     *
     * @param projectRepository
     * @param userRepository
     */
    public ProjectController(ProjectRepository projectRepository, UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }
    
    /**
     * Fetches from the repository a list of all the projects, converts them to
     * ProjectViewModels, and returns them.
     *
     * Available with a get request to /projects
     *
     * @return A list of ProjectViewModel items for all the projects in the repository
     */
    @GetMapping
    public List<ProjectViewModel> getAllProjects(){
        List<Project> projects = this.projectRepository.findAll();
        List<ProjectViewModel> pvm = new ArrayList<>();
        for (Project project : projects) {
            pvm.add(new ProjectViewModel(project));
        }
        return pvm;
    }
    
    
    /**
     * Fetches from the repository a single project by the Id which is passed in the URL,
     * converts it to a ProjectViewModel and returns it
     *
     * Available with a get request to /projects/{projectId}
     *
     * @param projectId The Id of the project to be returned
     * @return A ProjectViewModel for the requested project id
     */
    @GetMapping(value = "/{projectId}")
    public ProjectViewModel getProjectById(@PathVariable Long projectId){
        Project project = projectRepository.getOne(projectId);
        return new ProjectViewModel(project);
    }
    
    
    /**
     * Adds the new project received in the http request to the database; returns a
     * ProjectViewModel representing that project so that frontend will have access to generated
     * values such as ProjectId
     *
     * Available with a Post request to /projects
     * @param newProject A Project object received via http
     * @return A ProjectViewModel representing the new project
     */
    @PostMapping
    public ProjectViewModel addProject(@RequestBody Project newProject){
        
        User projectCreator = getProjectCreatorById(newProject.getProjectCreatorId());
        newProject.setCreator(projectCreator);
        
        this.projectRepository.save(newProject);
        return new ProjectViewModel(newProject);
    }
    
    /**
     * Returns the User with a given userId
     * @param id The id of the user to return
     * @return The requested User
     */
    private User getProjectCreatorById(Long id){
        
        //The findAllById method is defined in the JpaRepository class and takes a list, so a
        // list with one item is created and passed in
        return userRepository.findAllById(Arrays.asList(id)).get(0);
    }
    
    
    /**
     * Saves changes to a given project by updating the project in the repository. Identifies the
     * project by Id and saves the changes
     *
     * Available with a Put request to /projects
     * @param editedProject A Project object representing the desired changes
     * @return A ProjectViewModel representing the altered project
     */
    @PutMapping
    public ProjectViewModel editListing(@RequestBody Project editedProject){
        
        this.projectRepository.save(editedProject);
        
        return new ProjectViewModel(editedProject);
    }
    
    
    /**
     * If the logged in user created the project, deletes the project corresponding to the Id passed
     * in the URL, returns a list of ProjectViewModels for the remaining projects
     *
     * Available with a Delete request to /projects/{projectId}
     *
     * @param projectId The Id of the project to be deleted
     * @return An updated list of all the projects
     */
    @DeleteMapping(value = "/{projectId}")
    public List<ProjectViewModel> deleteListing(@PathVariable Long projectId){
        
        //get the currently logged in user, as passed in the JWT which came with the request
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedin = authentication.getName();
        //get the name of the creator of the project to be deleted
        String projectcreator = projectRepository.getOne(projectId).getCreator().getUsername();
        
        if (projectcreator.equals(loggedin)) {
            projectRepository.deleteById(projectId);
        } else {
            //todo throw some sort of error
        }
        return getAllProjects();
    }
    
    /**
     * Adds the submitted image to the project with the submitted Id, returns a ProjectViewModel
     * representing that project
     * @param projectId The Id of the project to which the image should be added
     * @param imageToAdd The image to add, as a MultipartFile
     * @return A ProjectViewModel representing the project after the image was added
     */
    @PostMapping(value = "/{projectId}/addImage")
    public ProjectViewModel addImage(@PathVariable long projectId,
                            @RequestParam("file") MultipartFile imageToAdd){
        //this needs an .orelse for if it can't find that project id
        //todo proper error handling?
        //https://stackoverflow.com/questions/49316751/spring-data-jpa-findone-change-to-optional-how-to-use-this
        
        Project currentProject = this.projectRepository.findById(projectId).orElse(null);
        if(currentProject != null) {
            try {
                currentProject.setProjectImage(imageToAdd.getBytes());
            } catch (IOException e){
                System.out.println("an io exception while writing image");
                e.printStackTrace();
            }
            projectRepository.save(currentProject);
        }
        return new ProjectViewModel(currentProject);
    }
}
