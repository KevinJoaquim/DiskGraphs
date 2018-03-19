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
            ArrayList<String> links = getLinks(allPoints,edgeThreshold);
            System.out.println("getLinks ok " + links.size());

            if (links.size() > 0) {
                HashMap<Integer, Integer> linksNumber = getLinksNumber(links);
                System.out.println("getLinksNumber ok " + linksNumber.size());
                //Need to sort hashmap by value
                Integer sommet = Collections.max(linksNumber.entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getKey();

                System.out.println("max ok " + sommet);

                //Add dominant to result
                result.add(saveAllPoints.get(sommet));

                //Delete the dominant and its neighbours (and add to result)
                allPoints.remove(saveAllPoints.get(sommet));
                for (Integer toDelete : getNeighbours(sommet, links))
                    allPoints.remove(saveAllPoints.get(toDelete));

                System.out.println("Remain points " + allPoints.size());
            }else {
                allPoints.forEach(point -> result.add(point));
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

    private ArrayList<String> getLinks(ArrayList<Point> input, int edgeThreshold) {
        ArrayList<String> retour = new ArrayList<>();

        for (int i = 0; i<input.size();i++) {
            for (int j=0; j< input.size();j++) {
                if (i != j) {
                    Cercle c = new Cercle(input.get(i),edgeThreshold);
                    Point compare = input.get(j);

                    if (c.isInside(compare)){
                        Integer point1 = getKeysByValue(saveAllPoints,input.get(i)).iterator().next();
                        Integer point2 = getKeysByValue(saveAllPoints,input.get(j)).iterator().next();

                        retour.add(point1 + " " + point2);
                    }
                }
            }
        }

        return retour;
    }

    private HashMap<Integer,Integer> getLinksNumber(ArrayList<String> input) {
        HashMap<Integer,Integer> retour = new HashMap<>();

        input.forEach(in -> {
            String [] s = in.split(" ");
            Integer nbr1 = Integer.parseInt(s[0]);
            Integer nbr2 = Integer.parseInt(s[1]);

            retour.put(nbr1,retour.getOrDefault(nbr1,0)+ 1);
            retour.put(nbr2,retour.getOrDefault(nbr2,0)+ 1);
        });

        return retour;
    }

    private ArrayList<Integer> getNeighbours(Integer position, ArrayList<String> links) {
        ArrayList<Integer> retour = new ArrayList<>();

        links.forEach(x-> {
            String [] s = x.split(" ");
            Integer nbr1 = Integer.parseInt(s[0]);
            Integer nbr2 = Integer.parseInt(s[1]);

            if (nbr1.equals(position)) retour.add(nbr2);
            if (nbr2.equals(position)) retour.add(nbr1);

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

    private class Cercle {
        Point point;
        int rayon;

        public Cercle(Point point, int rayon) {
            this.point = point;
            this.rayon = rayon;
        }

        public boolean isInside(Point compare){
            int d2 = (compare.x-this.point.x)*(compare.x-this.point.x) + (compare.y-this.point.y)*(compare.y-this.point.y);

            return !(d2>this.rayon*this.rayon);
        }
    }
}
