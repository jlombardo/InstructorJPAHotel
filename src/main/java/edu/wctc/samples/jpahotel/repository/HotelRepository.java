package edu.wctc.samples.jpahotel.repository;

import edu.wctc.samples.jpahotel.entity.Hotel;
import java.io.Serializable;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author jlombardo
 */
public interface HotelRepository extends JpaRepository<Hotel, Integer>, Serializable {
    
    @Query("select h from Hotel h where h.name = :searchKey OR h.city = :searchKey OR h.zip = :searchKey")
    List<Hotel> searchForHotelByAny(@Param("searchKey") String searchKey);
}
