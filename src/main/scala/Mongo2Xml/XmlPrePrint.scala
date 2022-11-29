package Mongo2Xml

import org.mongodb.scala.Document

import java.io.BufferedWriter
import java.nio.file.{Files, Paths}
import scala.util.Try


case class PrePrint (id: String,
                     bvs: String,
                     db: String,
                     instance: String,
                     collection: String,
                     pType: String,
                     la: String,
                     pu: String,
                     ti: String,
                     doi: String,
                     ur: String,
                     fulltext: String,
                     ab: String,
                     au: String,
                     afiliacao_autor: String,
                     entry_date: String,
                     da: String,
                     version_medrxiv_biorxiv: String,
                     license: String,
                     type_document_medrxiv_biorxiv: String,
                     category_medrxiv_biorxiv: String)


class ZBMedPrePrints{


  def toXml(docsMongo: Seq[Document]): Try[Unit] = {
    Try{
      generateXml(docsMongo.map(f => mapElements(f)))
    }
  }

  def mapElements(doc: Document): PrePrint ={
    val id: String = s"ppmedrxiv-${doc.getString("docLink").split("[/.]").reverse.head}"
    val bvs: String = "regional"
    val bd: String = "PREPRINT-MEDRXIV"
    val instance: String = "regional"
    val collection: String = "09-preprints"
    val pType: String = "preprint"
    val la: String = "en"
    val pu: String = "medRxiv"
    val ti: String = doc.getString("title").replace("<", "&lt;").replace(">","&gt;")

    PrePrint(id, bvs, bd, instance, collection, pType, la, pu, ti, "", "", "", "", "", "", "", "", "", "", "", "")
  }

  def generateXml(elements: Seq[PrePrint]): Unit = {

    val newXml: BufferedWriter = Files.newBufferedWriter(Paths.get("/home/oliveirmic/Documents/t.xml"))
    for (element <- elements){
      val xml_ppzbmed = {
<doc>
  <field name={"id"}>{element.id}</field>
  <field name={"bvs"}>{element.bvs}</field>
  <field name={"db"}>{element.db}</field>
  <field name={"instance"}>{element.instance}</field>
  <field name={"collection"}>{element.collection}</field>
  <field name={"typeXml"}>{element.pType}</field>
  <field name={"la"}>{element.la}</field>
  <field name={"pu"}>{element.pu}</field>
  <field name={"ti"}>{element.ti}</field>
  <field name={"doi"}>{element.doi}</field>
  <field name={"ur"}>{element.ur}</field>
  <field name={"fulltext"}>{element.fulltext}</field>
  <field name={"ab"}>{element.ab}</field>
  <field name={"au"}>{element.au}</field>
  <field name={"afiliacao_autor"}>{element.afiliacao_autor}</field>
  <field name={"entry_date"}>{element.entry_date}</field>
  <field name={"da"}>{element.da}</field>
  <field name={"version_medrxiv_biorxiv"}>{element.version_medrxiv_biorxiv}</field>
  <field name={"license"}>{element.license}</field>
  <field name={"type_document_medrxiv_biorxiv"}>{element.type_document_medrxiv_biorxiv}</field>
  <field name={"category_medrxiv_biorxiv"}>{element.category_medrxiv_biorxiv}</field>
</doc>
}

      scala.xml.XML.write( newXml, xml_ppzbmed,  "UTF-8" ,  xmlDecl = true, null)
    }
  }
}