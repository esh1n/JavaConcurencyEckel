package com.esh1n.pechk;

public class InnerClassShowCase {
    public static void main(String[] args) {
        String[] items = new String[]{"one", "two", "trip"};
        IterableArray iterableArray = new IterableArray(items);
        Iterator iterator = iterableArray.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
    }

    public interface Iterator {

        boolean hasNext();

        Object next();
    }

    public static class IterableArray {
        private final Object[] items;

        public IterableArray(Object[] items) {
            this.items = items;
        }

        public Iterator iterator() {
            return new ArrayIterator();
        }

        private class ArrayIterator implements Iterator {

            private int position = 0;

            @Override
            public boolean hasNext() {
                return position < items.length;
            }

            @Override
            public Object next() {
                return items[position++];
            }
        }
    }
}
