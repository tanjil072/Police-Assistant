package com.example.user.policeassistant;

public class Blog {

    private String Title;
    private String Description;
    private String PresentAdd;
    private String Rewards;
    private String CriminalsName;
    private String FathersName;
    private String MothersName;
    private String PermanentAdd;


    public Blog(String title, String description, String presentAddress, String reward,String name,String father,String mother,String permanentAdd) {
        Title = title;
        Description = description;
        PresentAdd = presentAddress;
        Rewards = reward;
        CriminalsName=name;
        FathersName=father;
        MothersName=mother;
        PermanentAdd=permanentAdd;

    }

    public String getTitle() {
        return Title;
    }
    public String getCriminalsName() {
        return CriminalsName;
    }
    public String getFathersName() {
        return FathersName;
    }
    public String getMothersName()
    {
        return MothersName;
    }
    public String getPermanentAdd()
    {
        return PermanentAdd;
    }
    public String getDescription() {
        return Description;
    }
    public String getPresentAdd() {
        return PresentAdd;
    }
    public String getRewards()
    {
        return Rewards;
    }

    public Blog(){

    }


}
