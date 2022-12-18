import org.junit.Test;
import records.Address;
import records.BaseData;
import records.Gender;
import records.Person;

public class RecordPatternTest {

    private Person carl = new Person(
            new BaseData("Carl", "Jhonson", 24),
            Gender.MALE,
            new Address("Grove Street", 12, 34, "LA12412"));

    @Test
    public void instanceOf() {
        printInstanceOf(carl);
        printInstanceOf(carl.address());
        printInstanceOf(carl.baseData());
    }

    @Test
    public void instanceOfOld() {
        printInstanceOfOld(carl);
        printInstanceOfOld(carl.address());
        printInstanceOfOld(carl.baseData());
    }

    @Test
    public void instanceOfMixed() {
        printInstanceOfMixed(carl);
        printInstanceOfMixed(carl.address());
        printInstanceOfMixed(carl.baseData());
    }

    @Test
    public void switchOld() {
        printSwitchOld(carl);
        printSwitchOld(carl.address());
        printSwitchOld(carl.baseData());
    }

    @Test
    public void switchNew() {
        printSwitch(carl);
        printSwitch(carl.address());
        printSwitch(carl.baseData());
    }

    @Test
    public void switchMixed() {
        printSwitchMixed(carl);
        printSwitchMixed(carl.address());
        printSwitchMixed(carl.baseData());
        Person child = new Person(
                new BaseData("John", "Carlson", 15),
                Gender.MALE,
                new Address("Grove Street", 12, 34, "LA12412"));
        printSwitchMixed(child.baseData());
    }

    //region Why this is useful
    @Test
    public void whyThisIsUseful() {
        Address familyAddress = new Address("Spooner Street", 31, 1, "QUA-1234");
        Person father = new Person(
                new BaseData("Peter", "Griffin", 45),
                Gender.MALE,
                familyAddress);
        Person mother = new Person(
                new BaseData("Lois", "Griffin", 40),
                Gender.FEMALE,
                familyAddress);
        Person son = new Person(
                new BaseData("Chris", "Griffin", 14),
                Gender.MALE,
                familyAddress);
        Person daughter = new Person(
                new BaseData("Meg", "Griffin", 16),
                Gender.FEMALE,
                familyAddress);
        handlePerson(father);
        handlePerson(mother);
        handlePerson(son);
        handlePerson(daughter);
    }

    private void handlePersonWithoutPattern(Object personData) {
        switch (personData) {
            case Person person
                    when Gender.MALE.equals(person.gender()) && person.baseData().age() < 18
                    -> processBoy(person);
            case Person person
                    when Gender.FEMALE.equals(person.gender()) && person.baseData().age() < 18
                    -> processGirl(person);
            case Person person
                    when Gender.MALE.equals(person.gender())
                    -> processMale(person);
            case Person person
                    when Gender.FEMALE.equals(person.gender())
                    -> processFemale(person);
            default -> {}
        }
    }

    private void handlePerson(Object personData) {
        switch (personData) {
            case Person(BaseData(String f, String l, Integer age), Gender gender, Address address) person
                    when Gender.MALE.equals(gender) && age < 18
                    -> processBoy(person);
            case Person(BaseData(String f, String l, Integer age), Gender gender, Address address) person
                    when Gender.FEMALE.equals(gender) && age < 18
                    -> processGirl(person);
            case Person(BaseData baseData, Gender gender, Address address) person
                    when Gender.MALE.equals(gender)
                    -> processMale(person);
            case Person(BaseData baseData, Gender gender, Address address) person
                    when Gender.FEMALE.equals(gender)
                    -> processFemale(person);
            default -> {}
        }
    }
    //endregion

    //region PrintInstanceOf
    private void printInstanceOfOld(Object info) {
        if (info instanceof Address address) {
            System.out.printf("Address is street: %s %d/%d %s%n",
                    address.street(),
                    address.buildingNumber(),
                    address.aptNumber(),
                    address.postalCode());
        } else if (info instanceof BaseData baseData) {
            System.out.printf("First name: %s%nLast name: %s%nAge:%d%n",
                    baseData.firstName(), baseData.lastName(), baseData.age());
        } else if (info instanceof Person person) {
            System.out.printf("Printing full person info%n" +
                            "First name: %s%nLast name: %s%nAge: %d%n" +
                            "Address:%nStreet: %s %d/%d%n%s%n",
                    person.baseData().firstName(),
                    person.baseData().lastName(),
                    person.baseData().age(),
                    person.address().street(),
                    person.address().buildingNumber(),
                    person.address().aptNumber(),
                    person.address().postalCode());
        }
    }

