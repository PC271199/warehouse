package com.example.Warehouse.dtos;

public class SearchDto {
	private String circleDraws;
	private String polygonDraws;
	private String points;
	public SearchDto() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getCircleDraws() {
		return circleDraws;
	}
	public void setCircleDraws(String circleDraws) {
		this.circleDraws = circleDraws;
	}
	public String getPolygonDraws() {
		return polygonDraws;
	}
	public void setPolygonDraws(String polygonDraws) {
		this.polygonDraws = polygonDraws;
	}
	public String getPoints() {
		return points;
	}
	public void setPoints(String points) {
		this.points = points;
	}
	
}
