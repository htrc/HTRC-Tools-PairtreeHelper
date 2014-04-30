package edu.illinois.i3.htrc.tools;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PairtreeHelperTest {

    @Test
    public void testParseSuccessFullPath() throws Exception {
        String docPath = "/NGPD/uc2/pairtree_root/ar/k+/=1/39/60/=t/4q/j7/97/0f/ark+=13960=t4qj7970f/ark+=13960=t4qj7970f.zip";
        PairtreeHelper.PairtreeDocument document = PairtreeHelper.parse(docPath);
        assertEquals(document.getDocumentPath(), docPath);
        assertEquals(document.getPpath(), "ar/k+/=1/39/60/=t/4q/j7/97/0f/ark+=13960=t4qj7970f");
        assertEquals(document.getLibraryId(), "uc2");
        assertEquals(document.getCleanId(), "uc2.ark+=13960=t4qj7970f");
        assertEquals(document.getCleanIdWithoutLibId(), "ark+=13960=t4qj7970f");
        assertEquals(document.getUncleanId(), "uc2.ark:/13960/t4qj7970f");
        assertEquals(document.getUncleanIdWithoutLibId(), "ark:/13960/t4qj7970f");
    }

    @Test
    public void testParseSuccessRootPath() throws Exception {
        String docPath = "/uc2/pairtree_root/ar/k+/=1/39/60/=t/4q/j7/97/0f/ark+=13960=t4qj7970f/ark+=13960=t4qj7970f.zip";
        PairtreeHelper.PairtreeDocument document = PairtreeHelper.parse(docPath);
        assertEquals(document.getDocumentPath(), docPath);
        assertEquals(document.getPpath(), "ar/k+/=1/39/60/=t/4q/j7/97/0f/ark+=13960=t4qj7970f");
        assertEquals(document.getLibraryId(), "uc2");
        assertEquals(document.getCleanId(), "uc2.ark+=13960=t4qj7970f");
        assertEquals(document.getCleanIdWithoutLibId(), "ark+=13960=t4qj7970f");
        assertEquals(document.getUncleanId(), "uc2.ark:/13960/t4qj7970f");
        assertEquals(document.getUncleanIdWithoutLibId(), "ark:/13960/t4qj7970f");
    }

    @Test
    public void testParseSuccessRelativePath() throws Exception {
        String docPath = "uc2/pairtree_root/ar/k+/=1/39/60/=t/4q/j7/97/0f/ark+=13960=t4qj7970f/ark+=13960=t4qj7970f.zip";
        PairtreeHelper.PairtreeDocument document = PairtreeHelper.parse(docPath);
        assertEquals(document.getDocumentPath(), docPath);
        assertEquals(document.getPpath(), "ar/k+/=1/39/60/=t/4q/j7/97/0f/ark+=13960=t4qj7970f");
        assertEquals(document.getLibraryId(), "uc2");
        assertEquals(document.getCleanId(), "uc2.ark+=13960=t4qj7970f");
        assertEquals(document.getCleanIdWithoutLibId(), "ark+=13960=t4qj7970f");
        assertEquals(document.getUncleanId(), "uc2.ark:/13960/t4qj7970f");
        assertEquals(document.getUncleanIdWithoutLibId(), "ark:/13960/t4qj7970f");
    }

    @Test(expected = InvalidPairtreePathException.class)
    public void testParseFailure() throws InvalidPairtreePathException {
        String wrongPath = "/ar/k+/=1/39/60/=t/4q/j7/97/0f/ark+=13960=t4qj7970f/ark+=13960=t4qj7970f.zip";
        PairtreeHelper.parse(wrongPath);
    }

    @Test
    public void testGetPathFromCleanId() throws Exception {
        String cleanId = "uc2.ark+=13960=t4qj7970f";
        String path = PairtreeHelper.getPathFromCleanId(cleanId);
        assertEquals("uc2/pairtree_root/ar/k+/=1/39/60/=t/4q/j7/97/0f/ark+=13960=t4qj7970f/", path);
    }

    @Test
    public void testGetPathFromUncleanId() throws Exception {
        String uncleanId = "uc2.ark:/13960/t4qj7970f";
        String path = PairtreeHelper.getPathFromUncleanId(uncleanId);
        assertEquals("uc2/pairtree_root/ar/k+/=1/39/60/=t/4q/j7/97/0f/ark+=13960=t4qj7970f/", path);
    }

    @Test(expected = InvalidHtrcIdException.class)
    public void testGetPathFromCleanIdWithWrongId() throws Exception {
        String cleanId = "ark+=13960=t4qj7970f";
        PairtreeHelper.getPathFromCleanId(cleanId);
    }

    @Test(expected = InvalidHtrcIdException.class)
    public void testGetPathFromUncleanIdWithWrongId() throws Exception {
        String uncleanId = "ark:/13960/t4qj7970f";
        PairtreeHelper.getPathFromUncleanId(uncleanId);
    }
}