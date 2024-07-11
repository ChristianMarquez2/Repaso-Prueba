import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        // Configuración de la conexión
        String url = "jdbc:mysql://localhost:3306/base_de_datos"; // Cambia esto si tu base de datos tiene otro nombre
        String user = "root";
        String password = ""; // Usa la contraseña adecuada para tu base de datos

        // Intentar conectar a la base de datos
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            if (conn != null) {
                System.out.println("Conexión exitosa a la base de datos.");
            } else {
                System.out.println("No se pudo conectar a la base de datos.");
            }
        } catch (SQLException e) {
            System.out.println("Error al conectar a la base de datos.");
            e.printStackTrace();
        }
    }
}
