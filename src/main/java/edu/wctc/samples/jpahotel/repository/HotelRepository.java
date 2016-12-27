package edu.wctc.samples.jpahotel.repository;

import edu.wctc.samples.jpahotel.entity.Hotel;
import java.io.Serializable;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Spring-data-jpa will automatically create a proxy object from this
 * interface contract that contains all basic C.R.U.D. functions
 * 
 * @author jlombardo
 */
public interface HotelRepository extends JpaRepository<Hotel, Integer>, Serializable {
    
    // basic C.R.U.D. queries auto generated and not visible here
    
    // these custom queries added by developer
    
    @Query("select h from Hotel h where h.name LIKE :searchKey OR h.city LIKE :searchKey OR h.zip LIKE :searchKey")
    List<Hotel> searchForHotelByAny(@Param("searchKey") String searchKey);
}
