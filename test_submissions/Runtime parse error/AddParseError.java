public class AddParseError {
    public static void main(String[] args) {
        int a = Integer.parseInt("a"); // always fails
        int b = Integer.parseInt(args[1]);
        System.out.println(a + b);
    }
}
