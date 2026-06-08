package ua.foxminded.school.model;

public class Group {
    private int groupId;
    private String groupName;
    public Group(int groupId,String groupName){
        this.groupId=groupId;
        this.groupName=groupName;
    }
    public int getGroupId(){
        return groupId;
    }
    public String getGroupName(){
        return groupName;
    }
}
