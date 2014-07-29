package com.example.babiesvsaliens;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class User {
	
	@SerializedName("nick")
	public static String nick;
	
	@SerializedName("points")
	public static String points;
	
	public List<User> results;


}
