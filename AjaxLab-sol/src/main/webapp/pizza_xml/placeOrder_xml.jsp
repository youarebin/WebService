<%@ page contentType="text/plain; charset=utf-8" %>
<%@ page import="example.ajax.pizza.*"%>
<%@ page import="java.util.*"%> 
<%! @SuppressWarnings("unchecked") %>
<%
String phone = request.getParameter("phone");
String order = request.getParameter("order");
String address = request.getParameter("address");
System.out.println("phone: " + phone);
System.out.println("order: " + order);
System.out.println("address: " + address);

if (order.length() <= 0) {
    response.setStatus(400);        // bad request
    response.addHeader("Status", "No order was received");
    out.print(" ");
}
else if (address.length() <= 0) {
    response.setStatus(400);        // bad request
    response.addHeader("Status", "No address was received");
    out.print(" ");
} 
else { 
    //session에서 "custMap" 객체 검색
    Map<String, Customer> custMap = (Map<String, Customer>)application.getAttribute("custMap"); 
    if (custMap == null || custMap.get(phone) == null) {     
        response.setStatus(400);        // bad request
        response.addHeader("Status", "Unregistered customer");
        return;
    } 
    Customer c = custMap.get(phone);
    System.out.println(c.getName());     
    c.setRecentOrder(order);            // update recent order

    // make pizza and compute delivery time ...
    int deliveryTime = (int)(Math.random()*8 + 2);
    System.out.println("delivery time: " + deliveryTime);
    out.print(deliveryTime);
}
%>