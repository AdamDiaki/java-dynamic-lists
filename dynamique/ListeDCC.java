package dynamique;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

/** @author olivier.ploton@univ-tours.fr */
public class ListeDCC<Elem> implements Iterable<Elem> {
    private class Cell {
        Elem elem;
        Cell prev, next;

        // Q1
        private Cell (Elem elem) {
            this.elem = elem;
        }
    }

    private final Cell root;

    // Q2
    ListeDCC() {
        root = new Cell(null);
        root.prev = root.next = root;
    }

    // Q3
    @Override
    public String toString () {
        StringBuilder b = new StringBuilder();
        b.append("root -> ");
        for (Cell c = root.next; c != root; c = c.next) {
            b.append(c.elem);
            b.append(" -> ");
        }
        b.append("root");
        return b.toString();

    }

    // Q4
    void clear () {
        root.prev = root.next = root;
    }

    // Q4
    boolean isEmpty () {
        return (root.next == root);
    }

    // Q5
    int size () {
        int n = 0;
        for (Cell c = root.next; c != root; c = c.next) {
            n++;
        }
        return n;
    }

    // Q6
    private Cell add (Cell prev, Elem elem, Cell next) {
        Cell cell = new Cell(elem);
        cell.prev = prev;
        cell.next = next;
        prev.next = cell;
        next.prev = cell;
        return cell;
    }

    // Q7
    void addFirst (Elem elem) {
        add(root, elem, root.next);
    }

    // Q7
    void addLast (Elem elem) {
        add(root.prev, elem, root);
    }

    // Q8
    @SafeVarargs
    ListeDCC (Elem... elems) {
        this();
        for (Elem elem: elems) {
            addLast(elem);
        }
    }


    // Q9
    private Elem remove (Cell cell) {
        // protection contre la suppression de la racine
        if (cell == root) throw new NoSuchElementException();
        cell.prev.next = cell.next;
        cell.next.prev = cell.prev;
        return cell.elem;
    }

    // Q9
    Elem removeFirst () {
        return remove(root.next);
    }

    // Q9
    Elem removeLast () {
        return remove(root.prev);        
    }
    
    // Q10
    private Cell fromFirst (int i) {
        if (i < 0) throw new IndexOutOfBoundsException();
        Cell cell = root.next;
        for (int k = 0; k < i; k++) {
            if (cell == root) throw new IndexOutOfBoundsException();
            cell = cell.next;
        }
        if (cell == root) throw new IndexOutOfBoundsException();
        return cell;
    }
    
    // Q10
    private Cell fromLast (int i) {
        if (i < 0) throw new IndexOutOfBoundsException();
        Cell cell = root.prev;
        for (int k = 0; k < i; k++) {
            if (cell == root) throw new IndexOutOfBoundsException();
            cell = cell.prev;
        }
        if (cell == root) throw new IndexOutOfBoundsException();
        return cell;
    }
    
    // Q11
    Elem getFirst (int i) {
        return fromFirst(i).elem;
    }
    
    // Q11
    Elem getLast (int i) {
        return fromLast(i).elem;
    }
    
    // Q11
    void setFirst (int i, Elem elem) {
        fromFirst(i).elem = elem;
    }
    
    // Q11
    void setLast (int i, Elem elem) {
        fromLast(i).elem = elem;
    }
    
    // Q12
    void addFirst (int i, Elem elem) {
        Cell cell = (i == 0) ? root : fromFirst(i-1); 
        add(cell, elem, cell.next);
    }
    
    // Q12
    void addLast (int i, Elem elem) {
        Cell cell = (i == 0) ? root : fromLast(i-1); 
        add(cell.prev, elem, cell);
    }
    
    // Q13
    Elem removeFirst (int i) {
        // si i = 0 et liste vide:
        // c'est fromFirst qui lève son exception d'abord,
        // donc exception = IndexOutOfBoundsException
        return remove(fromFirst(i));
    }

    // Q13
    Elem removeLast (int i) {
        // si i = 0 et liste vide:
        // c'est fromFirst qui lève son exception d'abord,
        // donc exception = IndexOutOfBoundsException
        return remove(fromLast(i));
    }
    
    // Q14
    int indexOf (Elem elem) {
        int i = 0;
        for (Cell c = root.next; c != root; c = c.next) {
            if (c.elem.equals(elem)) return i;
            i++;
        }
        return -1;
    }

    // Q14
    int lastIndexOf (Elem elem) {
        int i = 0;
        for (Cell c = root.prev; c != root; c = c.prev) {
            if (c.elem.equals(elem)) return size() - 1 - i;
            i++;
        }
        return -1;
    }
    
    // Q15
    @Override
    public Iterator<Elem> iterator () {
        return new Iterator<Elem>() {
            Cell cell = root.next;
            
            @Override
            public boolean hasNext() {
                return cell != root;
            }

            @Override
            public Elem next() {
                if (cell == root) throw new NoSuchElementException();
                Elem elem = cell.elem;
                cell = cell.next;
                return elem;
            }
        };
    }
    
    // Q15
    Iterable<Elem> inverse () {
        return () -> new Iterator<Elem>() {
            Cell cell = root.prev;
            
            @Override
            public boolean hasNext() {
                return cell != root;
            }

            @Override
            public Elem next() {
                if (cell == root) throw new NoSuchElementException();
                Elem elem = cell.elem;
                cell = cell.prev;
                return elem;
            }
        }; 
    }
        
    // Q16
    static <Elem> Comparator<ListeDCC<Elem>>  comparator(Comparator<Elem> comp) {
        return (listea, listeb) -> {
            ListeDCC<Elem>.Cell roota = listea.root;
            ListeDCC<Elem>.Cell rootb = listeb.root;
            ListeDCC<Elem>.Cell a = roota.next; // a == roota <=> liste a terminée.
            ListeDCC<Elem>.Cell b = rootb.next; // b == rootb <=> liste b terminée.
            for (;;) {
                if (a == roota) {
                    if (b == rootb) {
                        return 0;
                    } else {
                        return -1;
                    }
                } else {
                    if (b == rootb) {
                        return 1;
                    } else {
                        int c = comp.compare(a.elem, b.elem);
                        if (c != 0) {
                            return c;
                        } else {
                            a = a.next;
                            b = b.next;
                        }
                    }                    
                }
            }
        };
    }
}
