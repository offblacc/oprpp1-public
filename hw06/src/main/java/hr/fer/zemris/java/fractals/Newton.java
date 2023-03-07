package hr.fer.zemris.java.fractals;

import hr.fer.zemris.java.fractals.viewer.FractalViewer;
import hr.fer.zemris.java.fractals.viewer.IFractalProducer;
import hr.fer.zemris.math.Complex;
import hr.fer.zemris.math.ComplexPolynomial;
import hr.fer.zemris.math.ComplexRootedPolynomial;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Program that draws Newton-Raphson fractals.
 */
public class Newton {
    /**
     * Main method.
     * @param args Command line arguments. Ignored.
     */
    public static void main(String[] args) {
        System.out.println("Welcome to Newton-Raphson iteration-based fractal viewer.");
        System.out.println("Please enter at least two roots, one root per line. Enter 'done' when done.");
        int i = 1;
        Scanner sc = new Scanner(System.in);
        String line;
        List<Complex> complexRoots = new ArrayList<>();
        do {
            System.out.printf("Root " + i++ + " > ");
            line = sc.nextLine();
            try {
                Complex complex = Complex.parse(line);
                complexRoots.add(complex);
            } catch (IllegalArgumentException e) {
                if (!line.equals("done")) {
                    System.out.println("Invalid input, try again.");
                    i--;
                }
            }
        } while (!line.equals("done"));
        sc.close();
        System.out.println("Image of fractal will appear shortly. Thank you.");

        FractalViewer.show(getSequentialFractalProducer(complexRoots));
    }


    /**
     * Method that returns an instance of IFractalProducer that draws Newton-Raphson fractals.
     * @param roots Roots of the polynomial.
     * @return Instance of IFractalProducer that draws Newton-Raphson fractals.
     */
    private static IFractalProducer getSequentialFractalProducer(List<Complex> roots) {
        return (reMin, reMax, imMin, imMax, width, height, requestNo, observer, cancel) -> {
            System.out.println("Zapocinjem izracun...");
            ComplexRootedPolynomial rootedPolynomial = new ComplexRootedPolynomial(Complex.ONE, roots.toArray(new Complex[0]));
            ComplexPolynomial complexPolynomial = rootedPolynomial.toComplexPolynomial();
            ComplexPolynomial derived = complexPolynomial.derive();
            int m = 16 * 16 * 16;
            short[] data = new short[width * height];
            int offset = 0;
            for (int y = 0; y < height; y++) {
                if (cancel.get()) {
                    System.out.println("Izracun prekinut.");
                    return;
                }
                for (int x = 0; x < width; x++) {
                    double cre = x / (width - 1.0) * (reMax - reMin) + reMin;
                    double cim = (height - 1.0 - y) / (height - 1) * (imMax - imMin) + imMin;
                    Complex zn = new Complex(cre, cim); // piksel odnosno kompleksni broj u kojem računaš vrijednost polinoma
                    int iter = 0;
                    double module;
                    do {
                        Complex numerator = rootedPolynomial.apply(zn);
                        Complex denominator = derived.apply(zn);
                        Complex znold = zn;
                        Complex fraction = numerator.divide(denominator);
                        zn = zn.sub(fraction);
                        module = znold.sub(zn).module();
                        iter++;
                    } while (module > 1E-3 && iter < m);
                    int index = rootedPolynomial.indexOfClosestRootFor(zn, 0.002);
                    data[offset++] = (short) (index + 1);
                }
            }
            System.out.println("Izracun gotov...");
            observer.acceptResult(data, (short) (roots.size() + 1), requestNo);
        };
    }
}
