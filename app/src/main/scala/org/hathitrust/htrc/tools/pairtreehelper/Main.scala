package org.hathitrust.htrc.tools.pairtreehelper

import java.io.File

import org.hathitrust.htrc.data.{HtrcVolumeId, PairtreeDocument}
import org.rogach.scallop.ScallopOption

import scala.io.StdIn
import scala.language.reflectiveCalls
import scala.util.{Failure, Success}

object Main {
  val appName: String = "pairtree-helper"

  def main(args: Array[String]): Unit = {
    val conf = new Conf(args)

    conf.subcommand match {
      case Some(conf.cleanCmd) =>
        for (uncleanId <- input(conf.cleanCmd.uncleanIds)) {
          HtrcVolumeId.parseUnclean(uncleanId) match {
            case Success(htid) => Console.println(htid.cleanId)
            case Failure(e) => Console.err.println(e.toString)
          }
        }

      case Some(conf.uncleanCmd) =>
        for (cleanId <- input(conf.uncleanCmd.cleanIds)) {
          HtrcVolumeId.parseClean(cleanId) match {
            case Success(htid) => Console.println(htid.uncleanId)
            case Failure(e) => Console.err.println(e.toString)
          }
        }

      case Some(conf.clean2ptCmd) =>
        for (cleanId <- input(conf.clean2ptCmd.cleanIds)) {
          HtrcVolumeId.parseClean(cleanId) match {
            case Success(htid) => Console.println(htid.toPairtreeDoc.pathPrefix)
            case Failure(e) => Console.err.println(e.toString)
          }
        }

      case Some(conf.clean2rootCmd) =>
        for (cleanId <- input(conf.clean2rootCmd.cleanIds)) {
          HtrcVolumeId.parseClean(cleanId) match {
            case Success(htid) => Console.println(htid.toPairtreeDoc.rootPath)
            case Failure(e) => Console.err.println(e.toString)
          }
        }

      case Some(conf.unclean2ptCmd) =>
        for (uncleanId <- input(conf.unclean2ptCmd.uncleanIds)) {
          HtrcVolumeId.parseUnclean(uncleanId) match {
            case Success(htid) => Console.println(htid.toPairtreeDoc.pathPrefix)
            case Failure(e) => Console.err.println(e.toString)
          }
        }

      case Some(conf.unclean2rootCmd) =>
        for (uncleanId <- input(conf.unclean2rootCmd.uncleanIds)) {
          HtrcVolumeId.parseUnclean(uncleanId) match {
            case Success(htid) => Console.println(htid.toPairtreeDoc.rootPath)
            case Failure(e) => Console.err.println(e.toString)
          }
        }

      case Some(conf.parseCmd) =>
        if (conf.parseCmd.header())
          Console.println("uncleanId\tcleanId\tlib\tfile")

        for (file <- input(conf.parseCmd.paths).map(new File(_))) {
          PairtreeDocument.from(file) match {
            case Success(doc) =>
              val volumeId = doc.volumeId
              Console.println(s"${volumeId.uncleanId}\t${volumeId.cleanId}\t${volumeId.libId}\t${file.getName}")

            case Failure(e) => Console.err.println(e.toString)
          }
        }

      case _ => Console.err.println("Invalid subcommand in command line")
    }
  }

  def input(arg: ScallopOption[List[String]]): TraversableOnce[String] = arg.toOption match {
    case Some(values) => values
    case None => Iterator.continually(StdIn.readLine()).takeWhile(_ != null)
  }
}
