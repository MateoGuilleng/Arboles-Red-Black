package co.udistrital.control;

import co.udistrital.modelo.Nodo;
import co.udistrital.vista.VistaPrincipal;

import javax.swing.SwingUtilities;

/**
 * Lógica completa del Árbol Rojo-Negro.
 * Implementa: inserción, eliminación, búsqueda,
 * rotaciones y los fixups correspondientes.
 * También es el punto de arranque: crea ControlVista y VistaPrincipal.
 */
public class ControlPrincipal {

    // Nodo centinela NIL (hoja negra universal)
    public final Nodo NIL;
    public Nodo raiz;

    public ControlPrincipal() {
        NIL       = new Nodo();          // color NEGRO por defecto
        NIL.izq   = NIL;
        NIL.der   = NIL;
        NIL.padre = NIL;
        raiz      = NIL;

        // Arrancar la capa de vista en el Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            ControlVista cv = new ControlVista(this);
            new VistaPrincipal(cv);
        });
    }

    // ─────────────────────────────────────────────
    //  ROTACIONES
    // ─────────────────────────────────────────────

    /**
     * LEFT-ROTATE(T, x)
     * El hijo derecho y de x sube; x baja a la izquierda de y.
     */
    private void rotarIzquierda(Nodo x) {
        Nodo y = x.der;          // y = hijo derecho de x
        x.der  = y.izq;          // subárbol izq de y → der de x

        if (y.izq != NIL) {
            y.izq.padre = x;
        }

        y.padre = x.padre;       // padre de x → padre de y

        if (x.padre == NIL) {
            raiz = y;
        } else if (x == x.padre.izq) {
            x.padre.izq = y;
        } else {
            x.padre.der = y;
        }

        y.izq   = x;             // x pasa a ser hijo izq de y
        x.padre = y;
    }

    /**
     * RIGHT-ROTATE(T, x)
     * El hijo izquierdo y de x sube; x baja a la derecha de y.
     */
    private void rotarDerecha(Nodo x) {
        Nodo y = x.izq;          // y = hijo izquierdo de x
        x.izq  = y.der;          // subárbol der de y → izq de x

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

    // ─────────────────────────────────────────────
    //  INSERCIÓN
    // ─────────────────────────────────────────────

    /**
     * RB-INSERT(T, z)
     * Inserta el valor en el árbol como nodo rojo y repara.
     * @return false si el valor ya existe (no se insertan duplicados).
     */
    public boolean insertar(int valor) {
        // Verificar duplicado
        if (buscar(valor) != NIL) return false;

        Nodo z = new Nodo(valor);
        z.izq  = NIL;
        z.der  = NIL;

        Nodo y = NIL;
        Nodo x = raiz;

        // Descender hasta la posición correcta (BST)
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
            raiz = z;                  // árbol vacío
        } else if (z.dato < y.dato) {
            y.izq = z;
        } else {
            y.der = z;
        }

        // z ya tiene color ROJO (asignado en constructor)
        insertarFixup(z);
        return true;
    }

    /**
     * RB-INSERT-FIXUP(T, z)
     * Repara violaciones de color tras la inserción.
     */
    private void insertarFixup(Nodo z) {
        while (z.padre.color == Nodo.ROJO) {
            if (z.padre == z.padre.padre.izq) {
                // Padre es hijo IZQUIERDO del abuelo
                Nodo tio = z.padre.padre.der;

                if (tio.color == Nodo.ROJO) {
                    // ── Caso 1: tío ROJO ──
                    z.padre.color        = Nodo.NEGRO;
                    tio.color            = Nodo.NEGRO;
                    z.padre.padre.color  = Nodo.ROJO;
                    z = z.padre.padre;
                } else {
                    if (z == z.padre.der) {
                        // ── Caso 2: tío NEGRO, z es hijo derecho ──
                        z = z.padre;
                        rotarIzquierda(z);
                    }
                    // ── Caso 3: tío NEGRO, z es hijo izquierdo ──
                    z.padre.color       = Nodo.NEGRO;
                    z.padre.padre.color = Nodo.ROJO;
                    rotarDerecha(z.padre.padre);
                }
            } else {
                // Padre es hijo DERECHO del abuelo (casos simétricos)
                Nodo tio = z.padre.padre.izq;

                if (tio.color == Nodo.ROJO) {
                    // Caso 1 simétrico
                    z.padre.color        = Nodo.NEGRO;
                    tio.color            = Nodo.NEGRO;
                    z.padre.padre.color  = Nodo.ROJO;
                    z = z.padre.padre;
                } else {
                    if (z == z.padre.izq) {
                        // Caso 2 simétrico
                        z = z.padre;
                        rotarDerecha(z);
                    }
                    // Caso 3 simétrico
                    z.padre.color       = Nodo.NEGRO;
                    z.padre.padre.color = Nodo.ROJO;
                    rotarIzquierda(z.padre.padre);
                }
            }
        }
        // Caso 0: la raíz siempre es NEGRA
        raiz.color = Nodo.NEGRO;
    }

    // ─────────────────────────────────────────────
    //  ELIMINACIÓN
    // ─────────────────────────────────────────────

    /**
     * Reemplaza el subárbol de u por el de v (transplant).
     */
    private void transplantar(Nodo u, Nodo v) {
        if (u.padre == NIL) {
            raiz = v;
        } else if (u == u.padre.izq) {
            u.padre.izq = v;
        } else {
            u.padre.der = v;
        }
        v.padre = u.padre;   // siempre se actualiza (NIL también tiene padre)
    }

    /**
     * Devuelve el nodo con el valor mínimo en el subárbol de x.
     */
    private Nodo minimo(Nodo x) {
        while (x.izq != NIL) x = x.izq;
        return x;
    }

    /**
     * RB-DELETE(T, z)
     * Elimina el nodo con el valor dado.
     * @return false si el valor no existe.
     */
    public boolean eliminar(int valor) {
        Nodo z = buscar(valor);
        if (z == NIL) return false;

        Nodo y = z;
        boolean yColorOriginal = y.color;
        Nodo x;

        if (z.izq == NIL) {
            // z no tiene hijo izquierdo
            x = z.der;
            transplantar(z, z.der);

        } else if (z.der == NIL) {
            // z no tiene hijo derecho
            x = z.izq;
            transplantar(z, z.izq);

        } else {
            // z tiene dos hijos: sucesor in-order
            y = minimo(z.der);
            yColorOriginal = y.color;
            x = y.der;

            if (y.padre == z) {
                x.padre = y;           // x puede ser NIL; aseguramos padre
            } else {
                transplantar(y, y.der);
                y.der        = z.der;
                y.der.padre  = y;
            }

            transplantar(z, y);
            y.izq        = z.izq;
            y.izq.padre  = y;
            y.color      = z.color;
        }

        // Solo se repara si el nodo eliminado era NEGRO
        if (yColorOriginal == Nodo.NEGRO) {
            eliminarFixup(x);
        }
        return true;
    }

    /**
     * RB-DELETE-FIXUP(T, x)
     * Repara el déficit de negro introducido al eliminar un nodo negro.
     */
    private void eliminarFixup(Nodo x) {
        while (x != raiz && x.color == Nodo.NEGRO) {
            if (x == x.padre.izq) {
                Nodo w = x.padre.der;   // hermano de x

                // Tipo 1: hermano ROJO
                if (w.color == Nodo.ROJO) {
                    w.color        = Nodo.NEGRO;
                    x.padre.color  = Nodo.ROJO;
                    rotarIzquierda(x.padre);
                    w = x.padre.der;
                }

                // Tipo 2: hermano NEGRO con ambos hijos NEGROS
                if (w.izq.color == Nodo.NEGRO && w.der.color == Nodo.NEGRO) {
                    w.color = Nodo.ROJO;
                    x = x.padre;
                } else {
                    // Tipo 3: hermano NEGRO, hijo izq ROJO, hijo der NEGRO
                    if (w.der.color == Nodo.NEGRO) {
                        w.izq.color = Nodo.NEGRO;
                        w.color     = Nodo.ROJO;
                        rotarDerecha(w);
                        w = x.padre.der;
                    }
                    // Tipo 4: hermano NEGRO, hijo der ROJO
                    w.color       = x.padre.color;
                    x.padre.color = Nodo.NEGRO;
                    w.der.color   = Nodo.NEGRO;
                    rotarIzquierda(x.padre);
                    x = raiz;          // termina el bucle
                }
            } else {
                // Casos simétricos (x es hijo derecho)
                Nodo w = x.padre.izq;

                if (w.color == Nodo.ROJO) {
                    w.color        = Nodo.NEGRO;
                    x.padre.color  = Nodo.ROJO;
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
        x.color = Nodo.NEGRO;   // absorbe cualquier remanente de color
    }

    // ─────────────────────────────────────────────
    //  BÚSQUEDA
    // ─────────────────────────────────────────────

    /**
     * Busca un nodo por valor. Devuelve NIL si no existe.
     */
    public Nodo buscar(int valor) {
        Nodo actual = raiz;
        while (actual != NIL) {
            if (valor == actual.dato) return actual;
            actual = (valor < actual.dato) ? actual.izq : actual.der;
        }
        return NIL;
    }

    /**
     * Indica si el árbol está vacío.
     */
    public boolean estaVacio() {
        return raiz == NIL;
    }
}
