package dynamique;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;

/** @author olivier.ploton@univ-tours.fr */
public class ListeSC<Elem> extends AbstractCollection<Elem> implements Collection<Elem> { // Q23
    private class Cell {
        final Elem elem;
        final Cell next;
        
        // Q1
        Cell(Elem elem, Cell next) {
            this.elem = elem;
            this.next = next;
        }
    }
    
    /** une liste est simplement sa 1re cellule */
    private Cell root;
    
    // Q2
    private ListeSC (Cell root) {
        this.root = root;
    }
    
    // Q2
    public ListeSC() {
        root = null;
    }
    
    // Q3
    @SafeVarargs
    public ListeSC(Elem... elems) {
        root = null;
        for (int i = elems.length - 1; i >= 0; i--) {
            root = new Cell(elems[i], root);
        }
    }
    
    // Q4
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Cell c = root; c != null; c = c.next) {
            sb.append(c.elem);
            sb.append(" -> ");
        }
        sb.append("null");
        return sb.toString();
    }

    // Q5
    @Override
    public void clear () {
        root = null;
    }

    // Q5
    @Override
    public boolean isEmpty() {
        return root == null;
    }
    
    ////////////////////////////////////////
    
    // Q6. Plusieurs versions, on en choisit une.
    @Override
    public int size() { return size_for(); }
    
    // Q6, version itérative directe
    int size_for () {
        int n = 0;
        for (Cell c = root; c != null; c = c.next) {
            n++;
        }
        return n;
    }
    
    // Q6, version avec itérateur (cf. Q16)
    int size_iter () {
        int n = 0;
        for (@SuppressWarnings("unused") Elem elem: this) n++;
        return n;
    }
    
    // Q6, version récursive    
    int size_rec (Cell cell) {
        return (cell == null) ? 0 : 1 + size_rec(cell.next);
    }
    
    int size_rec () {
        return size_rec(root);
    }

    ////////////////////////////////////////

    // Q7
    ListeSC(Elem head, ListeSC<Elem> tail) {
        root = new Cell(head, tail.root);
    }

    // Q8
    Elem head() {
        if (root == null) throw new NoSuchElementException();
        return root.elem;
    }
    
    // Q8
    ListeSC<Elem> tail() {
        if (root == null) throw new NoSuchElementException();        
        return new ListeSC<>(root.next);
    }

    ////////////////////////////////////////

    // Q9
    void insérer (Elem head) {
        root = new Cell(head, root);
    }    

    // Q10
    void supprimer () {
        supprimer(1);
    }
    
    void supprimer (int n) {
        // astuce: si la liste est trop courte,
        // on aura un accès null.next ou null.elem.
        // On intercepte l'exception correspondante
        // et on la désactive conformément au sujet.
        try {
            for (int i = 0; i < n; i++) {
                root = root.next;
            }
        } catch (NullPointerException e) {
            // rien à faire
        }
    }

    // Q11. En fait, push <=> insérer
    void push (Elem head) {
        root = new Cell(head, root);
    }
    
    // Q11
    Elem pop () {
        if (root == null) throw new NoSuchElementException();
        
        Elem head = root.elem;
        root = root.next;
        return head;
    }

    ////////////////////////////////////////

    // Q12. Plusieurs versions, on en choisit une.
    Elem elem (int n) { return elem_for(n); }
    
    // Q12, version itérative sur l'indice
    Elem elem_for (int n) {
        if (n < 0) throw new IndexOutOfBoundsException(n);
        // astuce: si la liste est trop courte, on aura un accès
        // null.next ou null.elem. On intercepte l'exception correspondante
        // et on la transforme conformément au sujet.
        try {
            Cell cell = root;
            for (int i = 0; i < n; i++) {
                cell = cell.next;
            }
            return cell.elem;
        } catch (NullPointerException e) {
            throw new IndexOutOfBoundsException(n);
        }
    }
    
    // Q12, version avec itérateur sur la liste (cf. Q16)
    Elem elem_iter (int n) {
        if (n < 0) throw new IndexOutOfBoundsException(n);
        // on itère sur la liste, et on compte les numéros
        int i = 0;
        for (Elem elem: this) {
            if (i == n) return elem;
            i++;
        }
        // pas assez d'éléments dans la liste
        throw new IndexOutOfBoundsException(n);
    }
    
    // Q12, version récursive    
    Elem elem_rec (Cell cell, int n) {
        if (cell == null) throw new IndexOutOfBoundsException();
        return (n == 0) ? cell.elem : elem_rec(cell.next, n-1); 
    }
    
    Elem elem_rec (int n) {
        if (n < 0) throw new IndexOutOfBoundsException(n);
        return elem_rec(root, n);
    }

    ////////////////////////////////////////

    // Q13. Plusieurs versions, on en choisit une.
    ListeSC<Elem> suffixe (int n) { return suffixe_for(n); }
    
    // Q13, version itérative sur l'indice
    ListeSC<Elem> suffixe_for (int n) {
        if (n < 0) throw new IndexOutOfBoundsException(n);
        // astuce: si la liste est trop courte,
        // on aura un accès null.next ou null.elem.
        // On intercepte l'exception correspondante
        // et on la transforme conformément au sujet.
        try {
            Cell cell = root;
            for (int i = 0; i < n; i++) {
                cell = cell.next;
            }
            return new ListeSC<>(cell);
        } catch (NullPointerException e) {
            throw new IndexOutOfBoundsException(n);
        }
    }
    
    // Q13, pas de version avec itérateur (on a besoin des cellules)
    
    // Q13, version récursive. cell == null peut être pertinent si n == 0.
    ListeSC<Elem> suffixe_rec (Cell cell, int n) {
        if (n == 0) {
            return new ListeSC<>(cell);
        } else {
            if (cell == null) throw new IndexOutOfBoundsException();
            return suffixe_rec(cell.next, n-1);
        }
    }
    
    ListeSC<Elem> suffixe_rec (int n) {
        if (n < 0) throw new IndexOutOfBoundsException(n);
        return suffixe_rec(root, n);
    }

    ////////////////////////////////////////


    // Q14: seule version raisonnable: récursive
    Cell préfixe (Cell cell, int n) {
        if (n == 0) {
            return null;
        } else {
            if (cell == null) throw new IndexOutOfBoundsException();
            return new Cell(cell.elem, préfixe(cell.next, n-1));
        }        
    }
    
    ListeSC<Elem> préfixe (int n) {
        if (n < 0) throw new IndexOutOfBoundsException(n);
        return new ListeSC<>(préfixe(root, n));
    }

    ////////////////////////////////////////

    // Q15. Plusieurs versions. On en choisit une.
    int indice (Elem elem) { return indice_rec(elem); }  

    // Q15, version itérative directe
    int indice_for (Elem elem) {
        int i = 0;
        for (Cell c = root; c != null; c = c.next) {
            if (c.elem.equals(elem)) return i;
            i++;
        }
        throw new NoSuchElementException();
    }

    // Q15, version avec itérateur sur la liste (cf. Q16)
    int indice_iter (Elem elem) {
        int i = 0;
        for (Elem el: this) {
            if (el.equals(elem)) return i;
            i++;
        }
        throw new NoSuchElementException();
    }

    // Q15, version récursive
    int indice_rec (Elem elem, Cell cell) {
        if (cell == null) {
            throw new NoSuchElementException();
        } else if (cell.elem.equals(elem)) {
            return 0;
        } else {
            return indice_rec(elem, cell.next) + 1;
        }
    }

    int indice_rec (Elem elem) {
        return indice_rec(elem, root);
    }
    
    // Q15. Changement de conventions
    int indexOf (Elem elem) {
        try {
            return indice(elem);
        }
        catch (NoSuchElementException e) {
            return -1;
        }
    }
    
    ////////////////////////////////////////

    // Q16
    @Override
    public Iterator<Elem> iterator() {
        return new Iterator<Elem>() {
            Cell cell = root;
            @Override
            public boolean hasNext() {
                return cell != null;
            }

            @Override
            public Elem next() {
                if (cell == null) throw new NoSuchElementException();
                Elem elem = cell.elem;
                cell = cell.next;
                return elem;
            }
        };
    }
    
    ////////////////////////////////////////

    // Q17
    private static<Elem> int compare (Comparator<Elem> comp, ListeSC<Elem>.Cell a, ListeSC<Elem>.Cell b) {
        if (a == null) {
            if (b == null) {
                return 0;
            } else {
                return -1;
            }
        } else {
            if (b == null) {
                return 1;
            } else {
                int c = comp.compare(a.elem, b.elem);
                if (c != 0) {
                    return c;
                } else {
                    return compare(comp, a.next, b.next);
                }
            }
        }
    }
        
    static<Elem> Comparator<ListeSC<Elem>> comparator(Comparator<Elem> comp) {
        return (a, b) -> compare(comp, a.root, b.root);
    }
    
    ////////////////////////////////////////

    // Q18. Seule version raisonnable: récursive.
    
    Cell cat(Cell a, Cell b) {
        if (a == null) {
            return b;
        } else {
            return new Cell(a.elem, cat(a.next, b));
        }
    }
    
    ListeSC<Elem> cat (ListeSC<Elem> b) {
        ListeSC<Elem> a = this;
        return new ListeSC<>(cat(a.root, b.root));
    }
    
    ////////////////////////////////////////

    // Q19. Plusieurs versions, on en choisit une.
    ListeSC<Elem> inverse () { return inverse_for(); }
    
    // Q19, version itérative directe
    ListeSC<Elem> inverse_for () {
        Cell inv = null;
        for (Cell c = root; c != null; c = c.next) {
            inv = new Cell(c.elem, inv);
        }
        return new ListeSC<>(inv);
    }

    // Q19, version avec itérateur (cf. Q16)
    ListeSC<Elem> inverse_iter () {
        Cell inv = null;
        for (Elem elem: this) {
            inv = new Cell(elem, inv);
        }
        return new ListeSC<>(inv);
    }

    // pour la version récursive, il faut passer l'accumulateur
    // des versions itératives (inv) en paramètre (suite).
    // inverse(début, suite) calcule (début inversé) + suite
    // et il n'y aura plus qu'à demander inverse(liste, vide)
    
    ListeSC<Elem> inverse_rec (Cell début, Cell suite) {
        if (début == null) {
            return new ListeSC<>(suite);
        } else {
            return inverse_rec(début.next, new Cell(début.elem, suite));
        }
    }
    
    ListeSC<Elem> inverse_rec () {
        return inverse_rec(root, null);
    }
    
    ////////////////////////////////////////
    
    // Q20
    Cell selection (Cell cell, Predicate<Elem> p) {
        if (cell == null) {
            return null;
        } else if (p.test(cell.elem)) {
            return new Cell(cell.elem, selection(cell.next, p));
        } else {
            return selection(cell.next, p);
        }
    }
    
    ListeSC<Elem> selection (Predicate<Elem> p) {
        return new ListeSC<>(selection(root, p));
    }
    
    // Q21
    <Result> ListeSC<Result> application (Function<Elem,Result> f) {
        if (isEmpty()) {
            return new ListeSC<>();
        } else {
            Result fhead = f.apply(head());
            ListeSC<Result> ftail = tail().application(f);
            return new ListeSC<Result>(fhead, ftail);
        }
    }
    
    ////////////////////////////////////////

    // Q22. Plusieurs versions, on en choisit une.
    Elem calcul (BinaryOperator<Elem> op) { return calcul_GAUCHE_for(op); }
    
    Elem calcul (BinaryOperator<Elem> op, Elem spécial) {
        return isEmpty() ? spécial : calcul(op);
    }
    
    // Q22, version itérative directe. Associative à GAUCHE 
    Elem calcul_GAUCHE_for (BinaryOperator<Elem> op) {
        if (root == null) throw new IllegalArgumentException();
        Elem accu = root.elem;
        for (Cell c = root.next; c != null; c = c.next) {
            accu = op.apply(accu, c.elem);
        }
        return accu;
    }

    // Q22, version par itérateur (cf. Q16). Associative à GAUCHE 
    Elem calcul_GAUCHE_iter (BinaryOperator<Elem> op) {
        Elem accu = null;
        for (Elem elem: this) {
            if (accu == null) {
                accu = elem;
            } else {
                accu = op.apply(accu, elem);
            }
        }
        if (accu == null) throw new IllegalArgumentException();
        return accu;
    }

    // Q22, version récursive terminale. Associative à GAUCHE
    // On passe l'accumulateur de la version itérative en paramètre
    // Le résultat est accu op calcul(liste de racine cell)
    Elem calcul_GAUCHE_rec (BinaryOperator<Elem> op, Elem accu, Cell cell) {
        if (cell == null) {
            return accu;
        } else {
            return calcul_GAUCHE_rec(op, op.apply(accu, cell.elem), cell.next); 
        }
    }

    Elem calcul_GAUCHE_rec (BinaryOperator<Elem> op) {
        if (root == null) throw new IllegalArgumentException();
        return calcul_GAUCHE_rec(op, root.elem, root.next);
    }

    // Q22, version récursive. Associative à DROITE.
    // Précondition: ne fonctionne que si cell != null
    Elem calcul_DROITE_rec (BinaryOperator<Elem> op, Cell cell) {
        if (cell.next == null) {
            return cell.elem;
        } else {
            return op.apply(cell.elem, calcul_DROITE_rec(op, cell.next)); 
        }
    }

    Elem calcul_DROITE_rec (BinaryOperator<Elem> op) {
        if (root == null) throw new IllegalArgumentException();
        return calcul_DROITE_rec(op, root);
    }

    ////////////////////////////////////////

    // Q23: cf. entête classe au début du fichier.
    
}
