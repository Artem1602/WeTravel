package ua.pkk.wetravel.utils;

public class User {
    private static User user;
    private String id;

    private User(){};

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public static User getInstance(){
        if (user == null){
            user = new User();
        }
        return user;
    }
}