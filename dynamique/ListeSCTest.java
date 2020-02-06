package dynamique;

import static org.junit.Assert.*;

import java.util.Comparator;
import java.util.NoSuchElementException;

import org.junit.Test;

/** @author olivier.ploton@univ-tours.fr */
public class ListeSCTest {
    
    @Test
    public void test1() {
        assertEquals("null", new ListeSC<>().toString());
        assertEquals("1 -> null", new ListeSC<>(1).toString());
        assertEquals("1 -> 2 -> null", new ListeSC<>(1, 2).toString());
        assertEquals("1 -> 2 -> 3 -> null", new ListeSC<>(1, 2, 3).toString());
    }

    @Test
    public void test2() {
        ListeSC<Integer> l = new ListeSC<>(1, 2, 3);
        assertEquals("1 -> 2 -> 3 -> null", l.toString());
        assertEquals("99 -> 1 -> 2 -> 3 -> null", new ListeSC<>(99, l).toString());
        assertEquals((Integer)1, l.head());
        assertEquals("2 -> 3 -> null", l.tail().toString());
        l.insérer(44);
        assertEquals("44 -> 1 -> 2 -> 3 -> null", l.toString());
        l.supprimer();
        assertEquals("1 -> 2 -> 3 -> null", l.toString());
        l.supprimer(2);
        assertEquals("3 -> null", l.toString());
        l.supprimer(2); // trop court
        assertEquals("null", l.toString());
    }
    
    @Test
    public void test3() {
        ListeSC<Integer> l = new ListeSC<>(1, 2, 3);
        assertEquals("1 -> 2 -> 3 -> null", l.toString());
        assertEquals(1, (int)l.pop());
        assertEquals("2 -> 3 -> null", l.toString());
        assertEquals(2, (int)l.pop());
        assertEquals("3 -> null", l.toString());
        l.push(4);
        assertEquals("4 -> 3 -> null", l.toString());
        assertEquals(4, (int)l.pop());
        assertEquals("3 -> null", l.toString());
        assertEquals(3, (int)l.pop());
        assertEquals("null", l.toString());
        try {l.pop(); fail();} catch (NoSuchElementException e) {}
    }
    
    @Test
    public void test4() {
        int n = 10;
        Integer[] t = new Integer[n];
        for (int i = 0; i < n; i++) t[i] = i;
        
        ListeSC<Integer> l = new ListeSC<>(t);
        for (int i = 0; i < n; i++) assertEquals((Integer)i, l.elem(i));
        try {l.elem(n); fail();} catch (IndexOutOfBoundsException e) {}
        try {l.elem(-1); fail();} catch (IndexOutOfBoundsException e) {}
        
        for (int i = 0; i <= n; i++) {
            ListeSC<Integer> k = l.suffixe(i);
            assertEquals(n - i, k.size());
            for (int j = 0; j < n - i; j++) assertEquals(i+j, (int)k.elem(j));
        }
        try {l.suffixe(n+1); fail();} catch (IndexOutOfBoundsException e) {}
        try {l.suffixe(-1); fail();} catch (IndexOutOfBoundsException e) {}
        
        for (int i = 0; i <= n; i++) {
            ListeSC<Integer> k = l.préfixe(i);
            assertEquals(i, k.size());
            for (int j = 0; j < i; j++) assertEquals(j, (int)k.elem(j));
        }
        try {l.préfixe(n+1); fail();} catch (IndexOutOfBoundsException e) {}
        try {l.préfixe(-1); fail();} catch (IndexOutOfBoundsException e) {}
        
        for (int i = 0; i < n; i++) {
            assertEquals(i, l.indexOf(i));
            assertEquals(i, l.indice(i));
        }
        assertEquals(-1, l.indexOf(-999));
        try {l.indice(-999); fail();} catch (NoSuchElementException e) {}
        
        int p = 0;
        for (Integer elem: l) assertEquals(p++, (int)elem);
        assertEquals(n, p);
    }

    /** Une abréviation */
    static ListeSC<Integer> L (Integer...integers) {
        return new ListeSC<>(integers);
    }
    
    @Test
    public void test5() {
        Comparator<Integer> compint = (i, j) -> i - j;
        Comparator<ListeSC<Integer>> comp = ListeSC.comparator(compint);
        
        Object[] t = {
                new ListeSC<>(),
                new ListeSC<>(0),
                new ListeSC<>(0, 0),
                new ListeSC<>(0, 1),
                new ListeSC<>(1),
                new ListeSC<>(1, 0),
                new ListeSC<>(1, 0, 0),
                new ListeSC<>(1, 0, 1),
                new ListeSC<>(1, 1),
                new ListeSC<>(1, 1, 0),
                new ListeSC<>(1, 1, 1),
        };
        
        for (int i = 0; i < t.length; i++) {
            @SuppressWarnings("unchecked")
            ListeSC<Integer> li = (ListeSC<Integer>)t[i];
            assertEquals(0, comp.compare(li, li));
        }
        for (int i = 0; i < t.length; i++) for (int j = 0; j < i; j++) {
            @SuppressWarnings("unchecked")
            ListeSC<Integer> li = (ListeSC<Integer>)t[i];
            @SuppressWarnings("unchecked")
            ListeSC<Integer> lj = (ListeSC<Integer>)t[j];
            assertTrue(comp.compare(li, lj) > 0);
            assertTrue(comp.compare(lj, li) < 0);
        }        
    }
    
    @Test
    public void test6() {
        int n = 10;
        
        for (int i = 0; i < n; i++) for (int j = 0; j < n; j++) {
            ListeSC<Integer> a = new ListeSC<>();
            for (int k = i-1; k >= 0; k--) a.insérer(2 * k);
            
            ListeSC<Integer> b = new ListeSC<>();
            for (int k = j-1; k >= 0; k--) b.insérer(2 * k + 1);
            
            ListeSC<Integer> c = a.cat(b);
            assertEquals(i + j, c.size());
            for (int k = 0; k < i; k++) assertEquals(2 * k, (int)c.elem(k));
            for (int k = 0; k < j; k++) assertEquals(2 * k + 1, (int)c.elem(k + i));
        }
    }

    
    @Test
    public void test7() {
        int n = 10;
        
        ListeSC<Integer> a = new ListeSC<>();
        for (int k = n-1; k >= 0; k--) a.insérer(k);
        
        ListeSC<Integer> b = a.inverse();
        assertEquals(n, b.size());
        for (int k = 0; k < n; k++) assertEquals(n - 1 - k, (int)b.elem(k));
        
        ListeSC<Integer> c = a.selection(elem -> elem % 2 == 0);
        assertEquals(n/2, c.size());
        for (int k = 0; k < n/2; k++) assertEquals(2 * k, (int)c.elem(k));
        
        ListeSC<String> d = a.application(elem -> "[" + elem + "]");
        assertEquals(n, d.size());
        for (int k = 0; k < n; k++) assertEquals("[" + k + "]", d.elem(k));
        
        // somme des i pour i = 0..n: donne n(n-1)/2
        assertEquals((n * (n-1)) / 2, (int) a.calcul((x, y) -> x + y));
        assertEquals((n * (n-1)) / 2, (int) a.calcul((x, y) -> x + y), 0);
    }

}
