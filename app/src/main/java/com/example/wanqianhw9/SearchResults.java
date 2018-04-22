package com.example.wanqianhw9;

/**
 * Created by anwanqi on 4/13/18.
 */


public class SearchResults {

    private String imgUri;
    private String address;
    private String name;
    private String place_id;
    private Double place_lat;
    private Double place_lng;




    public SearchResults(String url, String placeName, String placeAddress,String place_id, Double lat, Double lng){
        imgUri = url;
        name = placeName;
        address = placeAddress;
        this.place_id = place_id;
        place_lat = lat;
        place_lng = lng;
    }

    public String getPlace_id() {
        return place_id;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }

    public String getImgUri() {
        return imgUri;
    }

    public void setImgUri(String imgUri) {
        this.imgUri = imgUri;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public Double getPlace_lat() {
        return place_lat;
    }

    public void setPlace_lat(Double place_lat) {
        this.place_lat = place_lat;
    }

    public Double getPlace_lng() {
        return place_lng;
    }

    public void setPlace_lng(Double place_lng) {
        this.place_lng = place_lng;
    }
}
