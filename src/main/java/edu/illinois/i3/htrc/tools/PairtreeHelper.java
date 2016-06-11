package edu.illinois.i3.htrc.tools;

import com.beust.jcommander.*;
import gov.loc.repository.pairtree.Pairtree;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PairtreeHelper {
    protected static final Pairtree pairtree = new Pairtree();
    protected static final Pattern PairtreeFilePartsRegex =
            Pattern.compile("(?<libid>[^/]+)/pairtree_root/(?<ppath>.+)/(?<cleanid>[^/]+)/[^/]+$");

    /**
     * Parses an HTRC pairtree file path into a {@link PairtreeDocument} that can be used
     * to extract metadata about the document
     *
     * @param filePath The pairtree file path
     * @return The {@link PairtreeDocument}
     * @throws InvalidPairtreePathException Thrown if the given filePath is not a valid pairtree path
     */
    public static PairtreeDocument parse(String filePath) throws InvalidPairtreePathException {
        Matcher pairtreeFilePartsMatcher = PairtreeFilePartsRegex.matcher(filePath);
        if (!pairtreeFilePartsMatcher.find())
            throw new InvalidPairtreePathException(String.format("%s is not a valid HTRC pairtree file path", filePath));

        String libId = pairtreeFilePartsMatcher.group("libid");
        String ppath = pairtreeFilePartsMatcher.group("ppath");
        String cleanId = pairtreeFilePartsMatcher.group("cleanid");
        String uncleanId = pairtree.uncleanId(cleanId);

        return new PairtreeDocument(libId, uncleanId, cleanId, ppath);
    }

    /**
     * Parses an HTRC pairtree file into a {@link PairtreeDocument} that can be used
     * to extract metadata about the document
     *
     * @param file The pairtree file
     * @return The {@link PairtreeDocument}
     * @throws IOException Thrown if the canonical path of the given file cannot be resolved
     * @throws InvalidPairtreePathException Thrown if the given filePath is not a valid pairtree path
     */
    public static PairtreeDocument parse(File file) throws IOException, InvalidPairtreePathException {
        return parse(file.getCanonicalPath());
    }

    /**
     * Converts an unclean id to a clean id<br>
     * Note: clean ids can be used as valid file or path names, while unclean ids cannot be used for such purposes
     *
     * @param uncleanId The unclean id
     * @return The clean id
     */
    public static String cleanId(String uncleanId) {
        return pairtree.cleanId(uncleanId);
    }

    /**
     * Converts a clean id to an unclean id <br>
     * Note: clean ids can be used as valid file or path names, while unclean ids cannot be used for such purposes
     *
     * @param cleanId The clean id
     * @return The unclean id
     */
    public static String uncleanId(String cleanId) {
        return pairtree.uncleanId(cleanId);
    }

    /**
     * Constructs the pairtree path associated with the given HTRC clean id
     *
     * @param htrcCleanId The HTRC clean id
     * @return The {@link PairtreeDocument}
     * @throws InvalidHtrcIdException Thrown if the given id is not a valid HTRC clean id
     */
    public static PairtreeDocument getDocFromCleanId(String htrcCleanId) throws InvalidHtrcIdException {
        int index = htrcCleanId.indexOf('.');
        if (index == -1)
            throw new InvalidHtrcIdException(String.format("%s is not a valid HTRC clean id", htrcCleanId));

        String libId = htrcCleanId.substring(0, index);
        String cleanId = htrcCleanId.substring(index + 1);
        String uncleanId = pairtree.uncleanId(cleanId);
        String ppath = pairtree.mapToPPath(uncleanId);

        return new PairtreeDocument(libId, uncleanId, cleanId, ppath);
    }

    /**
     * Constructs the pairtree path associated with the given HTRC unclean id
     *
     * @param htrcUncleanId The HTRC unclean id
     * @return The {@link PairtreeDocument}
     * @throws InvalidHtrcIdException Thrown if the given id is not a valid HTRC unclean id
     */
    public static PairtreeDocument getDocFromUncleanId(String htrcUncleanId) throws InvalidHtrcIdException {
        int index = htrcUncleanId.indexOf('.');
        if (index == -1)
            throw new InvalidHtrcIdException(String.format("%s is not a valid HTRC unclean id", htrcUncleanId));

        String libId = htrcUncleanId.substring(0, index);
        String uncleanId = htrcUncleanId.substring(index + 1);
        String cleanId = pairtree.cleanId(uncleanId);
        String ppath = pairtree.mapToPPath(uncleanId);

        return new PairtreeDocument(libId, uncleanId, cleanId, ppath);
    }

    /**
     * A class representing an HTRC pairtree document
     */
    public static class PairtreeDocument implements Serializable {
        private final String _libId;
        private final String _cleanId;
        private final String _uncleanId;
        private final String _ppath;
        private final String _docRootPath;
        private final String _docPathPrefix;

        private PairtreeDocument(String source, String uncleanId, String cleanId, String ppath) {
            _libId = source;
            _uncleanId = uncleanId;
            _cleanId = cleanId;
            _ppath = ppath;
            _docRootPath = String.format("%s/pairtree_root/%s/%s", _libId, _ppath, _cleanId);
            _docPathPrefix = String.format("%s/%s", _docRootPath, _cleanId);
        }

        /**
         * Returns the document path prefix for this pairtree document; for example,
         * a volume with ID mdp.39015063051745 would generate the document path prefix
         * mdp/pairtree_root/39/01/50/63/05/17/45/39015063051745/39015063051745
         * By appending ".zip" or ".mets.xml" to this path you can point to the pairtree volume ZIP
         * or the volume METS metadata file, as desired.
         *
         * @return The document path
         */
        public String getDocumentPathPrefix() {
            return _docPathPrefix;
        }

        /**
         * Returns the root folder for the document
         *
         * @return The document folder path
         */
        public String getDocumentRootPath() { return _docRootPath; }

        /**
         * Returns the library identifier for the source library that provided this document
         *
         * @return The source library id
         */
        public String getLibraryId() {
            return _libId;
        }

        /**
         * Returns the HTRC clean id for this document <br>
         * Note: clean ids can be used as valid file or path names, while unclean ids cannot be used for such purposes
         *
         * @return The HTRC clean id
         */
        public String getCleanId() {
            return String.format("%s.%s", _libId, _cleanId);
        }

        /**
         * Returns a clean id without the library identifier prefix
         *
         * @return A clean id without the library identifier prefix
         */
        public String getCleanIdWithoutLibId() {
            return _cleanId;
        }

        /**
         * Returns the HTRC unclean id for this document <br>
         * Note: clean ids can be used as valid file or path names, while unclean ids cannot be used for such purposes
         *
         * @return The HTRC unclean id
         */
        public String getUncleanId() {
            return String.format("%s.%s", _libId, _uncleanId);
        }

        /**
         * Returns an unclean id without the library identifier prefix
         *
         * @return An unclean id without the library identifier prefix
         */
        public String getUncleanIdWithoutLibId() {
            return _uncleanId;
        }

        /**
         * Returns the non-HTRC pairtree parent path of this document
         *
         * @return The non-HTRC pairtree parent path
         */
        public String getPpath() {
            return _ppath;
        }

        @Override
        public boolean equals(Object other) {
            if (this == other) return true;
            if (other == null || !(other instanceof PairtreeDocument)) return false;

            PairtreeDocument document = (PairtreeDocument) other;
            return _cleanId.equals(document._cleanId);
        }

        @Override
        public int hashCode() {
            return _cleanId.hashCode();
        }

        @Override
        public String toString() {
            return String.format("%s(uncleanId: %s, cleanId: %s, docPrefix: %s)",
                    getClass().getSimpleName(), _uncleanId, _cleanId, _docPathPrefix);
        }
    }

    private static final String APP_NAME = System.getProperty("app.name", PairtreeHelper.class.getSimpleName());

    public static void main(String[] args) {
        PairtreeHelperCommands commands = new PairtreeHelperCommands();
        JCommander commander = new JCommander(commands);
        commander.setProgramName(APP_NAME);

        commander.addCommand("clean", commands.cmdCleanId);
        commander.addCommand("unclean", commands.cmdUncleanId);
        commander.addCommand("clean2pt", commands.cmdGetPathFromCleanId);
        commander.addCommand("clean2root", commands.cmdGetRootDocFromCleanId);
        commander.addCommand("unclean2pt", commands.cmdGetPathFromUncleanId);
        commander.addCommand("unclean2root", commands.cmdGetRootDocFromUncleanId);
        commander.addCommand("parse", commands.cmdParse);

        if (args.length == 0)
            args = new String[] { "--help" };

        try {
            commander.parse(args);

            if (commands.cmdHelp.help) {
                commander.usage();
                System.exit(-1);
            }
        }
        catch (ParameterException e) {
            commander.usage();
            System.exit(-2);
        }

        String command = commander.getParsedCommand();

        if ("clean".equalsIgnoreCase(command)) {
            List<String> uncleanIds = commands.cmdCleanId.uncleanIds;
            if (uncleanIds == null) {
                uncleanIds = new ArrayList<>();
                Scanner scanner = new Scanner(System.in);
                while (scanner.hasNext())
                    uncleanIds.add(scanner.next());
            }
            for (String uncleanId : uncleanIds) {
                int pos = uncleanId.indexOf(".");
                String libId = "";
                if (pos > 0) {
                    libId = uncleanId.substring(0, pos+1);
                    uncleanId = uncleanId.substring(pos+1);
                }
                System.out.println(libId + cleanId(uncleanId));
            }
        }

        else

        if ("unclean".equalsIgnoreCase(command)) {
            List<String> cleanIds = commands.cmdUncleanId.cleanIds;
            if (cleanIds == null) {
                cleanIds = new ArrayList<>();
                Scanner scanner = new Scanner(System.in);
                while (scanner.hasNext())
                    cleanIds.add(scanner.next());
            }
            for (String cleanId : cleanIds) {
                int pos = cleanId.indexOf(".");
                String libId = "";
                if (pos > 0) {
                    libId = cleanId.substring(0, pos+1);
                    cleanId = cleanId.substring(pos+1);
                }
                System.out.println(libId + uncleanId(cleanId));
            }
        }

        else

        if ("clean2pt".equalsIgnoreCase(command)) {
            List<String> cleanIds = commands.cmdGetPathFromCleanId.cleanIds;
            if (cleanIds == null) {
                cleanIds = new ArrayList<>();
                Scanner scanner = new Scanner(System.in);
                while (scanner.hasNext())
                    cleanIds.add(scanner.next());
            }
            for (String cleanId : cleanIds) {
                try {
                    System.out.println(getDocFromCleanId(cleanId).getDocumentPathPrefix());
                }
                catch (InvalidHtrcIdException e) {
                    System.err.println("Invalid HTRC id: " + cleanId);
                }
            }
        }

        else

        if ("clean2root".equalsIgnoreCase(command)) {
            List<String> cleanIds = commands.cmdGetRootDocFromCleanId.cleanIds;
            if (cleanIds == null) {
                cleanIds = new ArrayList<>();
                Scanner scanner = new Scanner(System.in);
                while (scanner.hasNext())
                    cleanIds.add(scanner.next());
            }
            for (String cleanId : cleanIds) {
                try {
                    System.out.println(getDocFromCleanId(cleanId).getDocumentRootPath());
                }
                catch (InvalidHtrcIdException e) {
                    System.err.println("Invalid HTRC id: " + cleanId);
                }
            }
        }

        else

        if ("unclean2pt".equalsIgnoreCase(command)) {
            List<String> uncleanIds = commands.cmdGetPathFromUncleanId.uncleanIds;
            if (uncleanIds == null) {
                uncleanIds = new ArrayList<>();
                Scanner scanner = new Scanner(System.in);
                while (scanner.hasNext())
                    uncleanIds.add(scanner.next());
            }
            for (String uncleanId : uncleanIds) {
                try {
                    System.out.println(getDocFromUncleanId(uncleanId).getDocumentPathPrefix());
                }
                catch (InvalidHtrcIdException e) {
                    System.err.println("Invalid HTRC id: " + uncleanId);
                }
            }
        }

        else

        if ("unclean2root".equalsIgnoreCase(command)) {
            List<String> uncleanIds = commands.cmdGetRootDocFromUncleanId.uncleanIds;
            if (uncleanIds == null) {
                uncleanIds = new ArrayList<>();
                Scanner scanner = new Scanner(System.in);
                while (scanner.hasNext())
                    uncleanIds.add(scanner.next());
            }
            for (String uncleanId : uncleanIds) {
                try {
                    System.out.println(getDocFromUncleanId(uncleanId).getDocumentRootPath());
                }
                catch (InvalidHtrcIdException e) {
                    System.err.println("Invalid HTRC id: " + uncleanId);
                }
            }
        }

        else

        if ("parse".equalsIgnoreCase(command)) {
            List<String> paths = commands.cmdParse.paths;
            if (paths == null) {
                paths = new ArrayList<>();
                Scanner scanner = new Scanner(System.in);
                while (scanner.hasNext())
                    paths.add(scanner.next());
            }

            boolean showHeader = commands.cmdParse.showHeader;

            if (showHeader)
                System.out.println(String.format("%s\t%s\t%s\t%s", "uncleanId", "cleanId", "lib", "file"));

            for (String filePath : paths) {
                try {
                    File file = new File(filePath);
                    PairtreeDocument document = parse(file);
                    String uncleanId = document.getUncleanId();
                    String cleanId = document.getCleanId();
                    String libraryId = document.getLibraryId();
                    String name = file.getName();

                    System.out.println(String.format("%s\t%s\t%s\t%s", uncleanId, cleanId, libraryId, name));
                }
                catch (IOException e) {
                    System.err.println(e.getMessage());
                }
            }
        }
    }

    public static class PairtreeHelperCommands {
        @ParametersDelegate
        public final CommandHelp cmdHelp = new CommandHelp();
        public final CommandCleanId cmdCleanId = new CommandCleanId();
        public final CommandUncleanId cmdUncleanId = new CommandUncleanId();
        public final CommandGetPathFromCleanId cmdGetPathFromCleanId = new CommandGetPathFromCleanId();
        public final CommandGetRootDocFromCleanId cmdGetRootDocFromCleanId = new CommandGetRootDocFromCleanId();
        public final CommandGetPathFromUncleanId cmdGetPathFromUncleanId = new CommandGetPathFromUncleanId();
        public final CommandGetRootDocFromUncleanId cmdGetRootDocFromUncleanId = new CommandGetRootDocFromUncleanId();
        public final CommandParse cmdParse = new CommandParse();
    }

    @Parameters(commandDescription = "Converts unclean ids into clean ids (that can be used as filenames)")
    public static class CommandCleanId {
        @Parameter(description = "<list-of-unclean-ids-to-clean>")
        private List<String> uncleanIds;
    }

    @Parameters(commandDescription = "Converts clean ids back to the original unclean ids")
    public static class CommandUncleanId {
        @Parameter(description = "<list-of-clean-ids-to-convert>")
        private List<String> cleanIds;
    }

    @Parameters(commandDescription = "Constructs the pairtree path associated with the given HTRC clean ids")
    public static class CommandGetPathFromCleanId {
        @Parameter(description = "<list-of-htrc-clean-ids>")
        private List<String> cleanIds;
    }

    @Parameters(commandDescription = "Constructs the pairtree root folder for the documents associated with the given HTRC clean ids")
    public static class CommandGetRootDocFromCleanId {
        @Parameter(description = "<list-of-htrc-clean-ids>")
        private List<String> cleanIds;
    }

    @Parameters(commandDescription = "Constructs the pairtree path associated with the given HTRC (unclean) ids")
    public static class CommandGetPathFromUncleanId {
        @Parameter(description = "<list-of-htrc-unclean-ids>")
        private List<String> uncleanIds;
    }

    @Parameters(commandDescription = "Constructs the pairtree root folder for the documents associated with the given HTRC (unclean) ids")
    public static class CommandGetRootDocFromUncleanId {
        @Parameter(description = "<list-of-htrc-unclean-ids>")
        private List<String> uncleanIds;
    }

    @Parameters(commandDescription = "Parses a pairtree path and reports the components of the path")
    public static class CommandParse {
        @Parameter(description = "<pairtree-paths>")
        private List<String> paths;

        @Parameter(names = "--header", description = "Specifies whether a header is included in the output (useful for CSV readers)")
        private boolean showHeader = true;
    }

    public static class CommandHelp {
        @Parameter(
                names = { "-h", "-?", "-help", "--help" },
                description = "Show help menu",
                help = true
        )
        public boolean help;
    }
}
