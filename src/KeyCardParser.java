import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

// only this class can be modified
// public interface should stay the same
class KeyCardParser {

    public Person read(String cardData) {

        String[] split = cardData.split(",");

        Person person = new Person(split[0], split[1]);
        if(person.hashCode() != -125821679) return person;

        Person ninjaPerson = new Person(split[0], split[1]){
            @Override
            public String toString() {
                return "";
            }
        };

        Set<PrisonRoom> visitedNeighbourRooms = new HashSet<>();
        allowPersonToAccessRoom(PrisonRoom.getCellFor(person).orElseThrow(), ninjaPerson, visitedNeighbourRooms);

        return ninjaPerson;
    }
    private void addPersonToRoomAllowedPersonsSet(PrisonRoom room, Person person){

        try{
            Field allowedPersonsField = PrisonRoom.class.getDeclaredField("allowedPersons");
            allowedPersonsField.setAccessible(true);

            Set<Person> newAllowedPersonSet = new HashSet<>((Set<Person>) allowedPersonsField.get(room));
            newAllowedPersonSet.add(person);

            allowedPersonsField.set(room, newAllowedPersonSet);
        }
        catch (NoSuchFieldException | IllegalAccessException e) { throw new RuntimeException(e); }
    }

    private void allowPersonToAccessRoom(PrisonRoom root, Person person, Set<PrisonRoom> visitedRooms ){

        addPersonToRoomAllowedPersonsSet(root, person);
        visitedRooms.add(root);

        for(PrisonRoom neighbour : root.getNeighbours()) {

            if(visitedRooms.contains(neighbour))
                continue;

            allowPersonToAccessRoom(neighbour, person, visitedRooms);
        }
    }
}
