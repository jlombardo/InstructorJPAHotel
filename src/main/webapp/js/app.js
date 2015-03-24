/*
 * Custom JavaScript for this app
 */

(function ($, window, document) {
    $(function () {

        // declare JQuery selectors and cache results
        var $btnAdd = $('#btnAdd');
        var $btnSearch = $('#btnSearch');
        var $btnDelete = $('#btnDelete');
        var $btnSave = $('#btnSave');
        var $hotelId = $('#hotelId');
        var $hotelName = $('#name');
        var $hotelAddress = $('#address');
        var $hotelCity = $('#city');
        var $hotelZip = $('#zip');
        var $searchKey = $('#searchKey');
        var baseUrl = "HotelController";

        findAll();
        $btnDelete.hide();

        $btnAdd.on('click', function () {
            clearForm();
            $hotelName.focus();
            return;
        });

        $btnSave.click(function () {
            if ($hotelId.val() === '') {
                addHotel.then(function () {
                    alert("Hotel created successfully!");
                }, handleError);
            } else {
                updateHotel.then(function () {
                    alert("Hotel created successfully!");
                }, handleError);
            }
            return false;
        });

        $btnDelete.click(function () {
            deleteHotel.then(function () {
                alert("Hotel deleted successfully!");
            }, handleError);
            return false;
        });

        function clearForm() {
            $hotelId.val("");
            $hotelName.val("");
            $hotelAddress.val("");
            $hotelCity.val("");
            $hotelZip.val("");
        }

        function findAll() {
            $.get(baseUrl + "?action=list").then(function (hotels) {
                renderList(hotels);
            }, handleError);
        }

        function renderList(hotels) {
            $('#hotelList li').remove();
            $.each(hotels, function (index, hotel) {
                $('#hotelList').append('<li><a href="#" data-identity="'
                        + baseUrl + '?action=findone&hotelId='
                        + hotel.hotelId + '">' + hotel.name + '</a></li>');
            });
        }

        function handleError(xhr, status, error) {
            alert("Sorry, there was a problem: " + error);
        }

        $('#hotelList').on('click', "a", function () {
            findById($(this).data('identity'));
        });

        function findById(self) {
            $.get(self).then(function (hotel) {
                renderDetails(hotel);
            }, handleError);
            return false;
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
            return false;
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

        var addHotel = function () {
            $.ajax({
                type: 'POST',
                contentType: 'application/json',
                url: baseUrl + "?action=update",
                dataType: "json",
                data: formToJSON()
            });
        }


        var updateHotel = function () {
            console.log('updateHotel');
            $.ajax({
                type: 'PUT',
                contentType: 'application/json',
                url: baseUrl + "?action=update",
                dataType: "html",
                data: formToJSON()
            });
        }

        var deleteHotel = function () {
            console.log('deleteHotel');
            $.ajax({
                type: 'DELETE',
                url: baseUrl + "?action=delete&hotelId=" + $hotelId.val()
            });
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

// Helper function to serialize all the form fields into a JSON string
        function formToJSON() {
            return JSON.stringify({
                "hotelId": $hotelId.val(),
                "address": $hotelAddress.val(),
                "city": $hotelCity.val(),
                "name": $hotelName.val(),
                "zip": $hotelZip.val(),
            });
        }
    });

}(window.jQuery, window, document));