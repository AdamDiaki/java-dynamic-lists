package dynamique;

import static org.junit.Assert.*;

import java.util.Comparator;
import java.util.NoSuchElementException;

import org.junit.Test;

/** @author olivier.ploton@univ-tours.fr */
public class ListeDCCTest {

    @Test
    public void test1() {
        assertEquals("root -> root", new ListeDCC<>().toString());
        assertEquals("root -> 1 -> root", new ListeDCC<>(1).toString());
        assertEquals("root -> 1 -> 2 -> root", new ListeDCC<>(1, 2).toString());
        assertEquals("root -> 1 -> 2 -> 3 -> root", new ListeDCC<>(1, 2, 3).toString());
    }

    
    @Test
    public void test2() {
        int n = 10;
        ListeDCC<Integer> liste;
        
        liste = new ListeDCC<>();
        for (int i = 0; i < n; i++) liste.addLast(i);
        
        Integer k = 0;
        for (Integer elem: liste) {
            assertEquals(k, elem); 
            assertEquals(k, liste.getFirst(k));
            k++;
        }

        k = n-1;
        for (Integer elem: liste.inverse()) {
            assertEquals(k, elem); 
            assertEquals(k, liste.getFirst(k));
            k--;
        }
        
        liste = new ListeDCC<>();
        for (int i = 0; i < n; i++) liste.addFirst(i);
        
        k = n-1;
        for (Integer elem: liste) {
            assertEquals(k, elem); 
            assertEquals(k, liste.getLast(k));
            k--;
        }
        
        k = 0;
        for (Integer elem: liste.inverse()) {
            assertEquals(k, elem); 
            assertEquals(k, liste.getLast(k));
            k++;
        }
    }
    
    @Test
    public void test3() {
        ListeDCC<Integer> a, b;
        
        a = new ListeDCC<>();
        b = new ListeDCC<>();
        Comparator<ListeDCC<Integer>> comp = ListeDCC.comparator(Comparator.<Integer>naturalOrder());
        
        assertEquals(0, comp.compare(a, b));
        for (int i = 0; i < 10; i++) {
            a.addLast(i);
            b.addLast(i);
            assertEquals(0, comp.compare(a, b));
            assertEquals(0, comp.compare(b, a));
        }
        
        a.setFirst(3, 8);
        b.setFirst(3, 7);
        // maintenant, a > b
        assertEquals( 1, comp.compare(a, b));
        assertEquals(-1, comp.compare(b, a));
        
        a = new ListeDCC<>(-1);
        b = new ListeDCC<>();
        // a > b
        assertEquals( 1, comp.compare(a, b));
        assertEquals(-1, comp.compare(b, a));
        // maintenant, a < b
        for (int i = 0; i < 10; i++) {
            a.addLast(i);
            b.addLast(i);
            assertEquals(-1, comp.compare(a, b));
            assertEquals( 1, comp.compare(b, a));
        }

        a = new ListeDCC<>(-1);
        b = new ListeDCC<>();
        // a > b
        assertEquals( 1, comp.compare(a, b));
        assertEquals(-1, comp.compare(b, a));
        // maintenant, a > b
        for (int i = 0; i < 10; i++) {
            a.addFirst(i);
            b.addFirst(i);
            assertEquals( 1, comp.compare(a, b));
            assertEquals(-1, comp.compare(b, a));
        }
    }
    
    @Test
    public void test4() {
        int n = 10;
        ListeDCC<Integer> liste;
        
        liste = new ListeDCC<Integer>();
        for (int i = 0; i < n; i++) {
            liste.addLast(i);
            assertEquals(i+1, liste.size());
            for (Integer j = 0; j <= i; j++) {
                assertEquals(j, liste.getFirst(j));
                assertEquals(j, liste.getLast(i-j));
            }
            try { liste.getFirst(i+1); fail(); } catch (IndexOutOfBoundsException e) {}
            try { liste.getLast(i+1); fail(); } catch (IndexOutOfBoundsException e) {}            
        }

        liste = new ListeDCC<Integer>();
        for (int i = 0; i < n; i++) {
            liste.addFirst(i);
            assertEquals(i+1, liste.size());
            for (Integer j = 0; j <= i; j++) {
                assertEquals(j, liste.getLast(j));
                assertEquals(j, liste.getFirst(i-j));
            }
            try { liste.getLast(i+1); fail(); } catch (IndexOutOfBoundsException e) {}            
            try { liste.getFirst(i+1); fail(); } catch (IndexOutOfBoundsException e) {}
        }
    }
    
