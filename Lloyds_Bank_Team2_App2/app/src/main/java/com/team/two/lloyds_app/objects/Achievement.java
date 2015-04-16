package com.team.two.lloyds_app.objects;

/**
 * Created by Deniz on 15/04/2015.
 */
public class Achievement {
    private int achievementId;
    private String title;
    private String description;
    private int pointsAchieved;
    private int iconId;

    public Achievement(int achievementId, String title, String description, int pointsAchieved, int iconId)
    {
        this.achievementId = achievementId;
        this.title = title;
        this.description = description;
        this.pointsAchieved = pointsAchieved;
        this.iconId = iconId;
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
}