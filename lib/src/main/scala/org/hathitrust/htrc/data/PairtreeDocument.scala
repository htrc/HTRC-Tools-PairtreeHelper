package org.hathitrust.htrc.data

import java.io.File

import gov.loc.repository.pairtree.Pairtree
import org.hathitrust.htrc.data.exceptions.InvalidPairtreePathException

import scala.util.{Failure, Success, Try}
import scala.util.matching.Regex

object PairtreeDocument {
  protected val pairtreeFilePartsRegex: Regex = """([^/]+)/pairtree_root/.+/([^/]+)/[^/]+$""".r.unanchored
  protected val pairtree: Pairtree = new Pairtree()

  /**
    * Parses an HTRC pairtree file path into a `PairtreeDocument` that can be used
    * to extract metadata about the document
    *
    * @param filePath The pairtree file path
    * @return The `PairtreeDocument` wrapped in a `Try`
    */
  def from(filePath: String): Try[PairtreeDocument] = filePath match {
    case pairtreeFilePartsRegex(libId, cleanIdPart) =>
      val uncleanIdPart = pairtree.uncleanId(cleanIdPart)
      val uncleanId = s"$libId.$uncleanIdPart"
      Success(PairtreeDocument(HtrcVolumeId(uncleanId)))

    case _ => Failure(InvalidPairtreePathException(filePath))
  }

  /**
    * Parses an HTRC pairtree file into a `PairtreeDocument` that can be used
    * to extract metadata about the document
    *
    * @param file The pairtree file
    * @return The `PairtreeDocument` wrapped in a `Try`
    */
  def from(file: File): Try[PairtreeDocument] = Try(file.getCanonicalPath).flatMap(from)
}

case class PairtreeDocument(volumeId: HtrcVolumeId) {
  import PairtreeDocument._

  protected def ppath: String = pairtree.mapToPPath(volumeId.parts._2)

  /**
    * Returns the root folder for the document
    *
    * @return The document folder path
    */
  def rootPath: String = {
    val (libId, cleanIdPart) = volumeId.partsClean
    s"$libId/pairtree_root/$ppath/$cleanIdPart"
  }

  /**
    * Convenience method for retrieving the full document root path prefix.
    *
    * @param pairtreeRoot The path to the pairtree root
    * @return The full document root path prefix
    */
  def rootPath(pairtreeRoot: String): String = new File(pairtreeRoot, rootPath).toString

  /**
    * Returns the document path prefix for this pairtree document; for example,
    * a volume with ID mdp.39015063051745 would generate the document path prefix
    * mdp/pairtree_root/39/01/50/63/05/17/45/39015063051745/39015063051745
    * By appending ".zip" or ".mets.xml" to this path you can point to the pairtree volume ZIP
    * or the volume METS metadata file, as desired.
    *
    * @return The document path
    */
  def pathPrefix: String = {
    val (libId, cleanIdPart) = volumeId.partsClean
    s"$libId/pairtree_root/$ppath/$cleanIdPart/$cleanIdPart"
  }

  /**
    * Convenience method for retrieving the full document path prefix.
    *
    * @param pairtreeRoot The path to the pairtree root
    * @return The full document path prefix
    */
  def pathPrefix(pairtreeRoot: String): String = new File(pairtreeRoot, pathPrefix).toString

  /**
    * Convenience method for quickly getting the path to the ZIP file.
    *
    * @return The relative path to the ZIP file
    */
  def zipPath: String = s"$pathPrefix.zip"

  /**
    * Convenience method for retrieving the full ZIP volume path prefix.
    *
    * @param pairtreeRoot The path to the pairtree root
    * @return The full ZIP volume path prefix
    */
  def zipPath(pairtreeRoot: String): String = new File(pairtreeRoot, zipPath).toString

  /**
    * Convenience method for quickly getting the path to the METS XML file.
    *
    * @return The relative path to the METS XML file
    */
  def metsPath: String = s"$pathPrefix.mets.xml"

  /**
    * Convenience method for retrieving the full METS XML path prefix.
    *
    * @param pairtreeRoot The path to the pairtree root
    * @return The full METS XML path prefix
    */
  def metsPath(pairtreeRoot: String): String = new File(pairtreeRoot, metsPath).toString

  /**
    * Convenience method for quickly getting the path to the JSON metadata file.
    *
    * @return The relative path to the JSON metadata file
    */
  def jsonMetadataPath: String = s"$pathPrefix.json"

  /**
    * Convenience method for retrieving the full JSON metadata path prefix.
    *
    * @param pairtreeRoot The path to the JSON metadata root
    * @return The full JSON metadata path prefix
    */
  def jsonMetadataPath(pairtreeRoot: String): String = new File(pairtreeRoot, jsonMetadataPath).toString

  /**
    * Convenience method for quickly getting the path to the EF file.
    *
    * @return The relative path to the EF file
    */
  def extractedFeaturesPath: String = {
    val (libId, cleanIdPart) = volumeId.partsClean
    s"$libId/pairtree_root/$ppath/$cleanIdPart/$libId.$cleanIdPart.json.bz2"
  }

  /**
    * Convenience method for retrieving the full EF path prefix.
    *
    * @param pairtreeRoot The path to the pairtree root
    * @return The full EF path prefix
    */
  def extractedFeaturesPath(pairtreeRoot: String): String = new File(pairtreeRoot, extractedFeaturesPath).toString
}