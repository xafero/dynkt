import java.io.PrintWriter

fun main(args: Array<String>) {
   val out = ctx["out"] as PrintWriter   
   if (args.size() == 0) {
      out.println("Provide a name")
      return
   }
   out.println("Hello, ${args[0]}!")
}
