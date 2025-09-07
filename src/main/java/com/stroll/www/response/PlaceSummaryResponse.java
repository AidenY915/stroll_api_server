package com.stroll.www.response;

import com.stroll.www.vo.PlaceVO;

public class PlaceSummaryResponse {
    private int placeNo;
    private String name;
    private double star;
    private int distance;

    public PlaceSummaryResponse() {}

    public PlaceSummaryResponse(int placeNo, String name, double star, int distance) {
        this.placeNo = placeNo;
        this.name = name;
        this.star = star;
        this.distance = distance;
    }

    // VO → Response 변환
    public static PlaceSummaryResponse from(PlaceVO vo) {
        return new PlaceSummaryResponse(
                vo.getNo(),
                vo.getTitle(),
                vo.getStar(),
                vo.getDistance()
        );
    }

    public int getPlaceNo() { return placeNo; }
    public String getName() { return name; }
    public double getStar() { return star; }
    public int getDistance() { return distance; }
}
