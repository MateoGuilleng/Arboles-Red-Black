package co.udistrital.modelo;

public class Nodo {

    public static final boolean ROJO  = true;
    public static final boolean NEGRO = false;

    public int     dato;
    public boolean color;
    public Nodo    padre;
    public Nodo    izq;
    public Nodo    der;

    public Nodo(int dato) {
        this.dato  = dato;
        this.color = ROJO;
        this.padre = null;
        this.izq   = null;
        this.der   = null;
    }

    public Nodo() {
        this.color = NEGRO;
        this.padre = null;
        this.izq   = null;
        this.der   = null;
    }

    public String colorStr() {
        return color == ROJO ? "R" : "N";
    }

    @Override
    public String toString() {
        return String.valueOf(dato);
    }
}
