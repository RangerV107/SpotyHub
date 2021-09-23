package com.rangerv.spotyhub.extensions;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class KMeansImage extends AsyncTask<Void, Void, Void> {
    private final Bitmap image;
    private int color;
    private List<Integer> colors = new ArrayList<>();
    IOnPostExecute onPostExecuteI;

    public KMeansImage(Bitmap image, IOnPostExecute onPostExecuteI) {
        this.image = image;
        this.onPostExecuteI = onPostExecuteI;
    }

    @Override protected void onPreExecute() {
        super.onPreExecute();
        colors.clear();
    }

    @Override protected Void doInBackground(Void... voids) {
        ColorClusters(image, 10);
        return null;
    }

    @Override protected void onPostExecute(Void unused) {
        super.onPostExecute(unused);
        onPostExecuteI.onPostExecute(colors);
    }


    private void ColorClusters(Bitmap image, int density){

        int width = image.getWidth();
        int height = image.getHeight();

        List<ColorARGB> pixel_colors = new ArrayList<>();
        for(int x = 0; x < width; x += width / density) {
            for(int y = 0; y < height; y += height / density) {
                pixel_colors.add(new ColorARGB(image.getPixel(x, y)));
            }
        }

        int padding_x = width / 100 * 15;
        int padding_y = height / 100 * 15;
        List<ColorARGB> centroids = new ArrayList<>();
        centroids.add(new ColorARGB(image.getPixel(width / 2, height / 2)));
        centroids.add(new ColorARGB(image.getPixel(          padding_x,            padding_y)));
        centroids.add(new ColorARGB(image.getPixel(width - padding_x,            padding_y)));
        centroids.add(new ColorARGB(image.getPixel(          padding_x, height - padding_y)));
        centroids.add(new ColorARGB(image.getPixel(width - padding_x, height - padding_y)));

        KMeansColor colorsFinder = new KMeansColor(pixel_colors, centroids);
        colors = colorsFinder.Clustering2();

        centroids.clear();
        centroids.add(new ColorARGB(colors.get(0)));
        //centroids.add(new ColorARGB(colors.get(1)));
        centroids.add(new ColorARGB(colors.get(2)));
        pixel_colors.clear();
        for (int i = 0; i < colors.size(); i++) {
            pixel_colors.add(new ColorARGB(colors.get(i)));
        }
        colorsFinder = new KMeansColor(pixel_colors, centroids);
        colors = colorsFinder.Clustering2();

    }

    public interface IOnPostExecute {
        void onPostExecute(List<Integer> colors);
    }
}



class KMeansColor {

    final Random random = new Random();
    public List<ColorARGB> colors = new ArrayList<>();
    public List<ColorARGB> centroids = new ArrayList<>();


    public KMeansColor(List<ColorARGB> colors, List<ColorARGB> centroids) {
        this.colors = colors;
        this.centroids = centroids;
    }


    double ColorDistance(ColorARGB color1, ColorARGB color2) {
        return Math.sqrt(
                Math.pow(color2.r - color1.r, 2) +
                        Math.pow(color2.g - color1.g, 2) +
                        Math.pow(color2.b - color1.b, 2)
        );
    }


    public List<ColorARGB> Clustering() {
        //https://habr.com/ru/post/427761/

        //Clusters init
        List<Cluster> clusters = new ArrayList<>();
        for(int c = 0; c < centroids.size(); c++){
            clusters.add(new Cluster(centroids.get(c)));
        }

        //Clustering
        for(int iteration = 0; iteration < 500; iteration++) {
            for (int p = 0; p < colors.size(); p++) {
                double min_dist = 999999;
                int cluster_pos = 999999;
                for (int c = 0; c < clusters.size(); c++) {
                    double dist = ColorDistance(colors.get(p), clusters.get(c).center);
                    if (dist < min_dist) {
                        min_dist = dist;
                        cluster_pos = c;
                    }
                }
                clusters.get(cluster_pos).AddChild(colors.get(p));
            }

            for (int c = 0; c < clusters.size(); c++) {
                List<ColorARGB> cluster_colors = clusters.get(c).colors;
                Cluster cluster = clusters.get(c);
                for (int p = 0; p < cluster_colors.size(); p++) {
                    ColorARGB color = cluster_colors.get(p);
                    int r = (cluster.center.r + color.r) / 2;
                    int g = (cluster.center.g + color.g) / 2;
                    int b = (cluster.center.b + color.b) / 2;
                    cluster.center = new ColorARGB(255, r, g, b);
                }
                clusters.get(c).colors.clear();
            }
        }

        List<ColorARGB> centers = new ArrayList<>();
        for (int c = 0; c < clusters.size(); c++) {
            centers.add(clusters.get(c).center);
        }
        return centers;
    }

    public List<Integer> Clustering2() {
        List<ColorARGB> centers = Clustering();
        List<Integer> centers_int = new ArrayList<>();
        for (int c = 0; c < centers.size(); c++) {
            centers_int.add(centers.get(c).color);
        }
        return centers_int;
    }



    class Cluster {
        public List<ColorARGB> colors = new ArrayList<>();
        public ColorARGB center;

        public Cluster(ColorARGB center) {
            this.center = center;
        }
        public void AddChild(ColorARGB color){
            colors.add(color);
        }
    }


}
