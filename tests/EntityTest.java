import org.junit.Test;

import java.lang.reflect.Field;

import static org.junit.Assert.*;

public class EntityTest {

    @org.junit.Test
    public void getIdNumber() throws Exception {
        final Entity TestEntity = new Entity();
        final Field field = TestEntity.getClass().getDeclaredField("idNumber");
        field.setAccessible(true);
        field.set(TestEntity, 12345672);

        final int result = TestEntity.getIdNumber();

        assertEquals("idNumber wasn't retrieved properly", result, 1234562);
    }

    @org.junit.Test
    public void getName() throws Exception {
        final Entity TestEntity = new Entity();
        final Field field = TestEntity.getClass().getDeclaredField("name");
        field.setAccessible(true);
        field.set(TestEntity, "Donald Trump");

        final String result = TestEntity.getName();

        assertEquals("Name wasn't retrieved properly", result, "Donald Trump");
    }

    @org.junit.Test
    public void getAddress() throws Exception {

    }

    @org.junit.Test
    public void getCity() throws Exception {

    }

    @org.junit.Test
    public void getState() throws Exception {

    }

    @org.junit.Test
    public void getZip() throws Exception {

    }

    @org.junit.Test
    public void getStatus() throws Exception {

    }

    @org.junit.Test
    public void setIdNumber() throws Exception {

    }

    @org.junit.Test
    public void setName() throws Exception {

    }

    @org.junit.Test
    public void setAddress() throws Exception {

    }

    @org.junit.Test
    public void setCity() throws Exception {

    }

    @org.junit.Test
    public void setState() throws Exception {

    }

    @org.junit.Test
    public void setZip() throws Exception {

    }

    @org.junit.Test
    public void setStatus() throws Exception {

    }

}