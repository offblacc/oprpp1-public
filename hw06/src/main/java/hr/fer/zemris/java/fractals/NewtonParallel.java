package hr.fer.zemris.java.fractals;

import hr.fer.zemris.java.fractals.viewer.FractalViewer;
import hr.fer.zemris.java.fractals.viewer.IFractalProducer;
import hr.fer.zemris.java.fractals.viewer.IFractalResultObserver;
import hr.fer.zemris.math.Complex;
import hr.fer.zemris.math.ComplexPolynomial;
import hr.fer.zemris.math.ComplexRootedPolynomial;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.lang.System.exit;

/**
 * Multithreaded Newton-Raphson fractal generator.
 */
public class NewtonParallel {
    /**
     * Main method, entry point of the program. Takes arguments 'workers' and 'tracks'
     * from the command line and starts the fractal viewer (--tracks=4 --workers=4 or
     * -w 4 -t 4).
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        HashMap<String, Integer> params = parseArguments(args);
        System.out.println("Welcome to Newton-Raphson iteration-based fractal viewer.");
        int numOfThreads = params.get("workers");
        int numOfTracks = params.get("tracks");
        System.out.println("Using " + numOfThreads + " threads and " + numOfTracks + " tracks.");
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
        if (complexRoots.size() < 2) {
            System.out.println("You must enter at least two roots.");
            exit(1);
        }
        sc.close();
        System.out.println("Image of fractal will appear shortly. Thank you.");
        FractalViewer.show(new MyProducer(new ComplexRootedPolynomial(Complex.ONE, complexRoots.toArray(new Complex[0])), numOfThreads, numOfTracks));
    }

    /**
     * Class representing a calculation job for a chunk of the fractal.
     */
    public static class CalculationJob implements Runnable {
        /**
         * Minimum real value of the chunk.
         */
        double reMin;

        /**
         * Maximum real value of the chunk.
         */
        double reMax;

        /**
         * Minimum imaginary value of the chunk.
         */
        double imMin;

        /**
         * Maximum imaginary value of the chunk.
         */
        double imMax;

        /**
         * Width of the chunk.
         */
        int width;

        /**
         * Height of the chunk.
         */
        int height;

        /**
         * Minimum y value of the chunk.
         */
        int yMin;

        /**
         * Maximum y value of the chunk.
         */
        int yMax;

        /**
         * Maximum number of iterations.
         */
        int m;

        /**
         * Color values of the chunk.
         */
        short[] data;


        /**
         * Cancel flag.
         */
        AtomicBoolean cancel;

        /**
         * The polynomial whose fractal is being calculated as ComplexRootedPolynomial.
         */
        ComplexRootedPolynomial rootedPolynomial;

        /**
         * The polynomial whose fractal is being calculated as ComplexPolynomial.
         */
        ComplexPolynomial complexPolynomial;

        /**
         * Derivative of the polynomial whose fractal is being calculated.
         */
        ComplexPolynomial derived;

        /**
         * Empty calculation job constructor.
         */
        public static final CalculationJob NO_JOB = new CalculationJob();

        /**
         * Default constructor.
         */
        public CalculationJob() {
        }


        /**
         * Constructor for the calculation job.
         * @param reMin minimum real value of the chunk
         * @param reMax maximum real value of the chunk
         * @param imMin minimum imaginary value of the chunk
         * @param imMax maximum imaginary value of the chunk
         * @param width width of the chunk
         * @param height height of the chunk
         * @param yMin minimum y value of the chunk
         * @param yMax maximum y value of the chunk
         * @param m maximum number of iterations
         * @param data color values of the chunk
         * @param cancel cancel flag
         * @param roots roots of the polynomial whose fractal is being calculated
         */
        public CalculationJob(double reMin, double reMax, double imMin, double imMax, int width, int height, int yMin, int yMax, int m, short[] data, AtomicBoolean cancel, Complex[] roots) {
            this.reMin = reMin;
            this.reMax = reMax;
            this.imMin = imMin;
            this.imMax = imMax;
            this.width = width;
            this.height = height;
            this.yMin = yMin;
            this.yMax = yMax;
            this.m = m;
            this.data = data;
            this.cancel = cancel;
            this.rootedPolynomial = new ComplexRootedPolynomial(Complex.ONE, roots);
            this.complexPolynomial = rootedPolynomial.toComplexPolynomial();
            this.derived = complexPolynomial.derive();
        }

