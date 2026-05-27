package co.udistrital.control;

import co.udistrital.modelo.Nodo;

public class ControlVista {

    private final ControlPrincipal arbol;

    public ControlVista(ControlPrincipal arbol) {
        this.arbol = arbol;
    }

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

    public Nodo getRaiz() {
        return arbol.raiz;
    }

    public Nodo getNIL() {
        return arbol.NIL;
    }

    public Nodo getNodoBuscado(String entrada) {
        try {
            int valor = Integer.parseInt(entrada.trim());
            return arbol.buscar(valor);
        } catch (NumberFormatException e) {
            return arbol.NIL;
        }
    }
}
