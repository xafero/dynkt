
class Greeter(val name: String) { 
   fun greet() { 
      println("Hello, $name")
   }
}

fun main(args: Array<String>) {
   val greeter = Greeter(args[0])
   greeter.greet()
   val map = ctx as MutableMap<String, Greeter>
   map["result"] = greeter
}
