package com.company;

import java.io.IOException;
import java.net.MalformedURLException;
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
    public List historiclist;

    public int getPolarity() {
        return polarity;
    }

    public void setPolarity(int polarity) {
        this.polarity = polarity;
    }

    public int polarity;

    public List getHistoriclist() {
        return historiclist;
    }


    public Stock(String symbol) {

        getStock(symbol);

    }

    public void getStock(String symbol) {
        String content = null;
        URLConnection connection = null;
        try {
            connection = new URL("https://finance.google.co.uk/finance?q=" + symbol.toUpperCase() + "&ei=-JPKWfioD4iYUcG0mOgN").openConnection();
            Scanner scanner = new Scanner(connection.getInputStream());
            scanner.useDelimiter("\\Z");
            content = scanner.next();
            News news = new News(content);
            setPolarity(news.getPolarity());
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

    }

//    public void setHistoricStock(String exchange, String symbol) {
//        String content = null;
//        URLConnection connection = null;
//        try {
//            connection = new URL("http://www.eoddata.com/stockquote/" + exchange + "/" + symbol + ".htm").openConnection();
//            Scanner scanner = new Scanner(connection.getInputStream());
//            scanner.useDelimiter("\\Z");
//            content = scanner.next();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        try {
//            content = content.substring(content.indexOf("</th></tr>") + 1);
//            content = content.substring(0, content.indexOf("</table>"));
//            content = content.replaceAll("</td><td", " ");
//            content = content.replaceAll("<tr><td>", " ");
//            content = content.replaceAll("align=\"right\">", "");
//            content = content.replaceAll("</td></tr>", "");
//            content = content.replaceAll("/th></tr>", "");
//            content = content.replaceAll("\r\n", "          ");
//
//        } catch (Exception e) {
//        }
//        String[] historicArray = content.split("          ");
//        ArrayList<String> historicList = new ArrayList<String>();
//
//        for (String part : historicArray) {
//            if (!part.equals("")) {
//                historicList.add(part.substring(part.indexOf(" ") + 1).toUpperCase());
//                //System.out.println(historicList);
//            }
//
//        }
//        this.historiclist = historicList;
//
//    }

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
}
