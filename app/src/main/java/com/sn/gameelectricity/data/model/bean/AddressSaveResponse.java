package com.sn.gameelectricity.data.model.bean;

public class AddressSaveResponse {

    private String address;
    private String addresseeName;
    private int id;
    private String phoneNumber;
    private int type;

    public AddressSaveResponse() {
    }

    public AddressSaveResponse(String address, String addresseeName, int id, String phoneNumber, int type) {
        this.address = address;
        this.addresseeName = addresseeName;
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddresseeName() {
        return addresseeName;
    }

    public void setAddresseeName(String addresseeName) {
        this.addresseeName = addresseeName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
