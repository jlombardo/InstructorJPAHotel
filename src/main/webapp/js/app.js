/*
 * Custom JavaScript for this app
 */

(function ($, window, document) {
    $(function () {

        // declare JQuery selectors and cache results
        var $btnAdd = $('#btnAdd');
        var $btnSearch = $('#btnSearch');
        var $hotelId = $('#hotelId');
        var $hotelName = $('#name');
        var $hotelAddress = $('#address');
        var $hotelCity = $('#city');
        var $hotelZip = $('#zip');
        var $searchKey = $('#searchKey');
        var baseUrl = "HotelController";

        $btnAdd.on('click', function () {
            clearForm();
            $hotelName.focus();
            return;
        });

        function clearForm() {
            $hotelId.val("");
            $hotelName.val("");
            $hotelAddress.val("");
            $hotelCity.val("");
            $hotelZip.val("");
        }

        $.get(baseUrl + "?action=list").then(function (hotels) {
            renderList(hotels);
        }, handleError);

        function renderList(hotels) {
            $('#hotelList li').remove();
            $.each(hotels, function (index, hotel) {
                $('#hotelList').append('<li><a href="#" data-identity="' + rootURL + '/' + hotel.hotelId + '">' + hotel.name + '</a></li>');
            });
        }
        
        function handleError(xhr, status, error) {
            console.log(error);
        }

        /*
         * This is the old version which just sends a request to
         * a servlet for normal processing.
         */
        $btnSearch.on('click', function () {
            var searchKey = $searchKey.val();
            searchKey = escapeHtml(searchKey.trim());
            var url = "HotelController?action=search&searchKey=" + searchKey;
            document.location.href = url;
            return;
        });

        var htmlEscapeCodeMap = {
            "&": "&amp;",
            "<": "&lt;",
            ">": "&gt;",
            '"': '&quot;',
            "'": '&#39;',
            "/": '&#x2F;'
        };

        function escapeHtml(string) {
            return String(string).replace(/[&<>"'\/]/g, function (s) {
                return htmlEscapeCodeMap[s];
            });
        }

    });

}(window.jQuery, window, document));