    private void printInstanceOf(Object info) {
        if (info instanceof Address(String s, Integer b, Integer a, String p)) {
            System.out.printf("Address is street: %s %d/%d %s%n", s, b, a, p);
        } else if (info instanceof BaseData baseData) {
            System.out.printf("First name: %s%nLast name: %s%nAge:%d%n",
                    baseData.firstName(), baseData.lastName(), baseData.age());
        } else if (info instanceof Person(
                BaseData(String firstName, String lastName, Integer age),
                Gender gender,
                Address(String street,Integer buildingNum, Integer aptNum, String postalCode))) {
            System.out.printf("Printing full person info%n" +
                            "First name: %s%nLast name: %s%nAge: %d%n" +
                            "Address:%nStreet: %s %d/%d%n%s%n",
                    firstName, lastName, age,
                    street, buildingNum, aptNum, postalCode);
        }
    }

    private void printInstanceOfMixed(Object info) {
        if (info instanceof Address(String s, Integer b, Integer a, String p)) {
            System.out.printf("Address is street: %s %d/%d %s%n", s, b, a, p);
        } else if (info instanceof BaseData(String fistName, String lastName, Integer age)) {
            System.out.printf("First name: %s%nLast name: %s%nAge:%d%n", fistName, lastName, age);
        } else if (info instanceof Person(
                BaseData(String firstName, String lastName, Integer age),
                Gender gender,
                Address address)) {
            System.out.printf("Printing full person info%n" +
                            "First name: %s%nLast name: %s%nAge: %d%n" +
                            "Address:%nStreet: %s %d/%d%n%s%n",
                    firstName, lastName, age,
                    address.street(),
                    address.buildingNumber(),
                    address.aptNumber(),
                    address.postalCode());
        }
    }
    //endregion

    //region PrintSwitch
    private void printSwitchOld(Object info) {
        switch (info) {
            case Address address ->
                    System.out.printf("Address is street: %s %d/%d %s%n",
                            address.street(),
                            address.buildingNumber(),
                            address.aptNumber(),
                            address.postalCode());
            case BaseData baseData ->
                    System.out.printf("First name: %s%nLast name: %s%nAge:%d%n",
                            baseData.firstName(), baseData.lastName(), baseData.age());
            case Person person ->
                    System.out.printf("Printing full person info%n" +
                                    "First name: %s%nLast name: %s%nAge: %d%n" +
                                    "Address:%nStreet: %s %d/%d%n%s%n",
                            person.baseData().firstName(),
                            person.baseData().lastName(),
                            person.baseData().age(),
                            person.address().street(),
                            person.address().buildingNumber(),
                            person.address().aptNumber(),
                            person.address().postalCode());
            default -> {}
        }
    }

    private void printSwitch(Object info) {
        switch (info) {
            case Address(String s, Integer b, Integer a, String p) ->
                    System.out.printf("Address is street: %s %d/%d %s%n", s, b, a, p);
            case BaseData(String fistName, String lastName, Integer age) ->
                    System.out.printf("First name: %s%nLast name: %s%nAge:%d%n",
                            fistName, lastName, age);
            case Person(
                    BaseData(String firstName, String lastName, Integer age),
                    Gender gender,
                    Address(String street,Integer buildingNum, Integer aptNum, String postalCode)) ->
                    System.out.printf("Printing full person info%n" +
                                    "First name: %s%nLast name: %s%nAge: %d%n" +
                                    "Address:%nStreet: %s %d/%d%n%s%n",
                            firstName, lastName, age,
                            street, buildingNum, aptNum, postalCode);
            default -> {}
        }
    }

    private void printSwitchMixed(Object info) {
        switch (info) {
            case Address address ->
                    System.out.printf("Address is street: %s %d/%d %s%n",
                            address.street(),
                            address.buildingNumber(),
                            address.aptNumber(),
                            address.postalCode());
            case BaseData(String fistName, String lastName, Integer age) baseData
                    when age < 18 -> processChild(baseData);
            case BaseData(String fistName, String lastName, Integer age) ->
                    System.out.printf("First name: %s%nLast name: %s%nAge:%d%n",
                            fistName, lastName, age);
            case Person(
                    BaseData(String firstName, String lastName, Integer age),
                    Gender gender,
                    Address(String street,Integer buildingNum, Integer aptNum, String postalCode)) ->
                    System.out.printf("Printing full person info%n" +
                                    "First name: %s%nLast name: %s%nAge: %d%n" +
                                    "Address:%nStreet: %s %d/%d%n%s%n",
                            firstName, lastName, age,
                            street, buildingNum, aptNum, postalCode);
            default -> {}
        }
    }
    //endregion

    private void processChild(BaseData baseData) {
        System.out.printf("Processing child %s%n", baseData);
    }

    private void processMale(Person male) {
        System.out.printf("Processing male %s%n", male);
    }

    private void processFemale(Person female) {
        System.out.printf("Processing female %s%n", female);
    }

    private void processBoy(Person boy) {
        System.out.printf("Processing boy %s%n", boy);
    }

    private void processGirl(Person girl) {
        System.out.printf("Processing girl %s%n", girl);
    }
}
