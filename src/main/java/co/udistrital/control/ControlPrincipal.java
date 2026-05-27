package co.udistrital.control;

import co.udistrital.modelo.Nodo;
import co.udistrital.vista.VistaPrincipal;

import javax.swing.SwingUtilities;

public class ControlPrincipal {

    public final Nodo NIL;
    public Nodo raiz;

    public ControlPrincipal() {
        NIL       = new Nodo();
        NIL.izq   = NIL;
        NIL.der   = NIL;
        NIL.padre = NIL;
        raiz      = NIL;

        SwingUtilities.invokeLater(() -> {
            ControlVista cv = new ControlVista(this);
            new VistaPrincipal(cv);
        });
    }

    private void rotarIzquierda(Nodo x) {
        Nodo y = x.der;
        x.der  = y.izq;

        if (y.izq != NIL) {
            y.izq.padre = x;
        }

        y.padre = x.padre;

        if (x.padre == NIL) {
            raiz = y;
        } else if (x == x.padre.izq) {
            x.padre.izq = y;
        } else {
            x.padre.der = y;
        }

        y.izq   = x;
        x.padre = y;
    }

    private void rotarDerecha(Nodo x) {
        Nodo y = x.izq;
        x.izq  = y.der;

        if (y.der != NIL) {
            y.der.padre = x;
        }

        y.padre = x.padre;

        if (x.padre == NIL) {
            raiz = y;
        } else if (x == x.padre.der) {
            x.padre.der = y;
        } else {
            x.padre.izq = y;
        }

        y.der   = x;
        x.padre = y;
    }

    public boolean insertar(int valor) {
        if (buscar(valor) != NIL) return false;

        Nodo z = new Nodo(valor);
        z.izq  = NIL;
        z.der  = NIL;

        Nodo y = NIL;
        Nodo x = raiz;

        while (x != NIL) {
            y = x;
            if (z.dato < x.dato) {
                x = x.izq;
            } else {
                x = x.der;
            }
        }

        z.padre = y;

        if (y == NIL) {
            raiz = z;
        } else if (z.dato < y.dato) {
            y.izq = z;
        } else {
            y.der = z;
        }

        insertarFixup(z);
        return true;
    }

    private void insertarFixup(Nodo z) {
        while (z.padre.color == Nodo.ROJO) {
            if (z.padre == z.padre.padre.izq) {
                Nodo tio = z.padre.padre.der;

                if (tio.color == Nodo.ROJO) {
                    z.padre.color       = Nodo.NEGRO;
                    tio.color           = Nodo.NEGRO;
                    z.padre.padre.color = Nodo.ROJO;
                    z = z.padre.padre;
                } else {
                    if (z == z.padre.der) {
                        z = z.padre;
                        rotarIzquierda(z);
                    }
                    z.padre.color       = Nodo.NEGRO;
                    z.padre.padre.color = Nodo.ROJO;
                    rotarDerecha(z.padre.padre);
                }
            } else {
                Nodo tio = z.padre.padre.izq;

                if (tio.color == Nodo.ROJO) {
                    z.padre.color       = Nodo.NEGRO;
                    tio.color           = Nodo.NEGRO;
                    z.padre.padre.color = Nodo.ROJO;
                    z = z.padre.padre;
                } else {
                    if (z == z.padre.izq) {
                        z = z.padre;
                        rotarDerecha(z);
                    }
                    z.padre.color       = Nodo.NEGRO;
                    z.padre.padre.color = Nodo.ROJO;
                    rotarIzquierda(z.padre.padre);
                }
            }
        }
        raiz.color = Nodo.NEGRO;
    }

    private void transplantar(Nodo u, Nodo v) {
        if (u.padre == NIL) {
            raiz = v;
        } else if (u == u.padre.izq) {
            u.padre.izq = v;
        } else {
            u.padre.der = v;
        }
        v.padre = u.padre;
    }

    private Nodo minimo(Nodo x) {
        while (x.izq != NIL) x = x.izq;
        return x;
    }

    public boolean eliminar(int valor) {
        Nodo z = buscar(valor);
        if (z == NIL) return false;

        Nodo y = z;
        boolean yColorOriginal = y.color;
        Nodo x;

        if (z.izq == NIL) {
            x = z.der;
            transplantar(z, z.der);
        } else if (z.der == NIL) {
            x = z.izq;
            transplantar(z, z.izq);
        } else {
            y = minimo(z.der);
            yColorOriginal = y.color;
            x = y.der;

            if (y.padre == z) {
                x.padre = y;
            } else {
                transplantar(y, y.der);
                y.der       = z.der;
                y.der.padre = y;
            }

            transplantar(z, y);
            y.izq       = z.izq;
            y.izq.padre = y;
            y.color     = z.color;
        }

        if (yColorOriginal == Nodo.NEGRO) {
            eliminarFixup(x);
        }
        return true;
    }

    private void eliminarFixup(Nodo x) {
        while (x != raiz && x.color == Nodo.NEGRO) {
            if (x == x.padre.izq) {
                Nodo w = x.padre.der;

                if (w.color == Nodo.ROJO) {
                    w.color       = Nodo.NEGRO;
                    x.padre.color = Nodo.ROJO;
                    rotarIzquierda(x.padre);
                    w = x.padre.der;
                }

                if (w.izq.color == Nodo.NEGRO && w.der.color == Nodo.NEGRO) {
                    w.color = Nodo.ROJO;
                    x = x.padre;
                } else {
                    if (w.der.color == Nodo.NEGRO) {
                        w.izq.color = Nodo.NEGRO;
                        w.color     = Nodo.ROJO;
                        rotarDerecha(w);
                        w = x.padre.der;
                    }
                    w.color       = x.padre.color;
                    x.padre.color = Nodo.NEGRO;
                    w.der.color   = Nodo.NEGRO;
                    rotarIzquierda(x.padre);
                    x = raiz;
                }
            } else {
                Nodo w = x.padre.izq;

                if (w.color == Nodo.ROJO) {
                    w.color       = Nodo.NEGRO;
                    x.padre.color = Nodo.ROJO;
                    rotarDerecha(x.padre);
                    w = x.padre.izq;
                }

                if (w.der.color == Nodo.NEGRO && w.izq.color == Nodo.NEGRO) {
                    w.color = Nodo.ROJO;
                    x = x.padre;
                } else {
                    if (w.izq.color == Nodo.NEGRO) {
                        w.der.color = Nodo.NEGRO;
                        w.color     = Nodo.ROJO;
                        rotarIzquierda(w);
                        w = x.padre.izq;
                    }
                    w.color       = x.padre.color;
                    x.padre.color = Nodo.NEGRO;
                    w.izq.color   = Nodo.NEGRO;
                    rotarDerecha(x.padre);
                    x = raiz;
                }
            }
        }
        x.color = Nodo.NEGRO;
    }

    public Nodo buscar(int valor) {
        Nodo actual = raiz;
        while (actual != NIL) {
            if (valor == actual.dato) return actual;
            actual = (valor < actual.dato) ? actual.izq : actual.der;
        }
        return NIL;
    }

    public boolean estaVacio() {
        return raiz == NIL;
    }
}
