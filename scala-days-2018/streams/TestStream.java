import java.util.Arrays;

public class TestStream {

 static int[] array = new int[] {
  1,
  2,
  3
 };

 static final int N = 20;
 static final double EMPLOYMENT_RATIO = 0.5;
 static final int MAX_AGE = 100;
 static final int MAX_SALARY = 200_000;
 /**
  * @param args the command line arguments
  */
 public static void main(String[] args) {

  // Create data set.
  Person[] persons = new Person[N];
  for (int k = 0; k < N; ++k) {
   persons[k] = new Person(Math.random() > EMPLOYMENT_RATIO ? Employment.EMPLOYED : Employment.UNEMPLOYED, (int)(Math.random() * MAX_SALARY), (int)(Math.random() * MAX_AGE));
  }
  long sum = 0;
  for (int j = 1; j < 21; j++) {
   long time = System.currentTimeMillis();
   for (int i = 0; i < 1000000; ++i) {
    getValue(persons);
   }
   long currentTime = (System.currentTimeMillis() - time);
   sum += currentTime;
   System.out.println("Iteration " + j + " finished in " + currentTime + " milliseconds.");
  }
  System.out.println("TOTAL time: " + sum);
 }

 public static double getValue2() {
  int[] a = array;
  int i = 0;
  do {
   a[i] = 0;
  } while (i++ < a.length);
  return 1.0;
 }

 public enum Employment {
  EMPLOYED,
  UNEMPLOYED
 }

 public static class Person {

  public Person(Employment employment, int height, int age) {
   this.employment = employment;
   this.salary = height;
   this.age = age;
  }

  private Employment employment;
  private int age;
  private int salary;

  public int getSalary() {
   return salary;
  }

  public int getAge() {
   return age;
  }

  public Employment getEmployment() {
   return employment;
  }
 }


 public static double getValue(Person[] persons) {
  return Arrays.stream(persons)
   .filter(p -> p.getEmployment() == Employment.EMPLOYED)
   .filter(p -> p.getSalary() > 100_000)
   .mapToInt(Person::getAge)
   .filter(age -> age > 40)
   .average()
   .getAsDouble();
 }
}
