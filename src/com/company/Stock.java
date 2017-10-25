package com.company;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

public class Stock {
    public String name = "";
    public String tickerSymbol = "";
    public String exchange = "";
    public String exchangeTimeZone = "";
    public double price;
    public double priceChange;
    public double priceChangePercent;
    public String currency;
    public int polarity = 0;


//


    public Stock(String symbol) {

        getStock(symbol);

    }

    public void getStock(String symbol) {
        String content = null;
        URLConnection connection = null;
        try {
            connection = new URL("https://finance.google.co.uk/finance?q="+symbol.toUpperCase()).openConnection();
            Scanner scanner = new Scanner(connection.getInputStream());
            scanner.useDelimiter("\\Z");
            content = scanner.next();
            getNews(content);
            setPolarity(polarity);


            content = content.substring(content.indexOf("itemprop=") + 1, content.indexOf("/>\n" +
                    "</div>\n" +
                    "</div>\n" +
                    "</div>\n" +
                    "<div class=\"g-section g-tpl-right-1 sfe-break-top-5\">"));
            content = "<meta i" + content;
            content = content.replaceAll("<meta itemprop=", "");
            content = content.replaceAll("<meta  itemprop=", "");
            content = content.replaceAll("/>", "");
            content = content.replaceAll("\\s+", " ");
            content = content.replaceAll("> ", "\" \"");
            String split = "\" \"";

            String[] parts = content.split(split);
            ArrayList<String> list = new ArrayList<String>();

            for (int i = 0; i < parts.length; i++) {
                parts[i] = parts[i].replaceAll("\"", "");
                parts[i] = parts[i].replaceAll("content=", "");
                parts[i] = parts[i].replaceAll(",", "");
                parts[i] = parts[i].replaceAll("\\+", "");
                parts[i] = parts[i].replaceAll("-", "");

                parts[i] = parts[i].toLowerCase();
                if (parts[i].contains("www.") || parts[i].contains("after") || parts[i].contains("datasource") || parts[i].contains("quote")) {
                    parts[i] = "";
                }
            }
            for (String part : parts) {
                if (!part.equals(""))
                    list.add(part.substring(part.indexOf(" ") + 1).toUpperCase());

            }
            for (int i = 0; i < list.size(); i++) {


                if (list.get(i).equals("")) {
                    list.set(i, "0.00");

                }
            }
            if (list.size() != 0) {
                setName(list.get(0));
                setTickerSymbol(list.get(1));
                setExchange(list.get(2));
                setExchangeTimeZone(list.get(3));
                setPrice(Double.parseDouble(list.get(4)));
                setPriceChange(Double.parseDouble(list.get(5)));
                setPriceChangePercent(Double.parseDouble(list.get(6)));
                setCurrency(list.get(7));


            }
        } catch (Exception e) {
        }
        System.out.println("made: " + getTickerSymbol());
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTickerSymbol() {
        return tickerSymbol;
    }

    public void setTickerSymbol(String tickerSymbol) {
        this.tickerSymbol = tickerSymbol;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {

        if (exchange.equals("LON")) {
            this.exchange = "LSE";
        } else if (exchange.equals("KLSE")) {
            this.exchange = "LSE";
        } else {

            this.exchange = exchange;
        }

    }

    public String getExchangeTimeZone() {
        return exchangeTimeZone;
    }

    public void setExchangeTimeZone(String exchangeTimeZone) {
        this.exchangeTimeZone = exchangeTimeZone;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getPriceChange() {
        return priceChange;
    }

    public void setPriceChange(double priceChange) {
        this.priceChange = priceChange;
    }

    public double getPriceChangePercent() {
        return priceChangePercent;
    }

    public void setPriceChangePercent(double priceChangePercent) {
        this.priceChangePercent = priceChangePercent;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    String[] split;

    public int getPolarity() {
        return polarity;
    }

    public void setPolarity(int polarity) {
        this.polarity = polarity;
    }


    public void getNews(String content) {

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

