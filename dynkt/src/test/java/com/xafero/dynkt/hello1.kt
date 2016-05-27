import java.io.*

fun main(args: Array<String>) {
	val map = ctx as MutableMap<String, Any>
	val out = map["out"] as PrintWriter
	out.println("Hello, World!")
}
