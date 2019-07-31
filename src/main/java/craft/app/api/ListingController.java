package craft.app.api;

import craft.app.db.ListingRepository;
import org.apache.catalina.mapper.Mapper;
import org.springframework.web.bind.annotation.*;
import craft.app.models.Listing;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@RestController
@RequestMapping(value = "/listings")
@CrossOrigin
public class ListingController {
    private ListingRepository listingRepository;
    //private Mapper mapper;
    
    public ListingController(ListingRepository listingRepository) {
        this.listingRepository = listingRepository;

    }
    
    @GetMapping("/all")
    public List<Listing> all(){
        return this.listingRepository.findAll();
    }
   
    
    @RequestMapping(value = "/type/{type}", method = RequestMethod.GET)
    public List<Listing> getByType (@PathVariable String type){
        return this.listingRepository.findByType(type);

    }

    //todo get this working; location is a property of the client not the project
//    @RequestMapping(value = "/location/{location}", method = RequestMethod.GET)
//    public List<Listing> getByLocation (@PathVariable String location){
//        return listingRepository.findByLocation(location);
//    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public List<Listing> addListing(@RequestBody Listing newListing){
        listingRepository.save(newListing);
        return listingRepository.findAll();
    }

    @RequestMapping(value = "/delete/{listingId}", method =  RequestMethod.DELETE)
    public List<Listing> deleteListing(@PathVariable UUID listingId){
        listingRepository.deleteById(listingId);
        return listingRepository.findAll();
    }
}
