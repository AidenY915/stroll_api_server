package com.stroll.www.vo;

import java.io.Serializable;
import java.sql.Timestamp;

public class PlaceVO implements Serializable {
	private static final long serialVersionUID = 7766196491054550097L;

	public PlaceVO() {
	}

	private int no;
	private String title;
	private String content;
	private String category;
	private Timestamp writtenDate;
	private String guAddress;
	private String afterGuAddress;
	private String detailAddress;
	private double x;
	private double y;
	private String userId;
	private int distance;
	private float star;

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Timestamp getWrittenDate() {
		return writtenDate;
	}

	public void setWrittenDate(Timestamp writtenDate) {
		this.writtenDate = writtenDate;
	}

	public String getGuAddress() {
		return guAddress;
	}

	public void setGuAddress(String guAddress) {
		this.guAddress = guAddress;
	}

	public String getAfterGuAddress() {
		return afterGuAddress;
	}

	public void setAfterGuAddress(String afterGuAddress) {
		this.afterGuAddress = afterGuAddress;
	}

	public String getDetailAddress() {
		return detailAddress;
	}

	public void setDetailAddress(String detailAddress) {
		this.detailAddress = detailAddress;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public float getStar() {
		return star;
	}

	public void setStar(float star) {
		this.star = star;
	}

	public String toString() {
		return "{no:" + no +", title:" + title + ", content:" + content + ", category:" + category + ", writtenDate:" + writtenDate + ", address:" + guAddress + ", afterGuAddress:" + afterGuAddress + ", detailAddress:" + detailAddress + ", x:" + x + ", y:" + y + "}";
	}

}