        /**
         * Calculates the fractal for the chunk.
         */
        @Override
        public void run() {
            int offset = yMin * width;
            for (int y = yMin; y < yMax; y++) {
                if (cancel.get()) {
                    System.out.println("Izracun prekinut.");
                    return;
                }
                for (int x = 0; x < width; x++) {
                    double cre = x / (width - 1.0) * (reMax - reMin) + reMin;
                    double cim = (height - 1.0 - y) / (height - 1) * (imMax - imMin) + imMin;
                    Complex zn = new Complex(cre, cim);
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
        }
    }

    /**
     * Class representing a producer of calculation jobs.
     */
    public static class MyProducer implements IFractalProducer {
        /**
         * Number of threads to use for the calculation.
         */
        private final int numOfThreads;

        /**
         * Number of tracks to use for the calculation.
         */
        private final int numOfTracks;

        /**
         * Polynomial whose fractal is being calculated as ComplexPolynomial.
         */
        private final ComplexPolynomial polynomial;

        /**
         * Polynomial whose fractal is being calculated as ComplexRootedPolynomial.
         */
        private final ComplexRootedPolynomial rootedPolynomial;

        /**
         * Constructor for the producer, taking roots of the polynomial whose fractal is being calculated,
         * number of threads to use for the calculation and number of tracks to use for the calculation.
         * @param rootedPolynomial polynomial whose fractal is being calculated
         * @param numOfThreads number of threads to use for the calculation
         * @param numOfTracks number of tracks to use for the calculation
         */
        public MyProducer(ComplexRootedPolynomial rootedPolynomial, int numOfThreads, int numOfTracks) {
            this.numOfThreads = numOfThreads;
            this.numOfTracks = numOfTracks;
            this.rootedPolynomial = rootedPolynomial;
            this.polynomial = rootedPolynomial.toComplexPolynomial();
        }

        /**
         * Calculates the fractal for the given parameters.
         * @param reMin minimum real value
         * @param reMax maximum real value
         * @param imMax maximum imaginary value
         * @param imMin minimum imaginary value
         * @param width width of the image
         * @param height height of the image
         * @param requestNo request number
         * @param observer observer to be notified when the calculation is done
         * @param cancel cancel flag
         */
        @Override
        public void produce(double reMin, double reMax, double imMin, double imMax, int width, int height, long requestNo, IFractalResultObserver observer, AtomicBoolean cancel) {
            System.out.println("Zapocinjem izracun...");
            int m = 16 * 16 * 16;
            short[] data = new short[width * height];
            int brojYPoTraci = height / numOfTracks;

            final BlockingQueue<CalculationJob> queue = new LinkedBlockingQueue<>(numOfThreads);
            Thread[] workers = new Thread[numOfThreads];
            for (int i = 0; i < numOfThreads; i++) {
                workers[i] = new Thread(() -> {
                    while (true) {
                        CalculationJob job;
                        try {
                            job = queue.take();
                            if (job == CalculationJob.NO_JOB) break;
                        } catch (InterruptedException e) {
                            continue;
                        }
                        job.run();
                    }
                });
            }

            for (Thread worker : workers) {
                worker.start();
            }

            for (int i = 0; i < numOfTracks; i++) {
                int yMin = i * brojYPoTraci;
                int yMax = (i + 1) * brojYPoTraci;
                if (i == numOfTracks - 1) {
                    yMax = height - 1;
                }
                CalculationJob job = new CalculationJob(reMin, reMax, imMin, imMax, width, height, yMin, yMax, m, data, cancel, rootedPolynomial.getRoots());
                while (true) {
                    try {
                        queue.put(job);
                        break;
                    } catch (InterruptedException e) {
                    }
                }
            }

            for (int i = 0; i < workers.length; i++) {
                while (true) {
                    try {
                        queue.put(CalculationJob.NO_JOB);
                        break;
                    } catch (InterruptedException e) {
                    }
                }
            }

            for (Thread worker : workers) {
                while (true) {
                    try {
                        worker.join();
                        break;
                    } catch (InterruptedException e) {
                    }
                }
            }
            System.out.println("Izracun gotov...");
            observer.acceptResult(data, polynomial.order(), requestNo);
        }
    }


    /**
     * Helper method for parsing the command line arguments.
     * @param args command line arguments
     * @return parsed arguments as a map
     */
    public static HashMap<String, Integer> parseArguments(String[] args) {
        HashMap<String, Integer> map = new HashMap<>();
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-w") || args[i].startsWith("--workers=")) {
                boolean shortForm = args[i].equals("-w");
                if (shortForm && i + 1 < args.length) {
                    try {
                        map.put("workers", Integer.parseInt(args[i + 1]));
                        i++;
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid number of workers.");
                        exit(1);
                    }
                } else {
                    try {
                        map.put("workers", Integer.parseInt(args[i].substring(10)));
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid number of workers.");
                        exit(1);
                    }
                }
            } else if (args[i].equals("-t") || args[i].startsWith("--tracks=")) {
                boolean shortForm = args[i].equals("-t");
                if (shortForm && i + 1 < args.length) {
                    try {
                        map.put("tracks", Integer.parseInt(args[i + 1]));
                        i++;
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid number of tracks.");
                        exit(1);
                    }
                } else {
                    try {
                        map.put("tracks", Integer.parseInt(args[i].substring(9)));
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid number of tracks.");
                        exit(1);
                    }
                }
            } else {
                System.out.println("Unknown parameter: " + args[i]);
                exit(1);
            }
        }
        if (!map.containsKey("workers")) {
            map.put("workers", Runtime.getRuntime().availableProcessors());
        }
        if (!map.containsKey("tracks")) {
            map.put("tracks", 4 * map.get("workers"));
        }
        return map;
    }
}
