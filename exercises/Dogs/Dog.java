public class Dog {
  public int weight;

  /** Constructor function allow one parameter */
  public Dog (int w) {
    weight = w;
  }

  public void makeNoise() {
    if (weight < 10) {
      System.out.println("yipyipyip");
    }
    else if (weight < 30) {
      System.out.println("bark. bark.");
    }
    else {
      System.out.println("woof!");
    }
  }
}