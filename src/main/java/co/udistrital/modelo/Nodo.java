package co.udistrital.modelo;

/**
 * Nodo para un Árbol Rojo-Negro.
 * Almacena el dato (entero), color, padre e hijos izquierdo/derecho.
 */
public class Nodo {

    public static final boolean ROJO  = true;
    public static final boolean NEGRO = false;

    public int    dato;
    public boolean color;   // ROJO = true, NEGRO = false
    public Nodo   padre;
    public Nodo   izq;
    public Nodo   der;

    /** Constructor para nodos reales. */
    public Nodo(int dato) {
        this.dato  = dato;
        this.color = ROJO;   // todo nodo nuevo se inserta en rojo
        this.padre = null;
        this.izq   = null;
        this.der   = null;
    }

    /** Constructor para el nodo NIL centinela. */
    public Nodo() {
        this.color = NEGRO;
        this.padre = null;
        this.izq   = null;
        this.der   = null;
    }

    /** Devuelve "R" o "N" según el color del nodo. */
    public String colorStr() {
        return color == ROJO ? "R" : "N";
    }

    @Override
    public String toString() {
        return String.valueOf(dato);
    }
}
