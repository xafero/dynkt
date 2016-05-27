import java.io.PrintWriter

fun main(args: Array<String>) {
   val map = ctx as MutableMap<String, Any>
   val out = map["out"] as PrintWriter   
   if (args.size == 0) {
      out.println("Provide a name")
      return
   }
   out.println("Hello, ${args[0]}!")
}
