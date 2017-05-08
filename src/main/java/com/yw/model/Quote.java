package com.yw.model;

import java.util.Date;

import javax.persistence.*;

@Entity
public class Quote {

	@Id
	private int date;

	@Column
	private Double open;

	@Column
	private Double close;

	@Column
	private Double high;

	@Column
	private Double low;

	@Column
	private Double volume;

	@Column
	private Double turnover;

	public Quote() {
	}

	public Quote(int date, Double open, Double close, Double high, Double low, Double volume, Double turnover) {
		super();
		this.date = date;
		this.open = open;
		this.close = close;
		this.high = high;
		this.low = low;
		this.volume = volume;
		this.turnover = turnover;
	}

	public int getDate() {
		return date;
	}

	public void setDate(int date) {
		this.date = date;
	}

	public Double getOpen() {
		return open;
	}

	public void setOpen(Double open) {
		this.open = open;
	}

	public Double getClose() {
		return close;
	}

	public void setClose(Double close) {
		this.close = close;
	}

	public Double getHigh() {
		return high;
	}

	public void setHigh(Double high) {
		this.high = high;
	}

	public Double getLow() {
		return low;
	}

	public void setLow(Double low) {
		this.low = low;
	}

	public Double getVolume() {
		return volume;
	}

	public void setVolume(Double volume) {
		this.volume = volume;
	}

	public Double getTurnover() {
		return turnover;
	}

	public void setTurnover(Double turnover) {
		this.turnover = turnover;
	}

}
