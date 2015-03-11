package edu.wctc.samples.jpahotel.service;

import edu.wctc.samples.jpahotel.entity.Hotel;
import edu.wctc.samples.jpahotel.entity.Hotel_;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author jlombardo
 */
@Stateless
public class HotelFacade extends AbstractFacade<Hotel> {
    @PersistenceContext(unitName = "hotel_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public HotelFacade() {
        super(Hotel.class);
    }
    
    /**
     * Finds hotels by a search key, which is checked against one of these 
     * fields: name, city, zip
     * @param searchKey - a value or portion of a value of a field for name, 
     * city or zip
     * @return matching hotel records
     */
    public List<Hotel> searchForHotelByAny(String searchKey) {
        // put wildcard symbols on both sides of searchKey
        searchKey = new StringBuilder("%").append(searchKey).append("%").toString();
        // Use Criteria style queries so we can change the search field easily
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Hotel> criteriaQuery = builder.createQuery(Hotel.class);
        Root<Hotel> hotel = criteriaQuery.from(Hotel.class);
        
        // Begin by trying the name field
        criteriaQuery.where(builder.like(hotel.get(Hotel_.name),searchKey));
        TypedQuery<Hotel> q = getEntityManager().createQuery(criteriaQuery);
        List<Hotel> hotels = q.getResultList();
        
        if(hotels.isEmpty()) {
            // Not found so try another field
            criteriaQuery.where(builder.like(hotel.get(Hotel_.city),searchKey));
            q = getEntityManager().createQuery(criteriaQuery);
            hotels = q.getResultList();
            
            if(hotels.isEmpty()) {
                // try another field
                criteriaQuery.where(builder.like(hotel.get(Hotel_.zip),searchKey));
                q = getEntityManager().createQuery(criteriaQuery);
                hotels = q.getResultList();
            }
        }
        
        return hotels;
    }
    
}
