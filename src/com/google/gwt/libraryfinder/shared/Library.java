package com.google.gwt.libraryfinder.shared;


public class Library {

	private String name;
	private String city;
	private String address;
	private String postalCode;
	private String phone;
	private LatLon latLon;
	
	public Library(String name, String city, String address, String postalCode, String phone, LatLon latLon) {
		this.name = name;
		this.city = city;
		this.address = address;
		this.postalCode = postalCode;
		this.phone = phone;
		this.latLon = latLon;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPostalCode() {
		return postalCode;
	}
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public LatLon getLatLon() {
		return latLon;
	}
	public void setLatLon(LatLon latLon) {
		this.latLon = latLon;
	}
	
}
