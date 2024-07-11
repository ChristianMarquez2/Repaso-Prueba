import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Datos extends JFrame {

    // Componentes de la interfaz
    private JTextField tfPlaca, tfMarca, tfCilindraje, tfTipoCombustible, tfColor, tfPropietario;
    private JButton btnIngresar, btnLimpiar, btnBuscar;
    private JLabel placaLabel;
    private JLabel marcaLabel;
    private JLabel cilindrajeLabel;
    private JLabel tipoDeCombustibleLabel;
    private JLabel colorLabel;
    private JLabel propietarioLabel;
    private JLabel DATOSDELVEHICULOLabel;

    // Constructor
    public Datos() {
        setTitle("Registro de Vehículos");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Etiquetas y campos de texto
        String[] labels = {"Placa:", "Marca:", "Cilindraje:", "Tipo de Combustible:", "Color:", "Propietario:"};
        JTextField[] textFields = {tfPlaca = new JTextField(20), tfMarca = new JTextField(20),
                tfCilindraje = new JTextField(20), tfTipoCombustible = new JTextField(20),
                tfColor = new JTextField(20), tfPropietario = new JTextField(20)};

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        for (int i = 0; i < labels.length; i++) {
            add(new JLabel(labels[i]), gbc);
            gbc.gridx = 1;
            add(textFields[i], gbc);
            gbc.gridx = 0;
            gbc.gridy++;
        }

        // Botones
        btnIngresar = new JButton("Ingresar Datos");
        btnLimpiar = new JButton("Limpiar Formulario");
        btnBuscar = new JButton("Buscar Vehículo");

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(btnIngresar, gbc);

        gbc.gridy++;
        add(btnLimpiar, gbc);

        gbc.gridy++;
        add(btnBuscar, gbc);

        // Acción de los botones
        btnIngresar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                insertarDatos();
            }
        });

        btnLimpiar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limpiarFormulario();
            }
        });

        btnBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarVehiculo();
            }
        });
    }

    // Método para insertar datos en la base de datos
    private void insertarDatos() {
        String url = "jdbc:mysql://localhost:3306/base_de_datos";

        String user = "root";
        String password = "";

        String query = "INSERT INTO vehiculos (placa, marca, cilindraje, tipo_combustible, color, propietario) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Validación básica
            if (tfPlaca.getText().trim().isEmpty() || tfMarca.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Placa y Marca son campos obligatorios.");
                return;
            }

            stmt.setString(1, tfPlaca.getText());
            stmt.setString(2, tfMarca.getText());
            stmt.setString(3, tfCilindraje.getText());
            stmt.setString(4, tfTipoCombustible.getText());
            stmt.setString(5, tfColor.getText());
            stmt.setString(6, tfPropietario.getText());

            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Datos ingresados correctamente.");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al ingresar datos: " + e.getMessage());
        }
    }

    // Método para limpiar el formulario
    private void limpiarFormulario() {
        tfPlaca.setText("");
        tfMarca.setText("");
        tfCilindraje.setText("");
        tfTipoCombustible.setText("");
        tfColor.setText("");
        tfPropietario.setText("");
    }

    // Método para buscar un vehículo
    private void buscarVehiculo() {
        String placa = JOptionPane.showInputDialog(this, "Ingrese la placa del vehículo a buscar:");

        if (placa == null || placa.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar una placa.");
            return;
        }

        String url = "jdbc:mysql://localhost:3306/base_de_datos";
        String user = "root";
        String password = "";

        String query = "SELECT * FROM vehiculos WHERE placa = ?";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, placa);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String result = "Placa: " + rs.getString("placa") + "\n" +
                        "Marca: " + rs.getString("marca") + "\n" +
                        "Cilindraje: " + rs.getString("cilindraje") + "\n" +
                        "Tipo de Combustible: " + rs.getString("tipo_combustible") + "\n" +
                        "Color: " + rs.getString("color") + "\n" +
                        "Propietario: " + rs.getString("propietario");

                JOptionPane.showMessageDialog(this, result);
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró ningún vehículo con esa placa.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al buscar vehículo: " + e.getMessage());
        }
    }

    // Método principal
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Datos().setVisible(true));
    }
}
