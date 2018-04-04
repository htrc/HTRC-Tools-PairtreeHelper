package org.hathitrust.htrc.tools.pairtreehelper

import org.rogach.scallop.{ScallopConf, ScallopOption, Subcommand}

class Conf(args: Seq[String]) extends ScallopConf(args) {
  val (appTitle, appVersion, appVendor) = {
    val p = getClass.getPackage
    val nameOpt = Option(p).flatMap(p => Option(p.getImplementationTitle))
    val versionOpt = Option(p).flatMap(p => Option(p.getImplementationVersion))
    val vendorOpt = Option(p).flatMap(p => Option(p.getImplementationVendor))
    (nameOpt, versionOpt, vendorOpt)
  }

  version(appTitle.flatMap(
    name => appVersion.flatMap(
      version => appVendor.map(
        vendor => s"$name $version\n$vendor"))).getOrElse(Main.appName))

  val cleanCmd = new Subcommand("clean") {
    descr("Converts unclean ids into clean ids (that can be used as filenames)")
    val uncleanIds: ScallopOption[List[String]] = trailArg[List[String]](
      descr = "List of HTRC unclean ids",
      required = false
    )
  }

  val uncleanCmd = new Subcommand("unclean") {
    descr("Converts clean ids back to the original unclean ids")
    val cleanIds: ScallopOption[List[String]] = trailArg[List[String]](
      descr = "List of HTRC clean ids",
      required = false
    )
  }

  val clean2ptCmd = new Subcommand("clean2pt") {
    descr("Constructs the pairtree path associated with the given HTRC clean ids")
    val cleanIds: ScallopOption[List[String]] = trailArg[List[String]](
      descr = "List of HTRC clean ids",
      required = false
    )
  }

  val clean2rootCmd = new Subcommand("clean2root") {
    descr("Constructs the pairtree root folder for the documents associated with the given HTRC clean ids")
    val cleanIds: ScallopOption[List[String]] = trailArg[List[String]](
      descr = "List of HTRC clean ids",
      required = false
    )
  }

  val unclean2ptCmd = new Subcommand("unclean2pt") {
    descr("Constructs the pairtree path associated with the given HTRC (unclean) ids")
    val uncleanIds: ScallopOption[List[String]] = trailArg[List[String]](
      descr = "List of HTRC unclean ids",
      required = false
    )
  }

  val unclean2rootCmd = new Subcommand("unclean2root") {
    descr("Constructs the pairtree root folder for the documents associated with the given HTRC (unclean) ids")
    val uncleanIds: ScallopOption[List[String]] = trailArg[List[String]](
      descr = "List of HTRC unclean ids",
      required = false
    )
  }

  val parseCmd = new Subcommand("parse") {
    descr("Parses a pairtree path and reports the components of the path")
    val header: ScallopOption[Boolean] = opt[Boolean](
      descr = "Specifies whether a header is included in the output (useful for CSV readers)",
      default = Some(false)
    )
    val paths: ScallopOption[List[String]] = trailArg[List[String]](
      descr = "List of pairtree paths",
      required = false
    )
  }

  addSubcommand(cleanCmd)
  addSubcommand(uncleanCmd)
  addSubcommand(clean2ptCmd)
  addSubcommand(clean2rootCmd)
  addSubcommand(unclean2ptCmd)
  addSubcommand(unclean2rootCmd)
  addSubcommand(parseCmd)

  verify()
}
