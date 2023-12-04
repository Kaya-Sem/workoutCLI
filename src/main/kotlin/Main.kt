import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import kotlin.system.exitProcess


const val DATABASE_FILE = "workoutCLI_database.db"

fun main(args: Array<String>) {
    createProgramDataFolder()

    if (args.isEmpty()) {
        printUsage()
        exitProcess(1)
    }

    val command = args[0]
    when (command) {
        "-l", "--log" -> println("-l or --log called")
        else -> {
            println("Invalid command. Please try again.")
            printUsage()
        }
    }
}


fun printUsage(){
    println("Usage:")

}

fun createProgramDataFolder() {
    val userHome: Path = Paths.get(System.getProperty("user.home"))
    val programDataFolder: Path = userHome.resolve(".workout-CLI")

    if (Files.exists(programDataFolder) && Files.isDirectory(programDataFolder)) {
        println("Program data folder already exists! fantastic! -> $programDataFolder")
    } else {
        try {
            Files.createDirectories(programDataFolder)
            println("Program data folder created: $programDataFolder")
        } catch (e: Exception) {
            println("Error creating program data folder: ${e.message}")
            exitProcess(1)
        }
    }

    // Check if the database file exists, and create it if it doesn't
    val databaseFile: Path = programDataFolder.resolve(DATABASE_FILE)
    if (!Files.exists(databaseFile)) {
        try {
            createDatabase(databaseFile)
            println("Database file created: $databaseFile")
        } catch (e: Exception) {
            println("Error creating database file: ${e.message}")
            exitProcess(1)
        }
    } else {
        println("Database file already exists: $databaseFile")
    }
}

fun createDatabase(databaseFile: Path) {
    // Establish a connection to the SQLite database
    DriverManager.getConnection("jdbc:sqlite:${databaseFile.toAbsolutePath()}").use { connection ->
        // Create tables and perform any other initial database setup
        connection.createStatement().use { statement ->
            // Create tables or perform any other initial setup queries
            statement.execute("CREATE TABLE IF NOT EXISTS routines (id INTEGER PRIMARY KEY, name TEXT)")
            // Add more table creation or setup queries as needed
        }
    }
}
