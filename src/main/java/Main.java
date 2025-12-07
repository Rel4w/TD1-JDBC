import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        DBConnection dbConnection = new DBConnection();
        DataRetriever retriever = new DataRetriever(dbConnection);

        try {
            System.out.println("=== Connexion à la base de données ===");
            System.out.println("URL: " + dbConnection.getUrl());
            System.out.println("Utilisateur: " + dbConnection.getUser());
            System.out.println();


            System.out.println("=== a) Toutes les catégories ===");
            List<Category> categories = retriever.getAllCategories();
            System.out.println("Nombre de catégories trouvées: " + categories.size());
            categories.forEach(System.out::println);
            System.out.println();


            System.out.println("=== b) Pagination simple ===");
            System.out.println("Page 1, taille 3:");
            List<Product> page1 = retriever.getProductList(1, 3);
            page1.forEach(System.out::println);

            System.out.println("\nPage 2, taille 2:");
            List<Product> page2 = retriever.getProductList(2, 2);
            page2.forEach(System.out::println);
            System.out.println();


            System.out.println("=== c) Filtres multi-critères ===");

            System.out.println("\n1. Filtre : nom produit 'Dell'");
            List<Product> dellProducts = retriever.getProductsByCriteria("Dell", null, null, null);
            System.out.println("Résultats: " + dellProducts.size());
            dellProducts.forEach(System.out::println);

            System.out.println("\n2. Filtre : catégorie 'info'");
            List<Product> infoProducts = retriever.getProductsByCriteria(null, "info", null, null);
            System.out.println("Résultats: " + infoProducts.size());
            infoProducts.forEach(System.out::println);

            System.out.println("\n3. Filtre : 'iPhone' et catégorie 'mobile'");
            List<Product> iphoneMobileProducts = retriever.getProductsByCriteria("iPhone", "mobile", null, null);
            System.out.println("Résultats: " + iphoneMobileProducts.size());
            iphoneMobileProducts.forEach(System.out::println);

            System.out.println("\n4. Filtre : date entre 2024-02-01 et 2024-03-01");
            Instant min = LocalDateTime.of(2024, 2, 1, 0, 0).toInstant(ZoneOffset.UTC);
            Instant max = LocalDateTime.of(2024, 3, 1, 23, 59, 59).toInstant(ZoneOffset.UTC);
            List<Product> dateFiltered = retriever.getProductsByCriteria(null, null, min, max);
            System.out.println("Résultats: " + dateFiltered.size());
            dateFiltered.forEach(System.out::println);

            System.out.println("\n5. Filtre : 'Samsung' et catégorie 'bureau'");
            List<Product> samsungProducts = retriever.getProductsByCriteria("Samsung", "bureau", null, null);
            System.out.println("Résultats: " + samsungProducts.size());
            samsungProducts.forEach(System.out::println);

            System.out.println("\n6. Filtre : 'Sony' et catégorie 'informatique'");
            List<Product> sonyProducts = retriever.getProductsByCriteria("Sony", "informatique", null, null);
            System.out.println("Résultats: " + sonyProducts.size());
            sonyProducts.forEach(System.out::println);

            System.out.println("\n7. Filtre : catégorie 'audio' avec dates");
            Instant minAudio = LocalDateTime.of(2024, 1, 1, 0, 0).toInstant(ZoneOffset.UTC);
            Instant maxAudio = LocalDateTime.of(2024, 12, 1, 23, 59, 59).toInstant(ZoneOffset.UTC);
            List<Product> audioProducts = retriever.getProductsByCriteria(null, "audio", minAudio, maxAudio);
            System.out.println("Résultats: " + audioProducts.size());
            audioProducts.forEach(System.out::println);

            System.out.println("\n8. Aucun filtre (tous les produits)");
            List<Product> allProducts = retriever.getProductsByCriteria(null, null, null, null);
            System.out.println("Résultats: " + allProducts.size());
            allProducts.forEach(System.out::println);
            System.out.println();


            System.out.println("=== d) Filtres + pagination ===");

            System.out.println("\n1. Sans filtre, page 1, taille 10:");
            List<Product> paged1 = retriever.getProductsByCriteria(null, null, null, null, 1, 10);
            System.out.println("Résultats page 1: " + paged1.size());
            paged1.forEach(System.out::println);

            System.out.println("\n2. Filtre 'Dell', page 1, taille 5:");
            List<Product> paged2 = retriever.getProductsByCriteria("Dell", null, null, null, 1, 5);
            System.out.println("Résultats: " + paged2.size());
            paged2.forEach(System.out::println);

            System.out.println("\n3. Filtre 'informatique', page 1, taille 10:");
            List<Product> paged3 = retriever.getProductsByCriteria(null, "informatique", null, null, 1, 10);
            System.out.println("Résultats: " + paged3.size());
            paged3.forEach(System.out::println);

        } catch (Exception e) {
            System.err.println("Erreur: " + e.getMessage());
            e.printStackTrace();
        } finally {
            retriever.close();
            System.out.println("\n=== Connexion fermée ===");
        }
    }
}