package co.udistrital.vista;

import co.udistrital.control.ControlVista;
import co.udistrital.modelo.Nodo;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.util.HashMap;
import java.util.Map;

public class VistaPrincipal extends JFrame {

    private static final Color COLOR_FONDO       = new Color(30, 30, 40);
    private static final Color COLOR_PANEL_CTRL  = new Color(40, 40, 55);
    private static final Color COLOR_NODO_ROJO   = new Color(210, 50, 50);
    private static final Color COLOR_NODO_NEGRO  = new Color(30, 30, 30);
    private static final Color COLOR_NODO_RESALT = new Color(255, 200, 0);
    private static final Color COLOR_BORDE_NODO  = new Color(220, 220, 220);
    private static final Color COLOR_ARISTA      = new Color(180, 180, 180);
    private static final Color COLOR_TEXTO_NODO  = Color.WHITE;
    private static final Color COLOR_TEXTO_UI    = new Color(220, 220, 230);
    private static final Color COLOR_BTN_INS     = new Color(60, 140, 60);
    private static final Color COLOR_BTN_ELI     = new Color(160, 50, 50);
    private static final Color COLOR_BTN_BUS     = new Color(50, 100, 180);

    private static final int RADIO_NODO = 22;
    private static final int SEP_V      = 70;

    private final ControlVista controlVista;
    private final PanelArbol   panelArbol;
    private final JTextField   campoEntrada;
    private final JLabel       lblMensaje;

    private Nodo nodoResaltado = null;

    public VistaPrincipal(ControlVista cv) {
        this.controlVista = cv;

        setTitle("Árbol Rojo-Negro — Visualizador");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setMinimumSize(new Dimension(800, 550));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(0, 0));
        getContentPane().setBackground(COLOR_FONDO);

        panelArbol = new PanelArbol();
        JScrollPane scroll = new JScrollPane(panelArbol);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(COLOR_FONDO);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(scroll, BorderLayout.CENTER);

        JPanel panelCtrl = new JPanel(new BorderLayout(10, 6));
        panelCtrl.setBackground(COLOR_PANEL_CTRL);
        panelCtrl.setBorder(new EmptyBorder(12, 18, 12, 18));

        JPanel filaEntrada = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        filaEntrada.setOpaque(false);

        JLabel lblEntrada = new JLabel("Valor:");
        lblEntrada.setForeground(COLOR_TEXTO_UI);
        lblEntrada.setFont(new Font("Segoe UI", Font.BOLD, 14));

