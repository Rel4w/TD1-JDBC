import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class DataRetriever {
    private DBConnection dbConnection;


    public DataRetriever(DBConnection dbConnection) {
        this.dbConnection = dbConnection;
    }


    public DataRetriever() {
        this(new DBConnection());
    }


    public List<Category> getAllCategories() throws SQLException {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT * FROM Product_category ORDER BY id";

        try (Connection conn = dbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Category category = new Category();
                category.setId(rs.getInt("id"));
                category.setName(rs.getString("name"));
                categories.add(category);
            }
        }
        return categories;
    }


    public List<Product> getProductList(int page, int size) throws SQLException {
        List<Product> products = new ArrayList<>();
        int offset = (page - 1) * size;
        String sql = "SELECT * FROM Product ORDER BY id LIMIT ? OFFSET ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, size);
            pstmt.setInt(2, offset);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Product product = new Product();
                product.setId(rs.getInt("id"));
                product.setName(rs.getString("name"));
                product.setPrice(rs.getDouble("price"));
                product.setCreationDatetime(rs.getTimestamp("creation_datetime").toInstant());
                products.add(product);
            }
        }
        return products;
    }


    public List<Product> getProductsByCriteria(String productName, String categoryName,
                                               Instant creationMin, Instant creationMax) throws SQLException {
        List<Product> products = new ArrayList<>();


        StringBuilder sqlBuilder = new StringBuilder("""
            SELECT DISTINCT p.* 
            FROM Product p
            LEFT JOIN Product_category pc ON p.id = pc.product_id
            WHERE 1=1
        """);

        List<Object> params = new ArrayList<>();

        if (productName != null && !productName.trim().isEmpty()) {
            sqlBuilder.append(" AND p.name ILIKE ?");
            params.add("%" + productName.trim() + "%");
        }

        if (categoryName != null && !categoryName.trim().isEmpty()) {
            sqlBuilder.append(" AND pc.name ILIKE ?");
            params.add("%" + categoryName.trim() + "%");
        }

        if (creationMin != null) {
            sqlBuilder.append(" AND p.creation_datetime >= ?");
            params.add(Timestamp.from(creationMin));
        }

        if (creationMax != null) {
            sqlBuilder.append(" AND p.creation_datetime <= ?");
            params.add(Timestamp.from(creationMax));
        }

        sqlBuilder.append(" ORDER BY p.id");

        String sql = sqlBuilder.toString();

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {


            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Product product = new Product();
                product.setId(rs.getInt("id"));
                product.setName(rs.getString("name"));
                product.setPrice(rs.getDouble("price"));
                product.setCreationDatetime(rs.getTimestamp("creation_datetime").toInstant());
                products.add(product);
            }
        }
        return products;
    }


    public List<Product> getProductsByCriteria(String productName, String categoryName,
                                               Instant creationMin, Instant creationMax,
                                               int page, int size) throws SQLException {

        List<Product> allFilteredProducts = getProductsByCriteria(productName, categoryName,
                creationMin, creationMax);


        int startIndex = (page - 1) * size;
        int endIndex = Math.min(startIndex + size, allFilteredProducts.size());

        if (startIndex >= allFilteredProducts.size()) {
            return new ArrayList<>();
        }

        return allFilteredProducts.subList(startIndex, endIndex);
    }


    public void close() {
        if (dbConnection != null) {
            dbConnection.closeConnection();
        }
    }
}
