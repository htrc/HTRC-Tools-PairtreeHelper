# HTRC-Tools-PairtreeHelper
Tool and library that provides various APIs for managing HT IDs and the Pairtree structure.

# Build

`sbt "project lib" clean test package` for building the library
`sbt clean test app/dist` for building the app 

Then find the built packages in the following folders:
- lib/target/scala-2.12/
- app/target/universal/

# Run
```
pairtree-helper-app 4.0
HathiTrust Research Center
  -h, --help      Show help message
  -v, --version   Show version of this program

Subcommand: clean - Converts unclean ids into clean ids (that can be used as filenames)
  -h, --help   Show help message

 trailing arguments:
  unclean-ids (not required)   List of HTRC unclean ids

Subcommand: unclean - Converts clean ids back to the original unclean ids
  -h, --help   Show help message

 trailing arguments:
  clean-ids (not required)   List of HTRC clean ids

Subcommand: clean2pt - Constructs the pairtree path associated with the given HTRC clean ids
  -h, --help   Show help message

 trailing arguments:
  clean-ids (not required)   List of HTRC clean ids

Subcommand: clean2root - Constructs the pairtree root folder for the documents associated with the given HTRC clean ids
  -h, --help   Show help message

 trailing arguments:
  clean-ids (not required)   List of HTRC clean ids

Subcommand: unclean2pt - Constructs the pairtree path associated with the given HTRC (unclean) ids
  -h, --help   Show help message

 trailing arguments:
  unclean-ids (not required)   List of HTRC unclean ids

Subcommand: unclean2root - Constructs the pairtree root folder for the documents associated with the given HTRC (unclean) ids
  -h, --help   Show help message

 trailing arguments:
  unclean-ids (not required)   List of HTRC unclean ids

Subcommand: parse - Parses a pairtree path and reports the components of the path
  -h, --header   Specifies whether a header is included in the output (useful
                 for CSV readers)
      --help     Show help message

 trailing arguments:
  paths (not required)   List of pairtree paths
```

## clean vs unclean IDs
unclean IDs are the original HT IDs which may contain characters that are not suitable for use in a file name; for example: `uc2.ark:/13960/t4jm2cj7x`

clean IDs are file-friendly derivatives of an unclean ID, where the problematic characters have been replaced according to a (pretty obscure) set of rules, defined in the original [Pairtree Document](https://confluence.ucop.edu/display/Curation/PairTree);  example clean ID: `uc2.ark+=13960=t4jm2cj7x`

# APIs

To use via SBT:  
`libraryDependencies += "org.hathitrust.htrc" % "pairtree-helper" % "4.0"`

