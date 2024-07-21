package example.ajax.pizza;

public class Customer_old {
	private String name;
	private String street;
	private String city;
	private String state;
	private String zipCode;
	
	public Customer_old() {		// default constructor 
	}
	
	public Customer_old(String name, String street, String city, String state, String zipCode) {
		this.name = name;
		this.street = street;
		this.city = city;
		this.state = state;
		this.zipCode = zipCode;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	public String getAddress() {
		return street + "\n" + city + ", " + state + " " + zipCode;
	}
}
