public class ArgumentsSum {
  public static void main(String[] args){
    int sum = 0;
    int N = args.length;
    for (int i = 0; i < N; i++){
      sum = sum + Integer.parseInt(args[i]);
    }
    System.out.println(sum);
  }
