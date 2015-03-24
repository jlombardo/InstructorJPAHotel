package edu.wctc.samples.jpahotel.service;

import edu.wctc.samples.jpahotel.entity.Hotel;
import edu.wctc.samples.jpahotel.repository.HotelRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * @author jlombardo
 */
@Repository("hotelService")
public class HotelFacade {
    
    @Autowired
    private HotelRepository hotelRepo;
    

    public HotelFacade() {
    }
    
    public List<Hotel> findAll() {
        return hotelRepo.findAll();
    }
    
    public Hotel edit(Hotel hotel) {
        return hotelRepo.save(hotel);
    }
    
    public Hotel find(Integer id) {
        return hotelRepo.findOne(id);
    }
    
    public void deleteById(Integer id) {
        hotelRepo.deleteById(id);
    }
    
    /**
     * Finds hotels by a search key, which is checked against one of these 
     * fields: name, city, zip
     * @param searchKey - a value or portion of a value of a field for name, 
     * city or zip
     * @return matching hotel records
     */
    public List<Hotel> searchForHotelByAny(String searchKey) {
        return hotelRepo.searchForHotelByAny("%" + searchKey + "%");
    }
    
}
