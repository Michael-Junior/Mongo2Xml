package Mongo2Xml

import org.mongodb.scala.Document

import java.io.BufferedWriter
import java.nio.file.{Files, Paths}
import scala.util.{Failure, Success, Try}
import scala.xml.{Elem, PrettyPrinter, XML}


case class PrePrint (id: String,
                     db: String,
                     instance: String,
                     collection: String,
                     pType: String,
                     //la: String,                         //Não existe o dado
                     pu: String,
                     ti: String,
                     doi: String,
                     ur: String,
                     urPdf: String,                        //Verificar nome desse campo
                     fulltext: String,
                     ab: String,
                     au: String,
                     //afiliacaoAutor: String,             //Não existe o dado
                     entryDate: String,
                     da: String)
                     //versionMedrxivBiorxiv: String,      //Não existe o dado
                     // license: String,                   //Não existe o dado
                     //typeDocumentMedrxivBiorxiv: String, //Não existe o dado
                     //categoryMedrxivBiorxiv: String)     //Marcelo Verificando


class ZBMedPrePrints{

  def toXml(docsMongo: Seq[Document], pathOut: String): Try[Unit] = {
    Try{
      generateXml(docsMongo.map(f => mapElements(f)), pathOut) match {
        case Success(_) => println()
        case Failure(e) => println(e)
      }
    }
  }

  def mapElements(doc: Document): PrePrint ={

    val id: String = s"ppzbmed-${doc.getString("docLink").split("[/.]").reverse.head}"
    val bd: String = "PREPRINT-ZBMED"
    val instance: String = "regional"
    val collection: String = "09-preprints"
    val typeTmp: String = "preprint"
    val pu: String = doc.getString("source")
    val ti: String = doc.getString("title").replace("<", "&lt;").replace(">","&gt;")
    val doi: String = doc.getString("id")
    val link: String = doc.getString("link")
    val linkPdf: String = doc.getString("pdfLink")
    val fullText: String = if (link.nonEmpty) "1" else ""
    val ab: String = doc.getString("abstract").replace("<", "&lt;").replace(">","&gt;")
    val au: Seq[String] = Seq("")
    val entryDate: String = doc.getString("date").split("T").head.replace("-", "")
    val da: String = doc.getString("date").split("T").head.replace("-", "").substring(0,6)

    PrePrint(id, bd, instance, collection, typeTmp, pu, ti, doi, link, linkPdf, fullText, ab, "", entryDate, da)
  }

  def generateXml(elements: Seq[PrePrint], pathOut: String): Try[Unit] = {
    Try{
      val xmlPath: BufferedWriter = Files.newBufferedWriter(Paths.get(pathOut))
      val printer = new PrettyPrinter(80, 2)
      val xmlFormat =
        <add>
          {elements.map(f => docToElem(f))}
        </add>

      XML.write(xmlPath, XML.loadString(printer.format(xmlFormat)), "UTF-8", xmlDecl = true, null)
      xmlPath.flush()
      xmlPath.close()
    }
  }

  def docToElem(fields: PrePrint): Elem ={

  <doc>
    <field name={"id"}>{fields.id}</field>
    <field name={"db"}>{fields.db}</field>
    <field name={"instance"}>{fields.instance}</field>
    <field name={"collection"}>{fields.collection}</field>
    <field name={"type"}>{fields.pType}</field>
    <field name={"pu"}>{fields.pu}</field>
    <field name={"ti"}>{fields.ti}</field>
    <field name={"doi"}>{fields.doi}</field>
    <field name={"ur"}>{fields.ur}</field>
    <field name={"ur_pdf"}>{fields.urPdf}</field>
    <field name={"fulltext"}>{fields.fulltext}</field>
    <field name={"ab"}>{fields.ab}</field>
    <field name={"au"}>{fields.au}</field>
    <field name={"entry_date"}>{fields.entryDate}</field>
    <field name={"da"}>{fields.da}</field>
  </doc>
  }
}