package com.google.gwt.libraryfinder.shared;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Library {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;
	@Persistent
	private String name;
	@Persistent
	private String city;
	@Persistent
	private String address;
	@Persistent
	private String postalCode;
	@Persistent
	private String phone;
	@Persistent
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
