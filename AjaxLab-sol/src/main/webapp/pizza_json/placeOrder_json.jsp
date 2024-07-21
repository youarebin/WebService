<%@ page contentType="text/plain; charset=utf-8" %>
<%@ page import="java.util.*,example.ajax.pizza.*"%> 
<%@ page import="org.json.simple.parser.JSONParser"%> 
<%@ page import="org.json.simple.JSONObject"%> 
<%@ page import="com.fasterxml.jackson.databind.ObjectMapper"%>  
<%! @SuppressWarnings("unchecked") %>
<%
/*
// JSON-simple 이용 시
JSONParser parser = new JSONParser();
JSONObject obj = null;
try { 
	obj = (JSONObject)parser.parse(request.getReader());
} catch (Exception e) { e.printStackTrace(); }

String phone = (String)obj.get("phone");
String address = (String)obj.get("address");
String order = (String)obj.get("order");
System.out.println("phone: " + phone + "\n" + "address: " + address + "\n"
        + "order: " + order);
*/

// jackson2 이용 시
ObjectMapper objectMapper = new ObjectMapper();
Customer cust = objectMapper.readValue(request.getReader(), Customer.class);
String phone = cust.getPhone();
String address = cust.getAddress().getStreet();
String order = cust.getRecentOrder();

System.out.println("customer: " + cust);

if (order.length() <= 0) {
	response.setStatus(400);		// bad request
  	response.addHeader("Status", "No order was received");
  	out.print(" ");
}
else if (address.length() <= 0) {
	response.setStatus(400);		// bad request
  	response.addHeader("Status", "No address was received");
	out.print(" ");
} 
else { 
	//session에서 "custMap" 객체 검색
	Map<String, Customer> custMap = (Map<String, Customer>)application.getAttribute("custMap"); 
	if (custMap == null || custMap.get(phone) == null) {	 
		response.setStatus(400);		// bad request
		response.addHeader("Status", "Unregistered customer");
		return;
	} 
	Customer c = custMap.get(phone);
	System.out.println(c.getName());	 
	c.setRecentOrder(order);			// update recent order

	// make pizza and compute delivery time ...
	int deliveryTime = (int)(Math.random()*8 + 2);
	System.out.println("delivery time: " + deliveryTime);
	out.print(deliveryTime);
}
%>