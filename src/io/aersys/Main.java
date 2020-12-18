package io.aersys;

import javax.imageio.ImageIO;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.print.*;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

class AersysPrinter implements Printable {

    @Override
    public int print(Graphics _g, PageFormat page, int pg) {
        if (pg > 0) {
            return NO_SUCH_PAGE;
        }

        Graphics2D g = (Graphics2D)_g;
        for (int i = 0; i < 4; i++) {
            BufferedImage barcode;
            try {
                 barcode = ImageIO.read(new File((116 + i) + ".png"));
            } catch (Exception e) { e.printStackTrace(); continue; }
            g.drawImage(barcode, (int)(page.getImageableWidth() - barcode.getWidth()) / 2, (int)(((1.5 * 72 - barcode.getHeight()) / 2) + (i * 1.5) * 72), null);
        }
        return PAGE_EXISTS;
    }
}

public class Main {
    public static void main(String[] args) throws IOException {
        PrinterJob pj = PrinterJob.getPrinterJob();
        PageFormat pageFormat = pj.defaultPage();
        Paper labelPaper = new Paper();
        labelPaper.setSize(4 * 72, 6 * 72);
        labelPaper.setImageableArea(0, 0, 4 * 72, 6 * 72);
        pageFormat.setPaper(labelPaper);
        pageFormat = pj.validatePage(pageFormat);
        Book pBook = new Book();
        pBook.append(new AersysPrinter(), pageFormat);
        pBook.append(new AersysPrinter(), pageFormat);
        pj.setPageable(pBook);
        pj.printDialog();
        try {
            pj.print();
        } catch (Exception e) { e.printStackTrace(); }
    }
}
