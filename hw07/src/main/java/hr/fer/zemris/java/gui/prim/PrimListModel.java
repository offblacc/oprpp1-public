package hr.fer.zemris.java.gui.prim;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that represents a list model for prime numbers.
 *
 * @author offblacc
 */
public class PrimListModel implements ListModel<Integer> {
    /**
     * List of the primes.
     */
    private List<Integer> list = new ArrayList<>();

    /**
     * List of the listeners.
     */
    private List<ListDataListener> listeners = new ArrayList<>();

    /**
     * Constructor that creates a new PrimListModel object.
     */
    public PrimListModel() {
        list.add(1);
    }

    /**
     * Method that adds the next prime number to the list, notifying all the listeners.
     */
    public void next() {
        int next = list.size() == 0 ? 1 : list.get(list.size() - 1) + 1;
        next = nextPrime(next);
        list.add(next);
        ListDataEvent event = new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, list.size() - 1, list.size() - 1);
        listeners.forEach(listener -> listener.intervalAdded(event));
    }

    /**
     * Method that returns the next prime number.
     * @param n current prime
     * @return next prime
     */
    public static int nextPrime(int n) {
        if (n <= 1) return 2;
        int prime = n;
        boolean found = false;
        while (!found) {
            prime++;
            found = true;
            for (int i = 2; i <= Math.sqrt(prime); i++) {
                if (prime % i == 0) {
                    found = false;
                    break;
                }
            }
        }
        return prime;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getSize() {
        return list.size();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getElementAt(int index) {
        return list.get(index);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addListDataListener(ListDataListener l) {
        listeners.add(l);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeListDataListener(ListDataListener l) {
        listeners.remove(l);
    }
}