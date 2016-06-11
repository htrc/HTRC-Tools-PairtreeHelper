package edu.illinois.i3.htrc.tools;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PairtreeHelperTest {

    @Test
    public void testParseSuccessFullPath() throws Exception {
        String docPath = "/NGPD/uc2/pairtree_root/ar/k+/=1/39/60/=t/4q/j7/97/0f/ark+=13960=t4qj7970f/ark+=13960=t4qj7970f.zip";
        PairtreeHelper.PairtreeDocument document = PairtreeHelper.parse(docPath);
        assertEquals("uc2/pairtree_root/ar/k+/=1/39/60/=t/4q/j7/97/0f/ark+=13960=t4qj7970f/ark+=13960=t4qj7970f", document.getDocumentPathPrefix());
        assertEquals("uc2/pairtree_root/ar/k+/=1/39/60/=t/4q/j7/97/0f/ark+=13960=t4qj7970f", document.getDocumentRootPath());
        assertEquals("ar/k+/=1/39/60/=t/4q/j7/97/0f", document.getPpath());
        assertEquals("uc2", document.getLibraryId());
        assertEquals("uc2.ark+=13960=t4qj7970f", document.getCleanId());
        assertEquals("ark+=13960=t4qj7970f", document.getCleanIdWithoutLibId());
        assertEquals("uc2.ark:/13960/t4qj7970f", document.getUncleanId());
        assertEquals("ark:/13960/t4qj7970f", document.getUncleanIdWithoutLibId());
    }

    @Test
    public void testParseSuccessRootPath() throws Exception {
        String docPath = "/uc2/pairtree_root/ar/k+/=1/39/60/=t/4q/j7/97/0f/ark+=13960=t4qj7970f/ark+=13960=t4qj7970f.zip";
        PairtreeHelper.PairtreeDocument document = PairtreeHelper.parse(docPath);
        assertEquals("uc2/pairtree_root/ar/k+/=1/39/60/=t/4q/j7/97/0f/ark+=13960=t4qj7970f/ark+=13960=t4qj7970f", document.getDocumentPathPrefix());
        assertEquals("uc2/pairtree_root/ar/k+/=1/39/60/=t/4q/j7/97/0f/ark+=13960=t4qj7970f", document.getDocumentRootPath());
        assertEquals("ar/k+/=1/39/60/=t/4q/j7/97/0f", document.getPpath());
        assertEquals("uc2", document.getLibraryId());
        assertEquals("uc2.ark+=13960=t4qj7970f", document.getCleanId());
        assertEquals("ark+=13960=t4qj7970f", document.getCleanIdWithoutLibId());
        assertEquals("uc2.ark:/13960/t4qj7970f", document.getUncleanId());
        assertEquals("ark:/13960/t4qj7970f", document.getUncleanIdWithoutLibId());
    }

    @Test
    public void testParseSuccessRelativePath() throws Exception {
        String docPath = "uc2/pairtree_root/ar/k+/=1/39/60/=t/4q/j7/97/0f/ark+=13960=t4qj7970f/ark+=13960=t4qj7970f.zip";
        PairtreeHelper.PairtreeDocument document = PairtreeHelper.parse(docPath);
        assertEquals("uc2/pairtree_root/ar/k+/=1/39/60/=t/4q/j7/97/0f/ark+=13960=t4qj7970f/ark+=13960=t4qj7970f", document.getDocumentPathPrefix());
        assertEquals("uc2/pairtree_root/ar/k+/=1/39/60/=t/4q/j7/97/0f/ark+=13960=t4qj7970f", document.getDocumentRootPath());
        assertEquals("ar/k+/=1/39/60/=t/4q/j7/97/0f", document.getPpath());
        assertEquals("uc2", document.getLibraryId());
        assertEquals("uc2.ark+=13960=t4qj7970f", document.getCleanId());
        assertEquals("ark+=13960=t4qj7970f", document.getCleanIdWithoutLibId());
        assertEquals("uc2.ark:/13960/t4qj7970f", document.getUncleanId());
        assertEquals("ark:/13960/t4qj7970f", document.getUncleanIdWithoutLibId());
    }

    @Test(expected = InvalidPairtreePathException.class)
    public void testParseFailure() throws InvalidPairtreePathException {
        String wrongPath = "/ar/k+/=1/39/60/=t/4q/j7/97/0f/ark+=13960=t4qj7970f/ark+=13960=t4qj7970f.zip";
        PairtreeHelper.parse(wrongPath);
    }

    @Test
    public void testGetPathFromCleanId() throws Exception {
        String cleanId = "uc2.ark+=13960=t4qj7970f";
        PairtreeHelper.PairtreeDocument doc = PairtreeHelper.getDocFromCleanId(cleanId);
        assertEquals("uc2/pairtree_root/ar/k+/=1/39/60/=t/4q/j7/97/0f/ark+=13960=t4qj7970f/ark+=13960=t4qj7970f", doc.getDocumentPathPrefix());
    }

    @Test
    public void testGetRootPathFromCleanId() throws Exception {
        String cleanId = "uc2.ark+=13960=t4qj7970f";
        PairtreeHelper.PairtreeDocument doc = PairtreeHelper.getDocFromCleanId(cleanId);
        assertEquals("uc2/pairtree_root/ar/k+/=1/39/60/=t/4q/j7/97/0f/ark+=13960=t4qj7970f", doc.getDocumentRootPath());
    }

    @Test
    public void testGetPathFromUncleanId() throws Exception {
        String uncleanId = "uc2.ark:/13960/t4qj7970f";
        PairtreeHelper.PairtreeDocument doc = PairtreeHelper.getDocFromUncleanId(uncleanId);
        assertEquals("uc2/pairtree_root/ar/k+/=1/39/60/=t/4q/j7/97/0f/ark+=13960=t4qj7970f/ark+=13960=t4qj7970f", doc.getDocumentPathPrefix());
    }

    @Test
    public void testGetRootPathFromUncleanId() throws Exception {
        String uncleanId = "uc2.ark:/13960/t4qj7970f";
        PairtreeHelper.PairtreeDocument doc = PairtreeHelper.getDocFromUncleanId(uncleanId);
        assertEquals("uc2/pairtree_root/ar/k+/=1/39/60/=t/4q/j7/97/0f/ark+=13960=t4qj7970f", doc.getDocumentRootPath());
    }

    @Test(expected = InvalidHtrcIdException.class)
    public void testGetPathFromCleanIdWithWrongId() throws Exception {
        String cleanId = "ark+=13960=t4qj7970f";
        PairtreeHelper.getDocFromCleanId(cleanId);
    }

    @Test(expected = InvalidHtrcIdException.class)
    public void testGetPathFromUncleanIdWithWrongId() throws Exception {
        String uncleanId = "ark:/13960/t4qj7970f";
        PairtreeHelper.getDocFromUncleanId(uncleanId);
    }
}