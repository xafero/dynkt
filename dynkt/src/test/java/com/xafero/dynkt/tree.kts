import java.io.File
 
fun tree(dir: File, indent: String) {
    val files = dir.listFiles()!!
    for (file in files) {
        println(indent + file!!.getName())
        if (file.isDirectory()) {
            tree(file, indent + "  ")
        }
    }
}
 
tree(File("."), "")
