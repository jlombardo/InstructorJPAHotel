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

        getHotelList();

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

        function getHotelList() {
            $.get(baseUrl + "?action=list").then(function (hotels) {
                renderList(hotels);
            }, handleError);
        }

        function renderList(hotels) {
            $('#hotelList li').remove();
            $.each(hotels, function (index, hotel) {
                $('#hotelList').append('<li><a href="#" data-identity="' + baseUrl + '?action=findone&hotelId=' + hotel.hotelId + '">' + hotel.name + '</a></li>');
            });
        }

        function handleError(xhr, status, error) {
            console.log(error);
        }

        $('#hotelList').on('click', "a", function () {
            findById($(this).data('identity'));
        });

        function findById(self) {
            $.get(self).then(function (hotel) {
                renderDetails(hotel);
            }, handleError);
            return;
        }

        function renderDetails(hotel) {
            if (hotel.name === undefined) {
                $('#hotelId').val(hotel.hotelId);
            } else {
                var id = hotel.hotelId;
                $('#hotelId').val(id);
            }
            $('#name').val(hotel.name);
            $('#address').val(hotel.address);
            $('#city').val(hotel.city);
            $('#zip').val(hotel.zip);
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