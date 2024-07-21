<%@ page contentType="application/json; charset=utf-8" %>
<%@ page import="example.ajax.pizza.*,java.util.*"%>
<%@ page import="org.json.simple.JSONObject"%> 
<%@ page import="com.fasterxml.jackson.databind.ObjectMapper"%>  
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
Customer c = custMap.get(phone);	// "custMap"에서 검색

if (c != null) {	// a customer found
/*
    // Json-simple 사용 시
	JSONObject address = new JSONObject();
	address.put("street", c.getAddress().getStreet());
	address.put("city", c.getAddress().getCity());
	address.put("state", c.getAddress().getState());
	address.put("zipCode", c.getAddress().getZipCode());

	JSONObject person = new JSONObject();
	person.put("name", c.getName());
	person.put("address", address);
	person.put("recentOrder", c.getRecentOrder());
	
	System.out.println(person);		// 변환 결과 확인	
	out.clearBuffer();
	out.println(person);			// 응답 메시지에 출력
*/
	// Jackson2를 이용해서 JS object-to-JSON text 변환 처리
	ObjectMapper mapper = new ObjectMapper();
	String result = mapper.writeValueAsString(c);
	System.out.println(result);		// 변환 결과 확인
	out.clearBuffer();
	out.println(result);			// 응답 메시지에 출력
}
else {				// unregistered customer
	response.setStatus(400);		// bad request
	response.addHeader("Status", "Unregistered customer");
	return;
}
%>
