# HTRC-Tools-PairtreeHelper
This is a Java library that provides various APIs for managing HT IDs and the Pairtree structure.

# Build

`mvn clean package`  

Find the built packages in the `target/` folder.

# Run
```
Usage: pairtree-helper [options] [command] [command options]
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

    clean2root      Constructs the pairtree root folder for the documents associated with the given HTRC clean ids
      Usage: clean2root [options] <list-of-htrc-clean-ids>

    unclean2pt      Constructs the pairtree path associated with the given HTRC (unclean) ids
      Usage: unclean2pt [options] <list-of-htrc-unclean-ids>

    unclean2root      Constructs the pairtree root folder for the documents associated with the given HTRC (unclean) ids
      Usage: unclean2root [options] <list-of-htrc-unclean-ids>

    parse      Parses a pairtree path and reports the components of the path
      Usage: parse [options] <pairtree-paths>
        Options:
          --header
             Specifies whether a header is included in the output (useful for
             CSV readers)
             Default: true
```

## clean vs unclean IDs
unclean IDs are the original HT IDs which may contain characters that are not suitable for use in a file name; for example: `uc2.ark:/13960/t4jm2cj7x`

clean IDs are file-friendly derivatives of an unclean ID, where the problematic characters have been replaced according to a (pretty obscure) set of rules, defined in the original [Pairtree Document](https://confluence.ucop.edu/display/Curation/PairTree);  example clean ID: `uc2.ark+=13960=t4jm2cj7x`

# APIs

To use via Maven:
```
<dependency>
    <groupId>org.hathitrust.htrc</groupId>
    <artifactId>pairtree-helper</artifactId>
    <version>3.1-SNAPSHOT</version>
</dependency>
```

To use via SBT:  
`libraryDependencies += "org.hathitrust.htrc" % "pairtree-helper" % "3.1-SNAPSHOT"`

## PairtreeHelper API
```
/**
 * Parses an HTRC pairtree file path into a {@link PairtreeDocument} that can be used
 * to extract metadata about the document
 *
 * @param filePath The pairtree file path
 * @return The {@link PairtreeDocument}
 * @throws InvalidPairtreePathException Thrown if the given filePath is not a valid pairtree path
 */
public static PairtreeDocument parse(String filePath) throws InvalidPairtreePathException

/**
 * Parses an HTRC pairtree file into a {@link PairtreeDocument} that can be used
 * to extract metadata about the document
 *
 * @param file The pairtree file
 * @return The {@link PairtreeDocument}
 * @throws IOException Thrown if the canonical path of the given file cannot be resolved
 * @throws InvalidPairtreePathException Thrown if the given filePath is not a valid pairtree path
 */
public static PairtreeDocument parse(File file) throws IOException, InvalidPairtreePathException

/**
 * Converts an unclean id to a clean id<br>
 * Note: clean ids can be used as valid file or path names, while unclean ids cannot be used for such purposes
 *
 * @param uncleanId The unclean id
 * @return The clean id
 */
public static String cleanId(String uncleanId)

/**
 * Converts a clean id to an unclean id <br>
 * Note: clean ids can be used as valid file or path names, while unclean ids cannot be used for such purposes
 *
 * @param cleanId The clean id
 * @return The unclean id
 */
public static String uncleanId(String cleanId)

/**
 * Converts the given HTRC clean id into a {@link PairtreeDocument} that can be used
 * to extract metadata about the document
 *
 * @param htrcCleanId The HTRC clean id
 * @return The {@link PairtreeDocument}
 * @throws InvalidHtrcIdException Thrown if the given id is not a valid HTRC clean id
 */
public static PairtreeDocument getDocFromCleanId(String htrcCleanId) throws InvalidHtrcIdException

/**
 * Converts the given HTRC unclean id into a {@link PairtreeDocument} that can be used
 * to extract metadata about the document
 *
 * @param htrcUncleanId The HTRC unclean id
 * @return The {@link PairtreeDocument}
 * @throws InvalidHtrcIdException Thrown if the given id is not a valid HTRC unclean id
 */
public static PairtreeDocument getDocFromUncleanId(String htrcUncleanId) throws InvalidHtrcIdException

```

## PairtreeDocument API
```
/**
 * Returns the document path prefix for this pairtree document; for example,
 * a volume with ID mdp.39015063051745 would generate the document path prefix
 * mdp/pairtree_root/39/01/50/63/05/17/45/39015063051745/39015063051745
 * By appending ".zip" or ".mets.xml" to this path you can point to the pairtree volume ZIP
 * or the volume METS metadata file, as desired.
 *
 * @return The document path
 */
public String getDocumentPathPrefix()

/**
 * Returns the root folder for the document
 *
 * @return The document folder path
 */
public String getDocumentRootPath()

/**
 * Returns the library identifier for the source library that provided this document
 *
 * @return The source library id
 */
public String getLibraryId()

/**
 * Returns the HTRC clean id for this document <br>
 * Note: clean ids can be used as valid file or path names, while unclean ids cannot be used for such purposes
 *
 * @return The HTRC clean id
 */
public String getCleanId()

/**
 * Returns a clean id without the library identifier prefix
 *
 * @return A clean id without the library identifier prefix
 */
public String getCleanIdWithoutLibId()

/**
 * Returns the HTRC unclean id for this document <br>
 * Note: clean ids can be used as valid file or path names, while unclean ids cannot be used for such purposes
 *
 * @return The HTRC unclean id
 */
public String getUncleanId()

/**
 * Returns an unclean id without the library identifier prefix
 *
 * @return An unclean id without the library identifier prefix
 */
public String getUncleanIdWithoutLibId()

```