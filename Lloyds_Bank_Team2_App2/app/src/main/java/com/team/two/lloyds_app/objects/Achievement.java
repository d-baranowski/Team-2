package com.team.two.lloyds_app.objects;

/**
 * Author: Deniz
 * Date: 15/04/2015
 * Purpose: data about achievements
 */
public class Achievement {
    private int achievementId;
    private String title;
    private String description;
    private int pointsAchieved;
    private int iconId;
    private int incremental;

    public Achievement(int achievementId, String title, String description, int pointsAchieved, int iconId, int incremental)
    {
        this.achievementId = achievementId;
        this.title = title;
        this.description = description;
        this.pointsAchieved = pointsAchieved;
        this.iconId = iconId;
        this.incremental = incremental;
    }

    public int getAchievementId()
    {
        return this.achievementId;
    }

    public String getTitle()
    {
        return this.title;
    }

    public String getDescription()
    {
        return this.description;
    }

    public int getPointsAchieved()
    {
        return this.pointsAchieved;
    }

    public int getIconId() { return this.iconId; }

    public int isIncremental() { return this.incremental; }
}