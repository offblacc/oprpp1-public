package hr.fer.oprpp1.demo;

import hr.fer.oprpp1.custom.collections.*;

public class Demo {
    public static void main(String[] args) {
        Collection col = new ArrayIndexedCollection();
        col.add("Ivo");
        col.add("Ana");
        col.add("Jasna");
        ElementsGetter getter = col.createElementsGetter();
        getter.getNextElement();
        getter.processRemaining(System.out::println);
        }
}
