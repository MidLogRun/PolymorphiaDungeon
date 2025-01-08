package csci.ooad.polymorphia;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RoomKeyTest {
    ArtifactFactory artifactFactory = new ArtifactFactory();

    @Test
    public void testUseKey(){
        RoomKey key = artifactFactory.createRoomKey();
        key.useKey();
        assertTrue(key.isUsed());
    }

}
