package com.qa.framework.library.image.compare;

import com.library.common.IOHelper;
import com.qa.framework.config.PropConfig;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * ImageCompare changes image from RGB to LAB and make comparison
 */
@SuppressWarnings("restriction")
public class ImageCompare {

    /**
     * Change RGB to XYZ
     *
     * @param rgb the rgb
     * @return xyz color xyz
     */
    public static ColorXYZ rgb2xyz(int rgb) {
        ColorXYZ xyz = new ColorXYZ();
        int r = (rgb & 0xff0000) >> 16;
        int g = (rgb & 0xff00) >> 8;
        int b = (rgb & 0xff);
        if ((r == 0) && (g == 0) && (b == 0)) {
            xyz.x = 0;
            xyz.y = 0;
            xyz.z = 0;
        } else {
            xyz.x = (0.490 * r + 0.310 * g + 0.200 * b) / (0.667 * r + 1.132 * g + 1.200 * b);
            xyz.y = (0.117 * r + 0.812 * g + 0.010 * b) / (0.667 * r + 1.132 * g + 1.200 * b);
            xyz.z = (0.000 * r + 0.010 * g + 0.990 * b) / (0.667 * r + 1.132 * g + 1.200 * b);
        }
        return xyz;
    }

    /**
     * Change XYZ to LAB
     *
     * @param xyz the xyz
     * @return lab color lab
     */
    public static ColorLAB xyz2lab(ColorXYZ xyz) {
        ColorLAB lab = new ColorLAB();
        double x = xyz.x / 95.047;
        double y = xyz.y / 100.000;
        double z = xyz.z / 108.883;
        x = (x > 0.008856) ? Math.pow(x, 1.0 / 3.0) : (7.787 * x + 16.0 / 116);
        y = (y > 0.008856) ? Math.pow(y, 1.0 / 3.0) : (7.787 * y + 16.0 / 116);
        z = (z > 0.008856) ? Math.pow(z, 1.0 / 3.0) : (7.787 * z + 16.0 / 116);
        lab.l = 116 * Math.pow(y, 1.0 / 3.0) - 16;
        lab.a = 500 * (Math.pow(x, 1.0 / 3.0) - Math.pow(y, 1.0 / 3.0));
        lab.b = 200 * (Math.pow(y, 1.0 / 3.0) - Math.pow(z, 1.0 / 3.0));
        return lab;
    }

    /**
     * Calculate the color difference
     *
     * @param lab1 the lab 1
     * @param lab2 the lab 2
     * @return totalColorDifference delta
     */
    public static double getDelta(ColorLAB lab1, ColorLAB lab2) {
        double deltaL = lab1.l - lab2.l; // lightness difference
        double deltaA = lab1.a - lab2.a; // chromaticity difference
        double deltaB = lab1.b - lab2.b; // chromaticity difference
        return Math.pow((Math.pow(deltaL, 2) + Math.pow(deltaA, 2) + Math.pow(deltaB, 2)), 0.5); // total color difference
    }

    /**
     * compare images
     *
     * @param benchmarkImageName the benchmark image name
     * @param actualImageName    the actual image name
     * @return the boolean
     * @throws Exception the exception
     */
    public static boolean compareImages(String benchmarkImageName, String actualImageName) throws Exception {
        boolean isMatched = true;
        BufferedImage benchmark = null, actual = null, difference = null;
        try {
            benchmark = ImageIO.read(new File(PropConfig.getBenchmarkImagePath() + File.separator + benchmarkImageName));
            actual = ImageIO.read(new File(PropConfig.getActualImagePath() + File.separator + actualImageName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (benchmark != null) {
            Integer width = benchmark.getWidth();
            Integer height = benchmark.getHeight();
            difference = new BufferedImage(benchmark.getWidth(), benchmark.getHeight(), BufferedImage.TYPE_INT_RGB);
            for (int y = 0; y < height; ++y) {
                for (int x = 0; x < width; ++x) {
                    int expRGB = benchmark.getRGB(x, y);
                    int actRGB = actual.getRGB(x, y);
                    int newRGB = actRGB;
                    if (expRGB != actRGB) {
                        double deltaE = getDelta(xyz2lab(rgb2xyz(expRGB)), xyz2lab(rgb2xyz(actRGB)));
                        if (deltaE > PropConfig.getMaxColorThreshold()) {
                            newRGB = 0xff0000;// set red in difference place
                        }
                        isMatched = false;
                    }
                    difference.setRGB(x, y, newRGB);
                }
            }
            FileOutputStream out = null;
            if (!isMatched) {
                try {
                    String baseName = IOHelper.getBaseName(actualImageName);
                    String diffImagePath = PropConfig.getDiffImagePath();
                    IOHelper.createNestDirectory(diffImagePath);
                    out = new FileOutputStream(diffImagePath + File.separator + baseName + ".png");
                    ImageIO.write(difference, "png", out);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (out != null)
                        out.close();
                }
            }
        }
        return isMatched;
    }

    /**
     * RGB color space
     */
    public static class ColorLAB {
        /**
         * The L.
         */
        public double l;
        /**
         * The A.
         */
        public double a;
        /**
         * The B.
         */
        public double b;
    }

    /**
     * XYZ color space
     */
    public static class ColorXYZ {
        /**
         * The X.
         */
        public double x;
        /**
         * The Y.
         */
        public double y;
        /**
         * The Z.
         */
        public double z;
    }
}
