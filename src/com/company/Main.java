package com.company;


import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;


public class Main {


    public static void main(String[] args) throws IOException {


        String[] tickerSymbol = {"tsla"};
        ArrayList<Stock> listOfStock = new ArrayList<>();
        for (int i = 0; i < tickerSymbol.length; i++) {
            listOfStock.add(new Stock(tickerSymbol[i]));


//            if (listOfStock.size() != 0) {
//                listOfStock.get(i).setHistoricStock(listOfStock.get(i).getExchange(), tickerSymbol[i]);
//            }
//            System.out.println(listOfStock.get(i).getName());


        }


        for (int i = 0; i < listOfStock.size(); i++) {


            System.out.println(listOfStock.get(i).getTickerSymbol());
            File idea = new File(listOfStock.get(i).getTickerSymbol() + ".CSV");
            String passing = "";

            if (idea.isFile()) {
                Scanner sc = new Scanner(idea);
                while (sc.hasNextLine()) {
                    passing = sc.nextLine();
                }
                writeFile(passing, idea, listOfStock, i);


            } else {
                idea.createNewFile();
                writeFile(passing, idea, listOfStock, i);

            }
        }
    }

    private static void writeFile(String passing, File idea, ArrayList<Stock> listOfStock, int i) throws IOException {
        FileWriter writer = new FileWriter(idea);
        writer.append(passing);
        writer.append('\n');

        writer.append(listOfStock.get(i).getName());
        writer.append(", ");
        writer.append(listOfStock.get(i).getCurrency());
        writer.append(", ");
        writer.append(listOfStock.get(i).getExchange());
        writer.append(", ");
        writer.append(listOfStock.get(i).getExchangeTimeZone());
        writer.append(", ");
        writer.append(listOfStock.get(i).getTickerSymbol());
        writer.append(", ");
        writer.write(String.valueOf(listOfStock.get(i).getPrice()));
        writer.append(", ");
        writer.write(String.valueOf(listOfStock.get(i).getPriceChange()));
        writer.append(", ");
        writer.write(String.valueOf(listOfStock.get(i).getPriceChangePercent()));
//            for (int j = 0; j < listOfStock.get(i).getHistoriclist().size(); j++) {
//                String string = (String) listOfStock.get(i).getHistoriclist().get(j);
//                String[] parts = string.split(" ");
//
//                StringBuilder sb = new StringBuilder();
//                for (int k = 0; k < parts.length; k++) {
//                    sb.append(parts[k]);
//                }
//                sb.setLength(sb.length() - 1);
//                writer.write('\n');
//                writer.write(sb.toString());
//            }

        writer.flush();
        writer.close();
    }
}