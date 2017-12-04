package com.example.jason.ftp;

import android.widget.Button;

import java.io.Serializable;


public class Card implements Serializable{

	public int x;
	public int y;
	public transient Button button;
	
	public Card(Button button, int x,int y) {
		this.x = x;
		this.y=y;
		this.button=button;
	}
	

}
