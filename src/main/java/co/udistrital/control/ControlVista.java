package co.udistrital.control;

import co.udistrital.modelo.Nodo;

/**
 * Puente MVC entre la Vista y el Modelo (ControlPrincipal).
 * La vista solo llama métodos de esta clase; nunca toca el árbol directamente.
 */
public class ControlVista {

    private final ControlPrincipal arbol;

    public ControlVista(ControlPrincipal arbol) {
        this.arbol = arbol;
    }

    // ─── Operaciones expuestas a la Vista ───────────────────────────────────

    /**
     * Inserta un valor en el árbol.
     * @return mensaje de resultado para mostrar al usuario.
     */
    public String insertar(String entrada) {
        try {
            int valor = Integer.parseInt(entrada.trim());
            boolean ok = arbol.insertar(valor);
            return ok
                ? "✔ Nodo " + valor + " insertado correctamente."
                : "⚠ El valor " + valor + " ya existe en el árbol.";
        } catch (NumberFormatException e) {
            return "✘ Entrada inválida. Ingrese un número entero.";
        }
    }

    /**
     * Elimina un valor del árbol.
     * @return mensaje de resultado para mostrar al usuario.
     */
    public String eliminar(String entrada) {
        try {
            int valor = Integer.parseInt(entrada.trim());
            boolean ok = arbol.eliminar(valor);
            return ok
                ? "✔ Nodo " + valor + " eliminado correctamente."
                : "⚠ El valor " + valor + " no existe en el árbol.";
        } catch (NumberFormatException e) {
            return "✘ Entrada inválida. Ingrese un número entero.";
        }
    }

    /**
     * Busca un valor en el árbol.
     * @return mensaje de resultado para mostrar al usuario.
     */
    public String buscar(String entrada) {
        try {
            int valor = Integer.parseInt(entrada.trim());
            Nodo resultado = arbol.buscar(valor);
            if (resultado == arbol.NIL) {
                return "✘ El valor " + valor + " NO se encuentra en el árbol.";
            }
            String color = resultado.color == Nodo.ROJO ? "ROJO" : "NEGRO";
            return "✔ Nodo " + valor + " encontrado — Color: " + color + ".";
        } catch (NumberFormatException e) {
            return "✘ Entrada inválida. Ingrese un número entero.";
        }
    }

    /**
     * Expone la raíz del árbol para que la Vista pueda dibujarlo.
     */
    public Nodo getRaiz() {
        return arbol.raiz;
    }

    /**
     * Expone el nodo NIL centinela para que la Vista sepa cuándo parar.
     */
    public Nodo getNIL() {
        return arbol.NIL;
    }

    /**
     * Devuelve el nodo buscado (o NIL) para que la Vista lo resalte.
     */
    public Nodo getNodoBuscado(String entrada) {
        try {
            int valor = Integer.parseInt(entrada.trim());
            return arbol.buscar(valor);
        } catch (NumberFormatException e) {
            return arbol.NIL;
        }
    }
}
