package edu.wctc.samples.jpahotel.controller;

import edu.wctc.samples.jpahotel.entity.Hotel;
import edu.wctc.samples.jpahotel.service.HotelFacade;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.List;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonValue;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

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
    private static final String DELETE_ACTION = "delete";
    private static final String SEARCH_ACTION = "search";
    private static final String HOME_PAGE = "/index.jsp";

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
        
        // Need to get your Spring Beans this way in a controller...
        ServletContext sctx = getServletContext();
        WebApplicationContext ctx
                = WebApplicationContextUtils.getWebApplicationContext(sctx);
        HotelFacade hotelService = (HotelFacade) ctx.getBean("hotelService");


        // Attempt to get QueryString parameters, may not always be available
        String action = request.getParameter(ACTION_PARAM);
        String hotelId = request.getParameter(ID_PARAM);

        try {
            switch (action) {
                case LIST_ACTION:
                    refreshHotelList(request, response, hotelService);
                    break;

                case FIND_ONE_ACTION: {
                    Hotel hotel = hotelService.find(Integer.valueOf(hotelId));
                    JsonObjectBuilder builder = Json.createObjectBuilder()
                            .add("hotelId", hotel.getHotelId())
                            .add("name", hotel.getName())
                            .add("address", hotel.getAddress())
                            .add("city", hotel.getCity())
                            .add("zip", hotel.getZip());

                    JsonObject hotelJson = builder.build();

                    PrintWriter out = response.getWriter();
                    response.setContentType("application/json");
                    out.write(hotelJson.toString());
                    out.flush();
                    break;
                }

                case DELETE_ACTION: {
                    PrintWriter out = response.getWriter();
                    hotelService.deleteById(Integer.valueOf(hotelId));
                    response.setContentType("application/json; charset=UTF-8");
                    response.setStatus(200);
                    JsonObjectBuilder builder = Json.createObjectBuilder()
                            .add("success", true);
                    JsonObject statusObj = builder.build();
                    out.write(statusObj.toString());
                    out.flush();
                    break;
                }

                case UPDATE_ACTION: {
                    PrintWriter out = response.getWriter();
                    StringBuilder sb = new StringBuilder();
                    BufferedReader br = request.getReader();
                    try {
                        String line;
                        while ((line = br.readLine()) != null) {
                            sb.append(line).append('\n');
                        }
                    } finally {
                        br.close();
                    }

                    String payload = sb.toString();
                    JsonReader reader = Json.createReader(new StringReader(payload));
                    JsonObject hotelJson = reader.readObject();

                    // create new entity and populate
                    Hotel hotel = new Hotel();
                    int idVal = hotelJson.getInt("hotelId",0);
                    hotel.setHotelId(idVal);
                    hotel.setAddress(hotelJson.getString("address"));
                    hotel.setCity(hotelJson.getString("city"));
                    hotel.setName(hotelJson.getString("name"));
                    hotel.setZip(hotelJson.getString("zip"));

                    hotelService.edit(hotel);

                    response.setContentType("application/json; charset=UTF-8");
                    response.setStatus(200);
                    JsonObjectBuilder builder = Json.createObjectBuilder()
                            .add("success", true);
                    JsonObject statusObj = builder.build();
                    out.write(statusObj.toString());
                    out.flush();
                    break;
                }

                case SEARCH_ACTION:
                    JsonObjectBuilder builder = null;
                    JsonObject hotelJson = null;
                    String searchKey = request.getParameter("searchKey");
                    List<Hotel> hotels = hotelService.searchForHotelByAny(searchKey);
                    // Only return first match or nothing if none found
                    if(!hotels.isEmpty()) {
                        Hotel hotel = hotels.get(0);
                        builder = Json.createObjectBuilder()
                            .add("hotelId", hotel.getHotelId())
                            .add("name", hotel.getName())
                            .add("address", hotel.getAddress())
                            .add("city", hotel.getCity())
                            .add("zip", hotel.getZip());
                        hotelJson = builder.build();
                    }
                    
                    PrintWriter out = response.getWriter();
                    response.setContentType("application/json");
                    if(builder == null) {
                        out.write("{}");
                    } else {
                        out.write(hotelJson.toString());
                    }
                    out.flush();
                    break;
            }

        } catch (IOException | NumberFormatException e) {
            // Error messages will appear on the destination page if present
            request.setAttribute("errMessage", e.getMessage());

            // Just in case it's some other exception not predicted
        } catch (Exception e2) {
            // Error messages will appear on the destination page if present
            request.setAttribute("errMessage", e2.getMessage());
        }

    }

    /*
     This is very ineffeficient having to get the entire hotel list on every
     request. In the future we'll learn how to use Ajax techniques to only
     update portions of the page that need updating. Then this refresh 
     operation won't be necessary.
     */
    private void refreshHotelList(HttpServletRequest request, HttpServletResponse response, HotelFacade hotelService)
            throws ServletException, IOException {

        List<Hotel> hotels = hotelService.findAll();
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        
        // Java 5 Style for loop (FASTEST!! x4)
        for(Hotel hotel : hotels) {
            jsonArrayBuilder.add(
                    Json.createObjectBuilder()
                    .add("hotelId", hotel.getHotelId())
                    .add("name", hotel.getName())
                    .add("address", hotel.getAddress())
                    .add("city", hotel.getCity())
                    .add("zip", hotel.getZip())
            );
        }

        // Java 8 Style loop on Iterable object (SLIGHTLY FASTER THAN STREAM)
//        hotels.forEach((hotel) -> {
//            jsonArrayBuilder.add(
//                    Json.createObjectBuilder()
//                    .add("hotelId", hotel.getHotelId())
//                    .add("name", hotel.getName())
//                    .add("address", hotel.getAddress())
//                    .add("city", hotel.getCity())
//                    .add("zip", hotel.getZip())
//            );
//        });
        
        // Java 8 Style loop on Sream object (SLOWEST!! no guarantee of order!)
//        hotels.stream()
//            .forEach((hotel) -> {
//                jsonArrayBuilder.add(
//                        Json.createObjectBuilder()
//                        .add("hotelId", hotel.getHotelId())
//                        .add("name", hotel.getName())
//                        .add("address", hotel.getAddress())
//                        .add("city", hotel.getCity())
//                        .add("zip", hotel.getZip())
//                );
//        });

        JsonArray hotelsJson = jsonArrayBuilder.build();
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.write(hotelsJson.toString());
        out.flush();
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
