public class BreakContinue {
  
  public static void windowPosSum(int[] a, int n) {
    for (int i = 0, len = a.length; i < len; i++) {
      // if the number is positive
      if (a[i] >= 0) {
        int sum = a[i];
        for (int j = 1; j <= n; j++) {
            if (i + j >= len) {
              break;
            }
            sum += a[i + j];
        }
        a[i] = sum;
      }
    }    
  }

  public static void main (String[] args) {
    int[] a = {1, 2, -3, 4, 5, 4};
    int n = 3;

    windowPosSum(a, n);

    System.out.println(java.util.Arrays.toString(a));

    // Second Excercies
    String[] b = {"cat", "dog", "laser horse", "ketchup", "horse", "horbse"};

    for (String s : b) {
      System.out.println(s);
    }
  }
}