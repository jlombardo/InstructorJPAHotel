/*
 * Custom JavaScript for this app using Vue.js
 */

(function ($, window, document) {
    $(function () {

        var $hotelName = $('#name');

        var app = new Vue({
            el: '#app',
            data: {
                baseUrl: 'HotelController',
                hotels: [],
                hotelId: '',
                hotelName: '',
                hotelAddress: '',
                hotelCity: '',
                hotelZip: '',
                searchKey: '',
                btnDeleteShow: 'hidden',
                htmlEscapeCodeMap: {
                    "&": "&amp;",
                    "<": "&lt;",
                    ">": "&gt;",
                    '"': '&quot;',
                    "'": '&#39;',
                    "/": '&#x2F;'
                }
            },
            methods: {
                escapeHtml: function(string) {
                    return String(string).replace(/[&<>"'\/]/g, function (s) {
                        return app.htmlEscapeCodeMap[s];
                    });
                },
                renderForm: function(hotel) {
                    app.hotelId = hotel.hotelId;
                    app.hotelName = hotel.name;
                    app.hotelAddress = hotel.address;
                    app.hotelCity = hotel.city;
                    app.hotelZip = hotel.zip;
                    app.btnDeleteShow = 'visible';
                 },
                searchHotels: function() {
                    var searchKey = app.escapeHtml(app.searchKey.trim());
                    var url = app.baseUrl + "?action=search&searchKey=" + searchKey;
                    $.get(url).then(function (hotel) {
                            app.renderForm(hotel);
                            app.searchKey = '';
                        }, app.handleError);                    
                },
                findAll: function () {
                    $.get(app.baseUrl + "?action=list").then(function (hotels) {
                        app.hotels = hotels;
                        app.btnDeleteShow = 'hidden';
                    }, app.handleError);
                },
                findById(hotelId) {
                    $.get(app.baseUrl + '?action=findone&hotelId=' + hotelId).then(function (hotel) {
                        app.renderForm(hotel);
                    }, app.handleError);
                },
                saveOrUpdateHotel: function () {
                    $.ajax({
                        type: 'POST',
                        contentType: 'application/json',
                        url: app.baseUrl + "?action=update",
                        dataType: 'json',
                        data: this.formToJson()
                    })
                    .done(function () {
                        app.findAll();
                        app.btnDeleteShow = 'visible';
                        alert("Hotel saved or updated successfully");
                    })
                    .fail(function (jqXHR, textStatus, errorThrown) {
                        app.handleError(jqXHR, textStatus, errorThrown);
                    });
                },
                deleteHotel: function() {
                    $.ajax({
                        type: 'POST',
                        url: app.baseUrl + "?action=delete&hotelId=" + app.hotelId
                    })
                    .done(function () {
                        app.findAll();
                        app.clearForm();
                        alert("Hotel deleted successfully");
                    })
                    .fail(function ( jqXHR, textStatus, errorThrown ) {
                        app.handleError(jqXHR, textStatus, errorThrown);
                    });
                },
                clearForm: function() {
                    app.hotelId = '';
                    app.hotelName = '';
                    app.hotelAddress = '';
                    app.hotelCity = '';
                    app.hotelZip = '';
                    app.btnDeleteShow = 'hidden';
                    $hotelName.focus();
                 },
                formToJson: function () {
                    return JSON.stringify({
                        "hotelId": app.hotelId,
                        "address": app.hotelAddress,
                        "city": app.hotelCity,
                        "name": app.hotelName,
                        "zip": app.hotelZip
                    });
                },
                handleError: function (xhr, status, error) {
                    alert("Sorry, there was a problem: " + error);
                }
            }
        });

        app.findAll();


    });

}(window.jQuery, window, document));