import records.Address;
import records.BaseData;
import records.Person;

public class RecordPatternSandbox implements Sandbox {
    @Override
    public void play() {
        Person p = new Person(
                new BaseData("Carl", "Jhonson", 24),
                new Address("Grove Street", 12, 34, "LA12412")
        );
        printInstanceOf(p);
        printSwitch(p);
        printInstanceOf(p.address());
        printSwitch(p.address());
        printInstanceOf(p.baseData());
        printSwitch(p.baseData());
    }

    private void printInstanceOf(Object info) {
        if (info instanceof Address(String s, Integer b, Integer a, String p)) {
            System.out.printf("Address is street: %s %d/%d %s%n", s, b, a, p);
        } else if (info instanceof BaseData(String fistName, String lastName, Integer age)) {
            System.out.printf("First name: %s%nLast name: %s%nAge:%d%n", fistName, lastName, age);
        } else if (info instanceof Person(
                BaseData(String firstName, String lastName, Integer age),
                Address(String street,Integer buildingNum, Integer aptNum, String postalCode))) {
            System.out.printf("Printing full person info%n" +
                    "First name: %s%nLast name: %s%nAge: %d%n" +
                    "Address:%nStreet: %s %d/%d%n%s%n",
                    firstName, lastName, age,
                    street, buildingNum, aptNum, postalCode);
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
                    Address(String street,Integer buildingNum, Integer aptNum, String postalCode)) ->
                    System.out.printf("Printing full person info%n" +
                                    "First name: %s%nLast name: %s%nAge: %d%n" +
                                    "Address:%nStreet: %s %d/%d%n%s%n",
                            firstName, lastName, age,
                            street, buildingNum, aptNum, postalCode);
            default -> {}
        }
    }
}
