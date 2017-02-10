package org.hathitrust.htrc.tools.pairtreehelper;

import gov.loc.repository.pairtree.Pairtree;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
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
     * Converts the given HTRC clean id into a {@link PairtreeDocument} that can be used
     * to extract metadata about the document
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
     * Converts the given HTRC unclean id into a {@link PairtreeDocument} that can be used
     * to extract metadata about the document
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
         * Convenience method for retrieving the full document path prefix.
         *
         * @param pairtreeRoot The path to the pairtree root
         * @return The full document path prefix
         */
        public String getDocumentPathPrefix(String pairtreeRoot) {
            return new File(pairtreeRoot, getDocumentPathPrefix()).toString();
        }

        /**
         * Convenience method for quickly getting the path to the ZIP file.
         *
         * @return The relative path to the ZIP file
         */
        public String getZipPath() { return String.format("%s.zip", getDocumentPathPrefix()); }

        /**
         * Convenience method for retrieving the full ZIP volume path prefix.
         *
         * @param pairtreeRoot The path to the pairtree root
         * @return The full ZIP volume path prefix
         */
        public String getZipPath(String pairtreeRoot) {
            return new File(pairtreeRoot, getZipPath()).toString();
        }

        /**
         * Convenience method for quickly getting the path to the METS XML file.
         *
         * @return The relative path to the METS XML file
         */
        public String getMetsPath() { return String.format("%s.mets.xml", getDocumentPathPrefix()); }

        /**
         * Convenience method for retrieving the full METS XML path prefix.
         *
         * @param pairtreeRoot The path to the pairtree root
         * @return The full METS XML path prefix
         */
        public String getMetsPath(String pairtreeRoot) {
            return new File(pairtreeRoot, getMetsPath()).toString();
        }

        /**
         * Returns the root folder for the document
         *
         * @return The document folder path
         */
        public String getDocumentRootPath() { return _docRootPath; }

        /**
         * Convenience method for retrieving the full document root path prefix.
         *
         * @param pairtreeRoot The path to the pairtree root
         * @return The full document root path prefix
         */
        public String getDocumentRootPath(String pairtreeRoot) {
            return new File(pairtreeRoot, getDocumentRootPath()).toString();
        }

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
}
