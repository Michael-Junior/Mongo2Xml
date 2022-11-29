package Mongo2Xml

import org.mongodb.scala.Document
import java.util.Calendar
import scala.util.{Failure, Success, Try}


case class Mongo2Xml_Parameters(xmlDir: String,
                                database: String,
                                collection: String,
                                host: Option[String],
                                port: Option[Int],
                                user: Option[String],
                                password: Option[String])

class Mongo2Xml {

  def exportXml(parameters: Mongo2Xml_Parameters): Try[Unit] = {
    Try {
      val mExport: MongoExport = new MongoExport(parameters.database, parameters.collection, parameters.host, parameters.port)
      val docsMongo: Seq[Document] = mExport.findAll

      new ZBMedPrePrints().toXml(docsMongo) match {
        case Success(_) => println(s"\nFILE GENERATED SUCCESSFULLY IN: ${parameters.xmlDir}")
        case Failure(e) => println(s"\nFAILURE TO GENERATE FILE: $e")
      }
    }
  }
}

object Mongo2Xml {
  private def usage(): Unit = {
    System.err.println("-database=<name>   - MongoDB database name")
    System.err.println("-collection=<name> - MongoDB database collection name")
    System.err.println("-xmlDir=<path>     - XML file output directory")
    System.err.println("[-host=<name>]     - MongoDB server name. Default value is 'localhost'")
    System.err.println("[-port=<number>]   - MongoDB server port number. Default value is 27017")
    System.err.println("[-user=<name>])    - MongoDB user name")
    System.err.println("[-password=<pwd>]  - MongoDB user password")
    System.exit(1)
  }

  def main(args: Array[String]): Unit = {
    if (args.length < 3) usage()

    val parameters: Map[String, String] = args.foldLeft[Map[String, String]](Map()) {
      case (map, par) =>
        val split = par.split(" *= *", 2)
        if (split.size == 1) map + ((split(0).substring(2), ""))
        else map + (split(0).substring(1) -> split(1))
    }

    if (!Set("xmlDir", "database", "collection").forall(parameters.contains)) usage()

    val xmlDir: String = parameters("xmlDir")
    val database: String = parameters("database")
    val collection: String = parameters("collection")

    val host: Option[String] = parameters.get("host")
    val port: Option[Int] = parameters.get("port").flatMap(_.toIntOption)
    val user: Option[String] = parameters.get("user")
    val password: Option[String] = parameters.get("password")

    val params: Mongo2Xml_Parameters = Mongo2Xml_Parameters(xmlDir, database, collection, host, port, user, password)
    val time1: Long = Calendar.getInstance().getTimeInMillis

    (new Mongo2Xml).exportXml(params) match {
      case Success(_) =>
        println("Successful!")
        val time2: Long = Calendar.getInstance().getTime.getTime
        println(s"Diff time=${time2 - time1}ms\n")
        System.exit(0)
      case Failure(exception) =>
        println(s"Error: ${exception.toString}\n")
        System.exit(1)
    }
  }
}