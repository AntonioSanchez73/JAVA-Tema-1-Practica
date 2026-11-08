package es.studium;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.BadLocationException;

public class Ventana {

    public Ventana() {

        JFrame ventana 	= new JFrame();
        JPanel panel 	= new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
//        Elementos
        JLabel 						labelRuta 		= new JLabel(		"Ruta de busqueda: (Para ejecutables exe/ejecutable)");
        JTextField 					inputRuta 		= new JTextField(	 200);
        inputRuta.setText("/usr/bin/");
        
        JLabel 						labelExtension 	= new JLabel(		"Extensión que queremos buscar:");
        JTextField 					inputExtension 	= new JTextField(	12); // nº maximo caracteres
        
        JLabel						labelSeleccion	= new JLabel(		"Resultados");
        JButton 					btnBuscar 		= new JButton(		"Buscar");
        
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);

        JScrollPane scroll = new JScrollPane(textArea);
        
        JLabel 						labelMensaje	= new JLabel(		"");
        
//      Tamaño en pantalla
        inputRuta.setPreferredSize(new Dimension(350, 30));
        inputRuta.setMaximumSize(new Dimension(350, 30));
        inputExtension.setPreferredSize(new Dimension(350, 30));
        inputExtension.setMaximumSize(new Dimension(350, 30));
        scroll.setPreferredSize(new Dimension(450, 350));
        scroll.setMaximumSize(new Dimension(450, 350));
        scroll.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        Box boxRuta = Box.createHorizontalBox();
        boxRuta.setAlignmentX(Component.LEFT_ALIGNMENT);
        boxRuta.add(inputRuta);
        
//      textView y button
        Box busqueda = Box.createHorizontalBox();
        busqueda.setAlignmentX(Component.LEFT_ALIGNMENT);
        busqueda.add(inputExtension);
        busqueda.add(Box.createHorizontalStrut(5));
        busqueda.add(btnBuscar);
        
//      TextArea resultados
        Box resultados = Box.createHorizontalBox();
        resultados.add(Box.createHorizontalGlue());
        resultados.add(scroll);
        resultados.add(Box.createHorizontalGlue());
        
//      Elementos del panel principal
        panel.add(labelRuta);
        panel.add(boxRuta);
        
        panel.add(labelExtension);
        panel.add(busqueda);
        
        panel.add(labelSeleccion);
        panel.add(resultados);
        
        panel.add(labelMensaje);
        
        ventana.add(panel);

        ventana.setTitle("Buscador de archivos");
        ventana.setSize(500, 450);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setVisible(true);
        
        
//        btnBuscar
        btnBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	
            	String dir 		 = inputRuta.getText();
            	String extension = inputExtension.getText();
            	
        		ArrayList<File> seleccion = new ArrayList<File>();
        		seleccion = Buscador.buscar(dir, extension, seleccion);

        		textArea.setText("");
        		for (int i = 0; i < seleccion.size(); i++) {
        			textArea.append(seleccion.get(i).getAbsolutePath() + "\n");
        		}
            }
        });
        
//        Click TextArea
        textArea.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

//            	Doble click
                if (e.getClickCount() == 2) {
                    try {
                        int posicion = textArea.viewToModel2D(e.getPoint());
                        int linea 	 = textArea.getLineOfOffset(posicion);
                        int inicio 	 = textArea.getLineStartOffset(linea);
                        int fin 	 = textArea.getLineEndOffset(linea);

                        String ruta = textArea.getText(inicio, fin - inicio).trim();
                        File archivo = new File(ruta);
                        
                        if (archivo.canExecute()) {
                        	ProcessBuilder pb = new ProcessBuilder(ruta);
                            Process proceso =  pb.start();
                            labelMensaje.setText("PID: " + proceso.pid() + " Nombre: " + archivo.getName());
                        } else {
                        	System.out.println("No ejecutable: " + ruta);
                        }
                    } catch (BadLocationException ex) {
                        ex.printStackTrace();
                    } catch (IOException e1) {
                    	
                    	String permisos = "(Permiso denegado)";
                    	
                    	 if (e1.getMessage().contains(permisos)) {
                    	    System.out.println(permisos);
                    	 } else {
                    	    System.out.println("Error: " + e1.getMessage());
                    	 }
					}
                }
            }
        });
    }

}