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