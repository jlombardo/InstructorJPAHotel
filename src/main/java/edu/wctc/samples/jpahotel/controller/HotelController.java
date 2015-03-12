package edu.wctc.samples.jpahotel.controller;

import edu.wctc.samples.jpahotel.entity.Hotel;
import edu.wctc.samples.jpahotel.service.HotelFacade;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * The main controller in this app
 *
 * @author jlombardo
 */
public class HotelController extends HttpServlet {

    private static final String ACTION_PARAM = "action";
    private static final String ID_PARAM = "hotelId";
    private static final String LIST_ACTION = "list";
    private static final String FIND_ONE_ACTION = "findone";
    private static final String UPDATE_ACTION = "update";
    private static final String SEARCH_ACTION = "search";
    private static final String HOME_PAGE = "/index.jsp";

    // Note that we're using CDI here (see Web Pages/WEB-INF/beans.xml)
    // so we can use this annotatio instead of EJB
    @Inject
    private HotelFacade hotelService;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        // set default destination
        String destination = HOME_PAGE;

        // Attempt to get QueryString parameters, may not always be available
        String action = request.getParameter(ACTION_PARAM);
        String hotelId = request.getParameter(ID_PARAM);

        try {
            switch (action) {
                case LIST_ACTION:
                    refreshHotelList(request, response);
                    break;
                case FIND_ONE_ACTION: {
                    Hotel hotel = hotelService.find(Integer.valueOf(hotelId));
                    request.setAttribute("foundHotel", hotel);
                    refreshHotelList(request, response);
                    break;
                }
                case UPDATE_ACTION: {
                    // create new entity and populate
                    Hotel hotel = new Hotel();
                    Integer id = (hotelId == null || hotelId.isEmpty()) ? null : Integer.valueOf(hotelId);
                    hotel.setHotelId(id);
                    hotel.setAddress(request.getParameter("address"));
                    hotel.setCity(request.getParameter("city"));
                    hotel.setName(request.getParameter("name"));
                    hotel.setZip(request.getParameter("zip"));
                        // Next, check if we're saving or eleting
                    // Note that we're using Save for edits and new records.
                    // This is possible because the AbstractFacade uses merge for
                    // saving updates, which also works for new records.
                    String updateAction = request.getParameter("Update");
                    switch (updateAction) {
                        case "Save":
                            hotelService.edit(hotel);
                            request.setAttribute("foundHotel", hotel);
                            refreshHotelList(request, response);
                            break;
                        case "Delete":
                            hotelService.remove(hotel);
                            refreshHotelList(request, response);
                            break;
                    }
                    break;
                }
                case SEARCH_ACTION:
                    String searchKey = request.getParameter("searchKey");
                    List<Hotel> hotels = hotelService.searchForHotelByAny(searchKey);
                    // Only return first match or nothing if none found
                    request.setAttribute("foundHotel", hotels.isEmpty() ? null : hotels.get(0));
                    refreshHotelList(request, response);
                    break;
            }

        } catch (ServletException | IOException | NumberFormatException e) {
            // Error messages will appear on the destination page if present
            request.setAttribute("errMessage", e.getMessage());

            // Just in case it's some other exception not predicted
        } catch (Exception e2) {
            // Error messages will appear on the destination page if present
            request.setAttribute("errMessage", e2.getMessage());
        }

        RequestDispatcher dispatcher
                = getServletContext().getRequestDispatcher(destination);
        dispatcher.forward(request, response);

    }

    /*
     This is very ineffeficient having to get the entire hotel list on every
     request. In the future we'll learn how to use Ajax techniques to only
     update portions of the page that need updating. Then this refresh 
     operation won't be necessary.
     */
    private void refreshHotelList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Hotel> hotels = hotelService.findAll();

        PrintWriter out = response.getWriter();
        response.setContentType("application/json");

//        JsonObjectBuilder jsonObjBuilder = Json.createObjectBuilder()
//                .add("promoProdId", promoProd.getPromoProdId())
//                .add("promoWeekId", promoProd.getPromoWeekId())
//                .add("itemName", promoProd.getItemName())
//                .add("currRetail", promoProd.getCurrRetail().toString())
//                .add("featurePrice", promoProd.getFeaturePrice().toString());
//
//        JsonObject prodsJson = jsonObjBuilder.build();
//        out.write(prodsJson.toString());
        out.flush();
        return;
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
