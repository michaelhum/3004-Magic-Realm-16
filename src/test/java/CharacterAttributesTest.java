import ca.carleton.magicrealm.entity.Relationship;
import ca.carleton.magicrealm.entity.character.AbstractCharacter;
import ca.carleton.magicrealm.entity.character.CharacterFactory;
import ca.carleton.magicrealm.entity.character.CharacterType;
import ca.carleton.magicrealm.entity.character.Swordsman;
import ca.carleton.magicrealm.entity.natives.AbstractNative;
import ca.carleton.magicrealm.entity.natives.NativeFaction;
import ca.carleton.magicrealm.entity.natives.NativeFactory;
import ca.carleton.magicrealm.entity.natives.NativeType;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created with IntelliJ IDEA.
 * Date: 17/02/15
 * Time: 4:13 PM
 */
public class CharacterAttributesTest {

    @Test
    public void canCreateCharacterThroughFactory() throws Exception {

        final AbstractCharacter swordsman = CharacterFactory.createCharacter(CharacterType.SWORDSMAN);
        assertThat(swordsman, is(instanceOf(Swordsman.class)));
    }

    @Test
    public void canGetCharacterRelationship() throws Exception {

        final AbstractCharacter amazon = CharacterFactory.createCharacter(CharacterType.AMAZON);
        final AbstractNative knight = NativeFactory.createNative(NativeFaction.BASHKARS, NativeType.KNIGHT);

        assertThat(amazon.getRelationshipWith(knight.getFaction()), is(Relationship.UNFRIENDLY));
    }

}
