package pl.dmcs.docscanner.helpers;

import android.os.Environment;

import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.File;
import java.io.IOException;

/**
 * Created by mzalewski on 03.01.2015.
 */
public class PDFWriter {

    public static File createFile(String text, String fileName) throws IOException {

        File root = Environment.getExternalStorageDirectory();
        File dir = new File (root.getAbsolutePath() + "/docscanner");
        dir.mkdirs();

        try {

            PDDocument pdfDoc = new PDDocument();
            PDPage page1 = new PDPage();
            pdfDoc.addPage(page1);
            PDFont font = PDType1Font.TIMES_ROMAN;

            PDPageContentStream contentStream = new PDPageContentStream(pdfDoc, page1);
            contentStream.beginText();
            contentStream.setFont(font, 12);
            contentStream.moveTextPositionByAmount( 100, 100 );
            contentStream.drawString(text);
            contentStream.endText();
            contentStream.close();

            File file = new File(dir.getAbsolutePath() + "/" + fileName);

            pdfDoc.save(file);
            pdfDoc.close();

            return file;

        } catch (COSVisitorException e) {
            e.printStackTrace();
            return null;
        }
    }

}
