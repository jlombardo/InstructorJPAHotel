/*
 * Custom JavaScript for this app using Vue.js
 */

(function ($, window, document) {
    $(function () {
        // Private Properties

        var vmList = new Vue({
            el: '#hotelList',
            data: {
              baseUrl: 'HotelController',
              hotels: []
            }
        });
        
        // Private Methods
        
        findAll();

        function findAll() {
            $.get(vmList.baseUrl + "?action=list").then(function (hotels) {
                vmList.hotels = hotels;
            }, handleError);
        }

        function handleError(xhr, status, error) {
            alert("Sorry, there was a problem: " + error);
        }   
    
    
    });

}(window.jQuery, window, document));