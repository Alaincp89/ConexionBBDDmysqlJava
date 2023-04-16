package ConexionBBDD_App;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Aplicacion_Consulta {

    public static void main(String[] args) {
        JFrame marco = new Marco_Aplicacion();
        marco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        marco.setVisible(true);


    }
    static class  Marco_Aplicacion extends JFrame{
        public Marco_Aplicacion(){
            setTitle("Consulta BBDD");

            setBounds(500,300,400,400);

            setLayout(new BorderLayout());

            JPanel menus = new JPanel();

            menus.setLayout(new FlowLayout());

            Estudiante = new JComboBox();

            Estudiante.setEditable(false);

            Estudiante.addItem("Estudiantes");

            Nivel = new JComboBox<>();

            Nivel.setEditable(false);

            Nivel.addItem("Niveles");

            resultado = new JTextArea(4,50);

            resultado.setEditable(false);

            menus.add(Estudiante);

            menus.add(Nivel);

            add(menus, BorderLayout.NORTH);

            add(resultado, BorderLayout.CENTER);

            JButton botonConsulta = new JButton("Consulta");

            botonConsulta.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    ejecutaConsulta();
                }
            });

            add(botonConsulta, BorderLayout.SOUTH);

            // ------------- CONEXION BBDD -----------------

            try{
                Class.forName("com.mysql.cj.jdbc.Driver");
                conexion = DriverManager.getConnection(url, login, pwd);
                if (conexion != null) {
                    System.out.println(" - Conexion a la basa de datos " + url + " Exitosa...");

                    // 2. CREAR OBJETO STATEMENT

                    Statement miStatement = conexion.createStatement();

                    // CARGA JCOMBOBOX ESTUDIANTES

                    ResultSet miResultSet = miStatement.executeQuery("SELECT nombre FROM Estudiante");

                    while(miResultSet.next()){
                        Estudiante.addItem(miResultSet.getString(1));
                    }
                    miResultSet.close();

                    // CARGA JCOMBOBOX MATERIAS

                    miResultSet = miStatement.executeQuery("SELECT nivel FROM Estudiante");

                    while(miResultSet.next()){
                        Nivel.addItem(miResultSet.getString(1));
                    }
                    miResultSet.close();

                }


            }catch (ClassNotFoundException a) {
                System.out.println("Driver jbdc no encontrado");
                a.printStackTrace();

            } catch (SQLException b) {
                System.out.println("Error a conectar a la base de datos");
                b.printStackTrace();

            } catch (Exception e) {
                System.out.println("-Error general-");
                e.printStackTrace();

            }

    }

    private void ejecutaConsulta(){

            ResultSet rs = null;

            try{

                String estudiante = (String) Estudiante.getSelectedItem();

                String nivel = (String) Nivel.getSelectedItem();

                if(!estudiante.equals("Estudiantes") && nivel.equals("Niveles")) {

                    sendConsulEstudiante = conexion.prepareStatement(consultaEstudiante);

                    sendConsulEstudiante.setString(1, estudiante);

                    rs = sendConsulEstudiante.executeQuery();


                } else if (estudiante.equals("Estudiantes") && !nivel.equals("Niveles")) {

                    sendConsulNivel = conexion.prepareStatement(consultaNivel);

                    sendConsulNivel.setString(1, nivel);

                    rs = sendConsulNivel.executeQuery();

                    
                }else if (!estudiante.equals("Estudiantes") && !nivel.equals("Niveles")) {

                    sendConsultaTodos = conexion.prepareStatement(consultaTodos);

                    sendConsultaTodos.setString(1, estudiante);

                    sendConsultaTodos.setString(2, nivel);


                    rs = sendConsultaTodos.executeQuery();
                }

                resultado.setText(""); // RESETEA EL CUADRO RESULTADO

                while(rs.next()){

                    resultado.append("Codigo Estudiantil: ");
                    resultado.append(rs.getString( 1));
                    resultado.append("\n");
                    resultado.append("Nombre: ");
                    resultado.append(rs.getString(2));
                    resultado.append("\n");
                    resultado.append("Apellido: ");
                    resultado.append(rs.getString(3));
                    resultado.append("\n");
                    resultado.append("Nivel: ");
                    resultado.append(rs.getString(4));
                    resultado.append("\n");

                }

            }catch (Exception e) {
                System.out.println("-Error general-");
                e.printStackTrace();

            }


    }

        private PreparedStatement sendConsulEstudiante;
        private PreparedStatement sendConsulNivel;
        private PreparedStatement sendConsultaTodos;
        private final String consultaEstudiante = "SELECT ID_ESTUDIANTE, NOMBRE, APELLIDO, NIVEL FROM ESTUDIANTE WHERE NOMBRE=?";
        private final String consultaNivel = "SELECT ID_ESTUDIANTE, NOMBRE, APELLIDO, NIVEL FROM ESTUDIANTE WHERE NIVEL=?";
        private final String consultaTodos = "SELECT ID_ESTUDIANTE, NOMBRE, APELLIDO, NIVEL " +
                "FROM ESTUDIANTE WHERE NOMBRE=? AND NIVEL=?";
        protected JComboBox Estudiante;
        private  JComboBox Nivel;
        private JTextArea resultado;
        private Connection conexion;

        // DATOS DE CONEXION BASE DE DATOS
        private String bd = "evaluacione";
        private String login = "root";
        private String pwd = "210612";
        private String url = "jdbc:mysql://127.0.0.1:3306/"+ bd;
    }


}


