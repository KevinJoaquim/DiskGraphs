package algorithms;

import java.awt.Point;
import java.util.ArrayList;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

public class DefaultTeam {
    public ArrayList<Point> calculDominatingSet(ArrayList<Point> points, int edgeThreshold) {
        ArrayList<Point> result = (ArrayList<Point>)points.clone();

        ArrayList<String> links = getLinks(result);
        HashMap<Integer,Integer> linksNumber = getLinksNumber(links);

        //Need to sort hashmap by value

        //Delete the dominant and its neighbours (and add to result)

        //...

        for (int i=0;i<points.size()/3;i++) result.remove(0);
        // if (false) result = readFromFile("output0.points");
        // else saveToFile("output",result);

        return result;
    }


    //FILE PRINTER
    private void saveToFile(String filename,ArrayList<Point> result){
        int index=0;
        try {
            while(true){
                BufferedReader input = new BufferedReader(new InputStreamReader(new FileInputStream(filename+Integer.toString(index)+".points")));
                try {
                    input.close();
                } catch (IOException e) {
                    System.err.println("I/O exception: unable to close "+filename+Integer.toString(index)+".points");
                }
                index++;
            }
        } catch (FileNotFoundException e) {
            printToFile(filename+Integer.toString(index)+".points",result);
        }
    }
    private void printToFile(String filename,ArrayList<Point> points){
        try {
            PrintStream output = new PrintStream(new FileOutputStream(filename));
            int x,y;
            for (Point p:points) output.println(Integer.toString((int)p.getX())+" "+Integer.toString((int)p.getY()));
            output.close();
        } catch (FileNotFoundException e) {
            System.err.println("I/O exception: unable to create "+filename);
        }
    }

    //FILE LOADER
    private ArrayList<Point> readFromFile(String filename) {
        String line;
        String[] coordinates;
        ArrayList<Point> points=new ArrayList<Point>();
        try {
            BufferedReader input = new BufferedReader(
                    new InputStreamReader(new FileInputStream(filename))
            );
            try {
                while ((line=input.readLine())!=null) {
                    coordinates=line.split("\\s+");
                    points.add(new Point(Integer.parseInt(coordinates[0]),
                            Integer.parseInt(coordinates[1])));
                }
            } catch (IOException e) {
                System.err.println("Exception: interrupted I/O.");
            } finally {
                try {
                    input.close();
                } catch (IOException e) {
                    System.err.println("I/O exception: unable to close "+filename);
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Input file not found.");
        }
        return points;
    }

    private ArrayList<String> getLinks(ArrayList<Point> input) {
        ArrayList<String> retour = new ArrayList<>();

        return retour;
    }

    private HashMap<Integer,Integer> getLinksNumber(ArrayList<String> input) {
        HashMap<Integer,Integer> retour = new HashMap<>();

        return retour;
    }
}
