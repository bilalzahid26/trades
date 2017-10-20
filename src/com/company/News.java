package com.company;

public class News {

    public News(String content){

        content = content.substring(content.indexOf("google.finance.data = {\"common\":")+1, content.indexOf("google.finance.data.numberFormat"));
        String[] split = content.split("lead_story_url");
        for (int i = 0; i < split.length; i++) {
            split[i].substring(split[i].indexOf("\""));
            split[i].substring(0,split[i].indexOf("\","));
            System.out.println(split[i]);
        }
    }
}
