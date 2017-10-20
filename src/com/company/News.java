package com.company;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class News {
    String[] split;

    public int getPolarity() {
        return polarity;
    }

    public void setPolarity(int polarity) {
        this.polarity = polarity;
    }

    int polarity = 0;

    public News(String content) {

        content = content.substring(content.indexOf("google.finance.data = {\"common\":") + 1, content.indexOf("google.finance.data.numberFormat"));
        split = content.split("lead_story_url");
        for (int i = 0; i < split.length; i++) {
            split[i] = split[i].substring(split[i].indexOf("\"") + 1);
            split[i] = split[i].substring(0, split[i].indexOf("\"a"));
            split[i] = split[i].substring(2, split[i].length() - 2);

            //System.out.println(split[i]);
        }
        for (int i = 1; i < split.length; i++) {
            if (split[i] != "") {
                getArticle(split[i]);

            }
        }

    }

    public void getArticle(String s) {

        String content = null;
        URLConnection connection = null;
        try {
            connection = new URL(s).openConnection();
            Scanner scanner = new Scanner(connection.getInputStream());
            scanner.useDelimiter("\\Z");
            content = scanner.next();
            content = content.substring(content.indexOf("<p>"));
            content = content.substring(0, content.lastIndexOf("</p>"));
            String lines[] = content.split("\\r?\\n");
            ArrayList<String> articles = new ArrayList<String>();

            for (int i = 0; i < lines.length; i++) {

                if (!lines[i].contains("<p>")) {
                    lines[i] = "";
                } else {
                    articles.add(lines[i]);
                }


            }
            String article = "";
            for (int i = 0; i < articles.size(); i++) {
                articles.set(i, articles.get(i).replaceAll("<.*?>", ""));
                articles.set(i, articles.get(i).replaceAll("\\s+", " "));
                article = article + " " + articles.get(i);

            }
            checkPositivity(article);
        } catch (Exception ex) {
        }


    }

    public void checkPositivity(String article) {

        String csvFile = "/Users/BilalZahid/Desktop/Computer Science/GitHub/trades/positive-words.csv";
        String csvFile1 = "/Users/BilalZahid/Desktop/Computer Science/GitHub/trades/negative-words.csv";
        String[] articleWords = article.split(" ");
        String[] negativeWord = new String[0];
        String[] positiveWord = new String[0];
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {


            String str;

            List<String> list = new ArrayList<String>();
            while ((str = br.readLine()) != null) {
                list.add(str);
            }


            positiveWord = list.toArray(new String[0]);


        } catch (IOException e) {
            e.printStackTrace();
        }
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile1))) {

            String str;

            List<String> list = new ArrayList<String>();
            while ((str = br.readLine()) != null) {
                list.add(str);
            }


            negativeWord = list.toArray(new String[0]);


        } catch (IOException e) {

            e.printStackTrace();
        }

        for (int i = 0; i < positiveWord.length; i++) {

            for (int j = 0; j < articleWords.length; j++) {
                if (positiveWord[i].equals(articleWords[j])) {

                    polarity++;
                }
            }

        }
        for (int i = 0; i < negativeWord.length; i++) {

            for (int j = 0; j < articleWords.length; j++) {
                if (negativeWord[i].equals(articleWords[j])) {

                    polarity--;
                }
            }

        }
    }
}
