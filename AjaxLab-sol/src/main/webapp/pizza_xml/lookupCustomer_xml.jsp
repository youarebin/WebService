<%@ page contentType="text/xml; charset=utf-8" %>
<%@ page import="example.ajax.pizza.*,java.util.*"%>
<%! @SuppressWarnings("unchecked") %>
<%
Customer[] customers = new Customer[] {
    new Customer("Doug Henderson", 
        new Address("7804 Jumping Hill Lane", "Dallas", "Texas", "75218"),
        "010-111-1111", "no recent order"),
    new Customer("Mary Jenkins", 
        new Address("7081 Teakwood #24C", "Dallas", "Texas", "75182"),
        "010-222-2222", "no recent order"),
    new Customer("John Jacobs", 
        new Address("234 East Rutherford Drive","Topeka", "Kansas", "66608"),
        "010-333-3333", "no recent order"),
    new Customer("Happy Traum",
        new Address("876 Links Circle", "Topeka", "Kansas", "66608"),
        "010-444-4444", "no recent order"),
};

// application에서 "custMap" 객체 검색
Map<String, Customer> custMap = (Map<String, Customer>)application.getAttribute("custMap"); 

if (custMap == null) {  // "custMap"이 존재하지 않으면 새로 생성
    custMap = new HashMap<String, Customer>();
    
    for (Customer c : customers) {      // 위 배열의 Customer 객체들을 custMap에 저장
        custMap.put(c.getPhone(), c);   // phone을 key로 사용       
    } 
                
    application.setAttribute("custMap", custMap);   // custMap을 application에 저장
}

String phone = request.getParameter("phone");
System.out.println("phone number: " + phone);   

//find a customer having the given phone number
Customer c = custMap.get(phone);    // "custMap"에서 검색

if (c != null) {                    // unregistered customer    
/*
    String result = c.getName() + "\n" + c.getAddress();
    System.out.println("result: " + result);
    out.clearBuffer();
    out.print(result);
*/
%>
<result>
    <name><%=c.getName()%></name>
        <address>
        <street><%=c.getAddress().getStreet()%></street>
        <city><%=c.getAddress().getCity()%></city>
        <state><%=c.getAddress().getState()%></state>
        <zipCode><%=c.getAddress().getZipCode()%></zipCode>
    </address>
    <recentOrder><%=c.getRecentOrder()%></recentOrder>
</result>
<%
}
else {              // unregistered customer
    response.setStatus(400);        // bad request
    response.addHeader("Status", "Unregistered customer");
    return;
}

/*
// 참고: Jackson2를 이용한 object-to-JSON 변환
ObjectMapper mapper = new ObjectMapper();
String result2 = mapper.writeValueAsString(c);
System.out.println(result2);        // 변환 결과 확인
*/
%>