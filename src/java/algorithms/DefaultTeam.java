package algorithms;

import java.awt.Point;
import java.util.*;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class DefaultTeam {

    private HashMap<Integer,Point> saveAllPoints = new HashMap<>();

    public ArrayList<Point> calculDominatingSet(ArrayList<Point> points, int edgeThreshold) {
        ArrayList<Point> result = new ArrayList<>();
        ArrayList<Point> allPoints = (ArrayList<Point>)points.clone();

        allPoints.forEach(x-> saveAllPoints.put(saveAllPoints.size(),x));

        while (allPoints.size()>0) {
            //Calcul all links beetwen all points
            ArrayList<Lien> links = getLinks(allPoints,edgeThreshold);

            if (links.size() > 0) {
                //Get number of links for each points
                HashMap<Integer, Integer> linksNumber = getLinksNumber(links);

                //Need to sort hashmap by value
                Integer sommet = Collections.max(linksNumber.entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getKey();

                //Add dominant to result
                result.add(saveAllPoints.get(sommet));

                //Delete the dominant and its neighbours (and add to result)
                allPoints.remove(saveAllPoints.get(sommet));
                for (Integer toDelete : getNeighbours(sommet, links))
                    allPoints.remove(saveAllPoints.get(toDelete));

            }else {
                result.addAll(allPoints);
                allPoints.clear();
            }

        }

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

    private ArrayList<Lien> getLinks(ArrayList<Point> input, int edgeThreshold) {
        ArrayList<Lien> retour = new ArrayList<>();

        for (int i = 0; i<input.size();i++) {
            for (int j=0; j< input.size();j++) {
                if (i != j) {
                    Point origine = input.get(i);
                    Point compare = input.get(j);

                    if (origine.distance(compare.x,compare.y) < edgeThreshold) {
                        Integer point1 = getKeysByValue(saveAllPoints,origine).iterator().next();
                        Integer point2 = getKeysByValue(saveAllPoints,compare).iterator().next();

                        retour.add(new Lien(point1,point2));
                    }
                }
            }
        }

        return retour;
    }

    private HashMap<Integer,Integer> getLinksNumber(ArrayList<Lien> input) {
        HashMap<Integer,Integer> retour = new HashMap<>();

        input.forEach(in -> {
            retour.put(in.idPoint1,retour.getOrDefault(in.idPoint1,0)+ 1);
            retour.put(in.idPoint2,retour.getOrDefault(in.idPoint2,0)+ 1);
        });

        return retour;
    }

    private ArrayList<Integer> getNeighbours(Integer position, ArrayList<Lien> links) {
        ArrayList<Integer> retour = new ArrayList<>();

        links.forEach(x-> {
            if (x.idPoint1.equals(position)) retour.add(x.idPoint2);
            if (x.idPoint2.equals(position)) retour.add(x.idPoint1);

        });

        return retour;
    }

    public static <T, E> Set<T> getKeysByValue(Map<T, E> map, E value) {
        return map.entrySet()
                .stream()
                .filter(entry -> Objects.equals(entry.getValue(), value))
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
    }

    private class Lien {
        Integer idPoint1;
        Integer idPoint2;

        public Lien(Integer idPoint1, Integer idPoint2) {
            this.idPoint1 = idPoint1;
            this.idPoint2 = idPoint2;
        }
    }
}
