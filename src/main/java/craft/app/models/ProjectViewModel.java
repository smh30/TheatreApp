package craft.app.models;

import javax.persistence.Lob;
import java.util.Date;

public class ProjectViewModel {
    private long projectID;
    
    private String title;
    private String description;
    private String type;
    private Date dateListed;
    private String location;
    
    private byte[] projectImage;
    
    private UserViewModel creator;
    
    public ProjectViewModel(Project project){
        this.projectID = project.getProjectID();
        this.title = project.getTitle();
        this.description = project.getDescription();
        this.type = project.getType();
        this.location = project.getLocation();
        this.dateListed = project.getDateListed();
        this.projectImage = project.getProjectImage();
        this.creator = new UserViewModel(project.getCreator());
    }
    
    public long getProjectID() {
        return projectID;
    }
    
    public String getTitle() {
        return title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public String getType() {
        return type;
    }
    
    public Date getDateListed() {
        return dateListed;
    }
    
    public byte[] getProjectImage() {
        return projectImage;
    }
    
    public UserViewModel getCreator() {
        return creator;
    }
    
    public String getLocation() {
        return location;
    }
}
