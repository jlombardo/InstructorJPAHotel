<%-- 
    Document   : index
    Created on : Mar 11, 2015, 10:58:08 AM
    Author     : jlombardo
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>JPA Web Demo :: Hotels</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" href="css/styles.css" /> 
    </head>
    <body>
        <h1>Hotel Manager :: AJAX / Spring-Data-JPA Demo</h1>
        <p class="errMsg">${errMessage == null ? "" : "Sorry, there was a problem: " + errMessage}</p>

        <div class="header">
            <!-- These items are handled by app.js -->
            <input type="text" id="searchKey"/>
            <button id="btnSearch">Search</button>
            <button id="btnAdd">New Hotel</button>
        </div>

        <div class="leftArea">
            <ul id="hotelList">
            </ul>
        </div>

        <div id="content">
            <form id="hotelForm">

                <div class="mainArea">

                    <label>Id:</label>
                    <input id="hotelId" name="hotelId" type="text" readonly />

                    <label>Name:</label>
                    <input type="text" id="name" name="name" required>

                    <label>Address</label>
                    <input type="text" id="address" name="address"/>

                    <label>City</label>
                    <input type="text" id="city" name="city"/>

                    <label>Zip</label>
                    <input type="text" id="zip" name="zip"/>

                    <button id="btnSave" name="Update" type="button" value="Save">Save</button>
                    <button id="btnDelete" name="Update" type="button" value="Delete">Delete</button>

                </div>

                <div class="rightArea">

                    <img id="pic" height="300"/>

                    <label>Notes:</label>
                    <textarea id="description" name="description"></textarea>
                </div>

            </form>
        </div>
        
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
        <script src="js/app.js"></script>
    </body>
</html>
