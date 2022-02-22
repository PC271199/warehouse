package com.example.Warehouse.dtos;

import java.util.Date;

public class SearchDto {
	private String circleDraws;
	private String polygonDraws;
	private String points;
	private String name;
	private boolean opening;
	private boolean complete;
	private boolean notopening;
	private boolean notcomplete;
	private boolean configured;
	private boolean vr;
	private int minFloor;
	private int maxFloor;
	private int minFee;
	private int maxFee;
	private int minArea;
	private int maxArea;
	private String deliveryDate;
	
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isOpening() {
		return opening;
	}
	public void setOpening(boolean opening) {
		this.opening = opening;
	}
	public boolean isComplete() {
		return complete;
	}
	public void setComplete(boolean complete) {
		this.complete = complete;
	}
	public int getMinFloor() {
		return minFloor;
	}
	public void setMinFloor(int minFloor) {
		this.minFloor = minFloor;
	}
	public int getMaxFloor() {
		return maxFloor;
	}
	public void setMaxFloor(int maxFloor) {
		this.maxFloor = maxFloor;
	}
	public int getMinFee() {
		return minFee;
	}
	public void setMinFee(int minFee) {
		this.minFee = minFee;
	}
	public int getMaxFee() {
		return maxFee;
	}
	public void setMaxFee(int maxFee) {
		this.maxFee = maxFee;
	}
	public int getMinArea() {
		return minArea;
	}
	public void setMinArea(int minArea) {
		this.minArea = minArea;
	}
	public int getMaxArea() {
		return maxArea;
	}
	public void setMaxArea(int maxArea) {
		this.maxArea = maxArea;
	}
	public String getDeliveryDate() {
		return deliveryDate;
	}
	public void setDeliveryDate(String deliveryDate) {
		this.deliveryDate = deliveryDate;
	}
	public boolean isNotopening() {
		return notopening;
	}
	public void setNotopening(boolean notopening) {
		this.notopening = notopening;
	}
	public boolean isNotcomplete() {
		return notcomplete;
	}
	public void setNotcomplete(boolean notcomplete) {
		this.notcomplete = notcomplete;
	}
	public boolean isConfigured() {
		return configured;
	}
	public void setConfigured(boolean configured) {
		this.configured = configured;
	}
	public boolean isVr() {
		return vr;
	}
	public void setVr(boolean vr) {
		this.vr = vr;
	}
	
}
