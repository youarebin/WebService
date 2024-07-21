<%@ page import="example.ajax.pizza.*" contentType="text/plain; charset=utf-8" %>
<%
Customer_old[] customers = new Customer_old[4];

// Set up some addresses to use
customers[0] = new Customer_old("Doug Henderson",
                  "7804 Jumping Hill Lane",
                  "Dallas", "Texas", "75218");
customers[1] = new Customer_old("Mary Jenkins",
                  "7081 Teakwood #24C",
                  "Dallas", "Texas", "75182");
customers[2] = new Customer_old("John Jacobs",
                  "234 East Rutherford Drive",
                  "Topeka", "Kansas", "66608");
customers[3] = new Customer_old("Happy Traum",
                  "876 Links Circle",
                  "Topeka", "Kansas", "66608");

String phone = request.getParameter("phone");
System.out.println("phone number: " + phone);   

// Pick a customer
int i = (int)(Math.random()*4);
Customer_old c = customers[i];
String result = c.getName() + "\n" + c.getAddress();

System.out.println("result: " + result);   
out.clearBuffer();
out.print(result);
%>