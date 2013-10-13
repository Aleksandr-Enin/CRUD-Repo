import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: Tector
 * Date: 13.10.13
 * Time: 17:05
 * To change this template use File | Settings | File Templates.
 */
public class DatabaseTest {
    @Test
    public void testAdd() throws Exception {
        Database database = new Database("Database", "Database1", "mapOffset");
        assert "Key already exists".equals(database.add("key", "value"));
        assert "Key already exists".equals(database.add("anotherKey", "value"));
        System.out.println(database.read("key"));
    }

    @Test
    public void test() throws Exception
    {
        Database database = new Database("IMDB", "test", "offset");
    }

    @Test
    public void testRead() throws Exception {
        Database database = new Database("Database", "Database1", "mapOffset");
        database.add("key", "value");
        database.add("anotherKey", "value");
        assert "".equals(database.read("anotherKey"));
        assert "Key doesn't exist".equals(database.read("qwerty"));
    }

    @Test
    public void testUpdate() throws Exception {

    }

    @Test
    public void testDelete() throws Exception {
        Database database = new Database("Database", "Database1", "mapOffset");
        database.add("key", "value");
        assert "Successfully deleted".equals(database.delete("key"));
        assert "Key doesn't exist".equals(database.read("key"));
    }

    @Test
    public void testExit() throws Exception {

    }
}
