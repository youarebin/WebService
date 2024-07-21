package example.ajax.pizza;

public class Customer {
	private String name;
	private Address address;	// Address 객체 참조
	private String phone;
	private String recentOrder;
	
	public Customer() {}		// 기본 생성자
	
	public Customer(String name, Address address, String phone, String recentOrder) {
		this.name = name;
		this.address = address;
		this.phone = phone;
		this.recentOrder = recentOrder;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getRecentOrder() {
		return recentOrder;
	}

	public void setRecentOrder(String recentOrder) {
		this.recentOrder = recentOrder;
	}
	
	@Override
	public String toString() {
		return "Customer [name=" + name + ", address=" + address + ", phone=" + phone 
				+ ", recentOrder=" + recentOrder + "]";
	}	
}
