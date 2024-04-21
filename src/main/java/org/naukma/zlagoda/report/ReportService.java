package org.naukma.zlagoda.report;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;

import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.naukma.zlagoda.product.ProductRepository;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Files;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReportService {
    private final ProductRepository productRepository;

    @SneakyThrows
    public Resource productReport() {
        File report = new File("report.pdf");
        PdfDocument pdfDoc
                = new PdfDocument(new PdfWriter(report));
        Document doc = new Document(pdfDoc);
        doc.add(new Paragraph("Products").setTextAlignment(TextAlignment.CENTER).setFontSize(20));
        doc.add(productTable());
        doc.close();
        pdfDoc.close();
        return new PathResource(report.toPath());
    }

    @SneakyThrows
    public Table productTable() {
        PdfFont f1 = PdfFontFactory.createFont("FreeSans.ttf", "Cp1251");
        Table table = new Table(4);
        table.setHorizontalAlignment(HorizontalAlignment.CENTER);
        table.addCell(new Cell().add(new Paragraph("ID")).setTextAlignment(TextAlignment.CENTER));
        table.addCell(new Cell().add(new Paragraph("Name")).setTextAlignment(TextAlignment.CENTER));
        table.addCell(new Cell().add(new Paragraph("Category")).setTextAlignment(TextAlignment.CENTER));
        table.addCell(new Cell().add(new Paragraph("Characteristics")).setTextAlignment(TextAlignment.CENTER));
        productRepository.findAll()
                .forEach(product -> {
                    table.addCell(new Cell().add(new Paragraph(product.getId().toString())).setFont(f1).setTextAlignment(TextAlignment.CENTER));
                    table.addCell(new Cell().add(new Paragraph(product.getName())).setFont(f1));
                    table.addCell(new Cell().add(new Paragraph(product.getCategory().getName())).setFont(f1));
                    table.addCell(new Cell().add(new Paragraph(product.getCharacteristics())).setFont(f1).setMaxWidth(300));
                });
        return table;
    }
}
