package ua.pkk.wetravel.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import java.io.File;

public class User {
    private static User user;
    private String id;
    private String name;

    private String info;
    private Bitmap user_img;


    private User(){
        name = "User";
        info = "Empty";
    };

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        User.user = user;
    }

    public Bitmap getUser_img() {
        return user_img;
    }

    public void setUser_img(Uri user_img, Context context) {
        if (user_img == null || context == null) return;
        File user = new File(context.getFilesDir(), "user_img");


        this.user_img = BitmapFactory.decodeFile(user_img.getPath());
    }

    public static User getInstance(){
        if (user == null){
            user = new User();
        }
        return user;
    }

}