import org.junit.jupiter.api.Test;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class DataRetrieverTest {

    private DataRetriever retriever = new DataRetriever();

    @Test
    public void testGetAllCategories() throws Exception {
        List<Category> categories = retriever.getAllCategories();
        assertNotNull(categories);
        assertTrue(categories.size() > 0);
    }

    @Test
    public void testGetProductListWithPagination() throws Exception {
        List<Product> page1 = retriever.getProductList(1, 2);
        assertEquals(2, page1.size());
    }

    @Test
    public void testFilterByName() throws Exception {
        List<Product> results = retriever.getProductsByCriteria("Dell", null, null, null);
        assertTrue(results.size() >= 1);
        assertTrue(results.get(0).getName().toLowerCase().contains("dell"));
    }
}