        campoEntrada = new JTextField(8);
        campoEntrada.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        campoEntrada.setBackground(new Color(55, 55, 70));
        campoEntrada.setForeground(Color.WHITE);
        campoEntrada.setCaretColor(Color.WHITE);
        campoEntrada.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 100, 130), 1),
                new EmptyBorder(4, 6, 4, 6)));

        JButton btnInsertar = crearBoton("Insertar", COLOR_BTN_INS);
        JButton btnEliminar = crearBoton("Eliminar", COLOR_BTN_ELI);
        JButton btnBuscar   = crearBoton("Buscar",   COLOR_BTN_BUS);
        JButton btnLimpiar  = crearBoton("Limpiar",  new Color(90, 90, 110));

        filaEntrada.add(lblEntrada);
        filaEntrada.add(campoEntrada);
        filaEntrada.add(btnInsertar);
        filaEntrada.add(btnEliminar);
        filaEntrada.add(btnBuscar);
        filaEntrada.add(btnLimpiar);

        lblMensaje = new JLabel(" ", SwingConstants.CENTER);
        lblMensaje.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        lblMensaje.setForeground(new Color(180, 220, 180));

        panelCtrl.add(filaEntrada, BorderLayout.CENTER);
        panelCtrl.add(lblMensaje,  BorderLayout.SOUTH);
        add(panelCtrl, BorderLayout.SOUTH);

        add(crearLeyenda(), BorderLayout.NORTH);

        btnInsertar.addActionListener(e -> accionInsertar());
        btnEliminar.addActionListener(e -> accionEliminar());
        btnBuscar  .addActionListener(e -> accionBuscar());
        btnLimpiar .addActionListener(e -> accionLimpiar());

        campoEntrada.addKeyListener(new KeyAdapter() {
            @Override public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) accionInsertar();
            }
        });

        setVisible(true);
    }

    private void accionInsertar() {
        nodoResaltado = null;
        String msg = controlVista.insertar(campoEntrada.getText());
        mostrarMensaje(msg);
        campoEntrada.setText("");
        panelArbol.repaint();
    }

    private void accionEliminar() {
        nodoResaltado = null;
        String msg = controlVista.eliminar(campoEntrada.getText());
        mostrarMensaje(msg);
        campoEntrada.setText("");
        panelArbol.repaint();
    }

    private void accionBuscar() {
        nodoResaltado = controlVista.getNodoBuscado(campoEntrada.getText());
        String msg = controlVista.buscar(campoEntrada.getText());
        mostrarMensaje(msg);
        panelArbol.repaint();
    }

    private void accionLimpiar() {
        nodoResaltado = null;
        campoEntrada.setText("");
        lblMensaje.setText(" ");
        panelArbol.repaint();
    }

    private void mostrarMensaje(String msg) {
        lblMensaje.setText(msg);
    }

    private JButton crearBoton(String texto, Color fondo) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(fondo);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(7, 16, 7, 16));
        btn.addMouseListener(new MouseAdapter() {
            final Color original = fondo;
            @Override public void mouseEntered(MouseEvent e) { btn.setBackground(original.brighter()); }
            @Override public void mouseExited(MouseEvent e)  { btn.setBackground(original); }
        });
        return btn;
    }

    private JPanel crearLeyenda() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 6));
        p.setBackground(COLOR_PANEL_CTRL);
        p.add(itemLeyenda(COLOR_NODO_ROJO,   "Nodo Rojo"));
        p.add(itemLeyenda(COLOR_NODO_NEGRO,  "Nodo Negro"));
        p.add(itemLeyenda(COLOR_NODO_RESALT, "Nodo Buscado"));
        JLabel titulo = new JLabel("  Árbol Rojo-Negro");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 15));
        titulo.setForeground(COLOR_TEXTO_UI);
        p.add(titulo);
        return p;
    }

    private JPanel itemLeyenda(Color color, String texto) {
        JPanel item = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        item.setOpaque(false);
        JLabel circulo = new JLabel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(color);
                g2.fillOval(0, 2, 14, 14);
                g2.setColor(COLOR_BORDE_NODO);
                g2.drawOval(0, 2, 14, 14);
            }
            @Override public Dimension getPreferredSize() { return new Dimension(16, 18); }
        };
        JLabel lbl = new JLabel(texto);
        lbl.setForeground(COLOR_TEXTO_UI);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        item.add(circulo);
        item.add(lbl);
        return item;
    }

    private class PanelArbol extends JPanel {

        PanelArbol() {
            setBackground(COLOR_FONDO);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            Nodo raiz = controlVista.getRaiz();
            Nodo NIL  = controlVista.getNIL();

            if (raiz == NIL) {
                g2.setColor(new Color(120, 120, 140));
                g2.setFont(new Font("Segoe UI", Font.ITALIC, 16));
                String msg = "El árbol está vacío. Inserta un valor para comenzar.";
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(msg, (getWidth() - fm.stringWidth(msg)) / 2, getHeight() / 2);
                return;
            }

            Map<Nodo, int[]> posiciones = new HashMap<>();
            int[] xCounter = {0};
            calcularPosiciones(raiz, NIL, 0, xCounter, posiciones);

            int maxX       = posiciones.values().stream().mapToInt(p -> p[0]).max().orElse(0);
            int totalAncho = (maxX + 1) * (RADIO_NODO * 2 + 20);
            int offsetX    = Math.max(20, (getWidth() - totalAncho) / 2);
            int offsetY    = RADIO_NODO + 20;

            int totalAlto = (alturaArbol(raiz, NIL) + 1) * SEP_V + RADIO_NODO * 2 + 40;
            setPreferredSize(new Dimension(Math.max(getWidth(), totalAncho + 40), totalAlto));
            revalidate();

            dibujarAristas(g2, raiz, NIL, posiciones, offsetX, offsetY);

            for (Map.Entry<Nodo, int[]> entry : posiciones.entrySet()) {
                Nodo n  = entry.getKey();
                int  px = offsetX + entry.getValue()[0] * (RADIO_NODO * 2 + 20);
                int  py = offsetY + entry.getValue()[1] * SEP_V;
                dibujarNodo(g2, n, px, py);
            }
        }

        private void calcularPosiciones(Nodo n, Nodo NIL, int nivel,
                                        int[] xCounter, Map<Nodo, int[]> pos) {
            if (n == NIL) return;
            calcularPosiciones(n.izq, NIL, nivel + 1, xCounter, pos);
            pos.put(n, new int[]{xCounter[0]++, nivel});
            calcularPosiciones(n.der, NIL, nivel + 1, xCounter, pos);
        }

        private int alturaArbol(Nodo n, Nodo NIL) {
            if (n == NIL) return -1;
            return 1 + Math.max(alturaArbol(n.izq, NIL), alturaArbol(n.der, NIL));
        }

        private void dibujarAristas(Graphics2D g2, Nodo n, Nodo NIL,
                                    Map<Nodo, int[]> pos, int ox, int oy) {
            if (n == NIL) return;

            int[] pPos = pos.get(n);
            int px = ox + pPos[0] * (RADIO_NODO * 2 + 20);
            int py = oy + pPos[1] * SEP_V;

            g2.setColor(COLOR_ARISTA);
            g2.setStroke(new BasicStroke(1.5f));

            if (n.izq != NIL) {
                int[] cPos = pos.get(n.izq);
                int cx = ox + cPos[0] * (RADIO_NODO * 2 + 20);
                int cy = oy + cPos[1] * SEP_V;
                g2.drawLine(px, py, cx, cy);
                dibujarAristas(g2, n.izq, NIL, pos, ox, oy);
            }
            if (n.der != NIL) {
                int[] cPos = pos.get(n.der);
                int cx = ox + cPos[0] * (RADIO_NODO * 2 + 20);
                int cy = oy + cPos[1] * SEP_V;
                g2.drawLine(px, py, cx, cy);
                dibujarAristas(g2, n.der, NIL, pos, ox, oy);
            }
        }

        private void dibujarNodo(Graphics2D g2, Nodo n, int cx, int cy) {
            boolean resaltado = (n == nodoResaltado);

            g2.setColor(new Color(0, 0, 0, 80));
            g2.fill(new Ellipse2D.Double(cx - RADIO_NODO + 3, cy - RADIO_NODO + 3,
                    RADIO_NODO * 2, RADIO_NODO * 2));

            Color relleno = resaltado
                    ? COLOR_NODO_RESALT
                    : (n.color == Nodo.ROJO ? COLOR_NODO_ROJO : COLOR_NODO_NEGRO);
            g2.setColor(relleno);
            g2.fill(new Ellipse2D.Double(cx - RADIO_NODO, cy - RADIO_NODO,
                    RADIO_NODO * 2, RADIO_NODO * 2));

            g2.setColor(resaltado ? Color.ORANGE : COLOR_BORDE_NODO);
            g2.setStroke(new BasicStroke(resaltado ? 2.5f : 1.5f));
            g2.draw(new Ellipse2D.Double(cx - RADIO_NODO, cy - RADIO_NODO,
                    RADIO_NODO * 2, RADIO_NODO * 2));

            g2.setColor(resaltado ? Color.BLACK : COLOR_TEXTO_NODO);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 13));
            FontMetrics fm = g2.getFontMetrics();
            String txt = String.valueOf(n.dato);
            g2.drawString(txt, cx - fm.stringWidth(txt) / 2, cy + fm.getAscent() / 2 - 1);
        }
    }
}
