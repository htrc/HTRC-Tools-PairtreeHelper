package org.hathitrust.htrc.tools.pairtreehelper;

import com.beust.jcommander.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Java library and application that provides various APIs for
 * managing HT IDs and the Pairtree structure.
 *
 * @author Boris Capitanu
 */

public class Main {
    private static final String APP_NAME = System.getProperty("app.name", "pairtree-helper");

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
                System.out.println(libId + PairtreeHelper.cleanId(uncleanId));
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
                System.out.println(libId + PairtreeHelper.uncleanId(cleanId));
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
                    System.out.println(PairtreeHelper.getDocFromCleanId(cleanId).getDocumentPathPrefix());
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
                    System.out.println(PairtreeHelper.getDocFromCleanId(cleanId).getDocumentRootPath());
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
                    System.out.println(PairtreeHelper.getDocFromUncleanId(uncleanId).getDocumentPathPrefix());
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
                    System.out.println(PairtreeHelper.getDocFromUncleanId(uncleanId).getDocumentRootPath());
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
                    PairtreeHelper.PairtreeDocument document = PairtreeHelper.parse(file);
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
