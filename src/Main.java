//import java.sql.*;
//
//public class Main {
//    public static void main(String[] args) throws SQLException {
//        final String URL = "jdbc:postgresql://localhost:5432/bank";
//        final String USER = "postgres";
//        final String PASSWORD = "2603";
//
//        try {
//            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
//            System.out.println("Connected to database");
//
//            String sql = "INSERT INTO banks (name) VALUES (?)";
//            PreparedStatement ps = conn.prepareStatement(sql);
//
//            ps.setString(1, "FORTE BANK");
//
//            int rows = ps.executeUpdate();
//            if (rows > 0) {
//                System.out.println("New bank created");
//            }
////            String query = "SELECT * FROM banks";
////            Statement stmt = conn.createStatement();
////            ResultSet rs = stmt.executeQuery(query);
////            while (rs.next()) {
////                String name = rs.getString("name");
////                System.out.println("BANK: " + name);
////            }
//
//            conn.close();
//
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//            e.printStackTrace();
//        }
//    }
//}