package com.company;


import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class News {
    String[] split;
    public News(String content){

        content = content.substring(content.indexOf("google.finance.data = {\"common\":")+1, content.indexOf("google.finance.data.numberFormat"));
         split = content.split("lead_story_url");
        for (int i = 0; i < split.length; i++) {
            split[i] = split[i].substring(split[i].indexOf("\"")+1);
            split[i] = split[i].substring(0,split[i].indexOf("\"a"));
            split[i] = split[i].substring(2,split[i].length()-2);

            //System.out.println(split[i]);
        }
getArticle();

    }

    public void getArticle(){

        String content = null;
        URLConnection connection = null;
        try {
            connection = new URL(split[5]).openConnection();
            System.out.println(split[5]);
            Scanner scanner = new Scanner(connection.getInputStream());
            scanner.useDelimiter("\\Z");
            content = scanner.next();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        content = content.substring(content.indexOf("<p>"));
        content = content.substring(0, content.lastIndexOf("</p>"));
        String lines[] = content.split("\\r?\\n");
        ArrayList<String> articles = new ArrayList<String>();

        for (int i = 0; i < lines.length; i++) {

            if (!lines[i].contains("<p>")){
                lines[i] = "";
            }else{
                articles.add(lines[i]);
            }


        }
        String article = "";
        for (int i = 0; i < articles.size(); i++) {
            articles.set(i,articles.get(i).replaceAll("<.*?>", ""));
            articles.set(i,articles.get(i).replaceAll("\\s+"," "));
            article = article + " " + articles.get(i);

        }
        System.out.println(article);

    }
}