    @Test
    public void test5() {
        ListeDCC<Integer> liste;
        
        liste = new ListeDCC<Integer>();
        try { liste.removeFirst(0); fail(); } catch (IndexOutOfBoundsException e) {}
        try { liste.removeLast(0); fail(); } catch (IndexOutOfBoundsException e) {}
        try { liste.removeFirst(); fail(); } catch (NoSuchElementException e) {}
        try { liste.removeLast(); fail(); } catch (NoSuchElementException e) {}

        liste = new ListeDCC<Integer>(99);
        try { liste.removeFirst(1); fail(); } catch (IndexOutOfBoundsException e) {}
        try { liste.removeLast(1); fail(); } catch (IndexOutOfBoundsException e) {}

        liste = new ListeDCC<Integer>(1, 2);
        try { liste.removeFirst(2); fail(); } catch (IndexOutOfBoundsException e) {}
        try { liste.removeLast(2); fail(); } catch (IndexOutOfBoundsException e) {}

        Integer elem = 1;
        
        liste = new ListeDCC<Integer>(elem);
        assertEquals(elem, liste.removeFirst(0));
        assertTrue(liste.isEmpty());
        
        liste = new ListeDCC<Integer>(elem);
        assertEquals(elem, liste.removeFirst());
        assertTrue(liste.isEmpty());
        
        liste = new ListeDCC<Integer>(elem);
        assertEquals(elem, liste.removeLast(0));
        assertTrue(liste.isEmpty());
        
        liste = new ListeDCC<Integer>(elem);
        assertEquals(elem, liste.removeLast());
        assertTrue(liste.isEmpty());
        
        Integer a = 0, b = 2;
        ListeDCC<Integer> reste = new ListeDCC<Integer>(a, b);

        liste = new ListeDCC<Integer>(a, elem, b);
        assertEquals(elem, liste.removeFirst(1));
        assertEquals(reste.toString(), liste.toString());
        
        liste = new ListeDCC<Integer>(a, elem, b);
        assertEquals(elem, liste.removeLast(1));
        assertEquals(reste.toString(), liste.toString());
    }

    @Test
    public void test6() {
        int n = 10;
        ListeDCC<Integer> liste;
        
        liste = new ListeDCC<Integer>();
        for (int i = 0; i < n; i++) {
            assertEquals(i, liste.size());
            liste.addFirst(i, i * 10);
            assertEquals(i * 10, (int) liste.getFirst(i));
        }

        liste = new ListeDCC<Integer>();
        for (int i = 0; i < n; i++) {
            assertEquals(i, liste.size());
            liste.addLast(i, i * 10);
            assertEquals(i * 10, (int) liste.getLast(i));
        }
    }

    @Test
    public void test7() {
        int n = 5;
        ListeDCC<Integer> liste;
        
        liste = new ListeDCC<Integer>();
        for (int i = 0; i < n; i++) liste.addLast(i * 10);
       
        for (int i = 0; i < n; i++) {
            assertEquals(i, liste.indexOf(i * 10));
            assertEquals(i, liste.lastIndexOf(i * 10));
        }
        
        for (int i = 0; i < n; i++) liste.addLast(i * 10);
        
        for (int i = 0; i < n; i++) {
            assertEquals(i, liste.indexOf(i * 10));
            assertEquals(i+n, liste.lastIndexOf(i * 10));
        }

    }

}
