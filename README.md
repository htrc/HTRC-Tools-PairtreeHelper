# HTRC-Tools-PairtreeHelper
This is a Java library that helps with dealing with a pairtree structure.

# Usage
```
Usage: PairtreeHelper [options] [command] [command options]
  Options:
    -h, -?, -help, --help
       Show help menu
       Default: false
  Commands:
    clean      Converts unclean ids into clean ids (that can be used as filenames)
      Usage: clean [options] <list-of-unclean-ids-to-clean>

    unclean      Converts clean ids back to the original unclean ids
      Usage: unclean [options] <list-of-clean-ids-to-convert>

    clean2pt      Constructs the pairtree path associated with the given HTRC clean ids
      Usage: clean2pt [options] <list-of-htrc-clean-ids>

    unclean2pt      Constructs the pairtree path associated with the given HTRC (unclean) ids
      Usage: unclean2pt [options] <list-of-htrc-unclean-ids>

    parse      Parses a pairtree path and reports the components of the path
      Usage: parse [options] <pairtree-path>
        Options:
          --header
             Specifies whether a header is included in the output (useful for
             CSV readers)
             Default: true
```

## clean vs unclean IDs
unclean IDs are the original HT IDs which may contain characters that are not suitable for use in a file name; for example: `uc2.ark:/13960/t4jm2cj7x`

clean IDs are file-friendly derivatives of an unclean ID, where the problematic characters have been replaced according to a (pretty obscure) set of rules, defined in the original [Pairtree Document](https://confluence.ucop.edu/display/Curation/PairTree);  example clean ID: `uc2.ark+=13960=t4jm2cj7x`
