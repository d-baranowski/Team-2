package com.team.two.lloyds_app.objects;

/**
 * Author: Deniz
 * Date: 15/04/2015
 * Purpose: data about achievements
 */
public class Achievement{
	private int achievementId;
	private String title;
	private String description;
	private int pointsAchieved;
	private int iconId;
	private int incremental;

	//Single constructor
	public Achievement(int achievementId, String title, String description, int pointsAchieved, int iconId, int incremental){
		this.achievementId = achievementId;
		this.title = title;
		this.description = description;
		this.pointsAchieved = pointsAchieved;
		this.iconId = iconId;
		this.incremental = incremental;
	}

	//getters for fields
	public int getAchievementId(){
		return achievementId;
	}

	public String getTitle(){
		return title;
	}

	public String getDescription(){
		return description;
	}

	public int getPointsAchieved(){
		return pointsAchieved;
	}

	public int getIconId(){
		return iconId;
	}

	public int getIncremental(){
		return incremental;
	}
}