package example.ajax.pizza;

public class Address {
	private String street;
	private String city;
	private String state;
	private String zipCode;
	
	public Address() {}		// 기본 생성자
	
	public Address(String street, String city, String state, String zipCode) {
		this.street = street;
		this.city = city;
		this.state = state;
		this.zipCode = zipCode;
	}
	public String getStreet() {
		return street;
	}
	public String getCity() {
		return city;
	}
	public String getState() {
		return state;
	}
	public String getZipCode() {
		return zipCode;
	}	
	@Override
	public String toString() {
		return street + "\n" + city + ", " + state + " " + zipCode;
	}
}
