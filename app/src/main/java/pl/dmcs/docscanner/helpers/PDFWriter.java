package pl.dmcs.docscanner.helpers;

import android.graphics.Rect;
import android.graphics.pdf.PdfDocument;
import android.graphics.pdf.PdfDocument.Page;
import android.graphics.pdf.PdfDocument.PageInfo;
import android.os.Environment;
import android.view.View;

import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
            PDFont font = PDType1Font.HELVETICA;
            float fontSize = 14;
            float leading = fontSize * 1.5f;

            PDRectangle mediaBox = page1.findMediaBox();
            float margin = 50;
            float width = mediaBox.getWidth() - 2*margin;
            float startX = mediaBox.getLowerLeftX() + margin;
            float startY = mediaBox.getUpperRightY() - margin;

            PDPageContentStream contentStream = new PDPageContentStream(pdfDoc, page1);

            contentStream.beginText();
            contentStream.setFont(font, fontSize);
            contentStream.moveTextPositionByAmount(startX, startY);
            for (String line : splitToLines(text, width, fontSize, font)) {
                contentStream.drawString(line);
                contentStream.moveTextPositionByAmount(0, -leading);
            }
            contentStream.endText();
            contentStream.close();

            File file = new File(dir.getAbsolutePath() + "/" + fileName);

            pdfDoc.save(file);
            pdfDoc.close();

            return file;

        } catch (COSVisitorException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static List<String> splitToLines(String text, float contentWidth, float fontSize, PDFont font) throws IOException {

        List<String> lines = new ArrayList<String>();
        int lastSpace = -1;
        while (text.length() > 0) {
            int spaceIndex = text.indexOf(' ', lastSpace + 1);
            if (spaceIndex < 0) {
                lines.add(text);
                text = "";
            } else {
                String subString = text.substring(0, spaceIndex);
                float size = fontSize * font.getStringWidth(subString) / 1000;
                if (size > contentWidth) {
                    if (lastSpace < 0)
                        lastSpace = spaceIndex;
                    subString = text.substring(0, lastSpace);
                    lines.add(subString);
                    text = text.substring(lastSpace).trim();
                    lastSpace = -1;
                } else {
                    lastSpace = spaceIndex;
                }
            }
        }
        return lines;
    }

}
