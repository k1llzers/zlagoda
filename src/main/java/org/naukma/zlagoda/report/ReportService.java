package org.naukma.zlagoda.report;

import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;

import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.naukma.zlagoda.category.CategoryRepository;
import org.naukma.zlagoda.check.CheckEntity;
import org.naukma.zlagoda.check.CheckRepository;
import org.naukma.zlagoda.customercard.CustomerCardEntity;
import org.naukma.zlagoda.customercard.CustomerCardRepository;
import org.naukma.zlagoda.employee.EmployeeRepository;
import org.naukma.zlagoda.exception.NoSuchEntityException;
import org.naukma.zlagoda.product.ProductRepository;
import org.naukma.zlagoda.storeproduct.StoreProductRepository;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReportService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final EmployeeRepository employeeRepository;
    private final CustomerCardRepository customerCardRepository;
    private final StoreProductRepository storeProductRepository;
    private final CheckRepository checkRepository;

    @SneakyThrows
    public Resource report(ReportType type) {
        File report = new File("report.pdf");
        PdfDocument pdfDoc
                = new PdfDocument(new PdfWriter(report));
        Document doc = new Document(pdfDoc);
        doc.add(new Paragraph(type.name().charAt(0)
                + type.name().toLowerCase().replace("_", " ").substring(1))
                .setTextAlignment(TextAlignment.CENTER).setFontSize(17));
        doc.add(getTableAccordingToType(type));
        doc.close();
        pdfDoc.close();
        return new PathResource(report.toPath());
    }

    @SneakyThrows
    public Resource report(Integer id) {
        File report = new File("report.pdf");
        PdfDocument pdfDoc
                = new PdfDocument(new PdfWriter(report));
        Document doc = new Document(pdfDoc);
        doc.add(checkTable(id));
        doc.close();
        pdfDoc.close();
        return new PathResource(report.toPath());
    }

    private IBlockElement getTableAccordingToType(ReportType type) {
        switch (type) {
            case PRODUCTS -> {return productTable();}
            case CATEGORIES -> {return  categoryTable();}
            case EMPLOYEES -> {return employeesTable();}
            case CUSTOMER_CARDS -> {return customerCardsTable();}
            case STORE_PRODUCTS -> {return storeProductsTable();}
            case CHECKS -> {return getAllChecks();}
        }
        throw new RuntimeException("Incorrect type!");
    }

    @SneakyThrows
    private Table productTable() {
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

    @SneakyThrows
    private Table categoryTable() {
        PdfFont f1 = PdfFontFactory.createFont("FreeSans.ttf", "Cp1251");
        Table table = new Table(2);
        table.setHorizontalAlignment(HorizontalAlignment.CENTER);
        table.addCell(new Cell().add(new Paragraph("ID")).setTextAlignment(TextAlignment.CENTER));
        table.addCell(new Cell().add(new Paragraph("Name")).setTextAlignment(TextAlignment.CENTER));
        categoryRepository.findAll()
                .forEach(category -> {
                    table.addCell(new Cell().add(new Paragraph(category.getId().toString())).setFont(f1).setTextAlignment(TextAlignment.CENTER));
                    table.addCell(new Cell().add(new Paragraph(category.getName())).setFont(f1).setMaxWidth(100));
                });
        return table;
    }

    @SneakyThrows
    private Table customerCardsTable() {
        PdfFont f1 = PdfFontFactory.createFont("FreeSans.ttf", "Cp1251");
        Table table = new Table(5);
        table.setFontSize(12);
        table.setHorizontalAlignment(HorizontalAlignment.CENTER);
        table.addCell(new Cell().add(new Paragraph("ID")).setTextAlignment(TextAlignment.CENTER));
        table.addCell(new Cell().add(new Paragraph("Name")).setTextAlignment(TextAlignment.CENTER));
        table.addCell(new Cell().add(new Paragraph("Phone Number")).setTextAlignment(TextAlignment.CENTER));
        table.addCell(new Cell().add(new Paragraph("Address")).setTextAlignment(TextAlignment.CENTER));
        table.addCell(new Cell().add(new Paragraph("Percent")).setTextAlignment(TextAlignment.CENTER));
        customerCardRepository.findAll()
                .forEach(customerCard -> {
                    table.addCell(new Cell().add(new Paragraph(customerCard.getId().toString())).setFont(f1).setTextAlignment(TextAlignment.CENTER));
                    table.addCell(new Cell().add(new Paragraph(customerCard.getFullName())).setFont(f1).setMaxWidth(200));
                    table.addCell(new Cell().add(new Paragraph(customerCard.getPhoneNumber())).setFont(f1));
                    table.addCell(new Cell().add(new Paragraph((customerCard.getCity() == null ? "" : customerCard.getCity()) + (customerCard.getStreet() == null ? "" : ", " + customerCard.getStreet()) + (customerCard.getZipCode() == null ? "" : ", " + customerCard.getZipCode()))).setFont(f1).setMaxWidth(200));
                    table.addCell(new Cell().add(new Paragraph(customerCard.getPercent().toString() + '%')).setFont(f1));
                });
        return table;
    }

    @SneakyThrows
    private Table employeesTable() {
        PdfFont f1 = PdfFontFactory.createFont("FreeSans.ttf", "Cp1251");
        Table table = new Table(8);
        table.setFontSize(10);
        table.setHorizontalAlignment(HorizontalAlignment.CENTER);
        table.addCell(new Cell().add(new Paragraph("ID")).setTextAlignment(TextAlignment.CENTER));
        table.addCell(new Cell().add(new Paragraph("Name")).setTextAlignment(TextAlignment.CENTER));
        table.addCell(new Cell().add(new Paragraph("Role")).setTextAlignment(TextAlignment.CENTER));
        table.addCell(new Cell().add(new Paragraph("Salary")).setTextAlignment(TextAlignment.CENTER));
        table.addCell(new Cell().add(new Paragraph("Birth Date")).setTextAlignment(TextAlignment.CENTER));
        table.addCell(new Cell().add(new Paragraph("Start Date")).setTextAlignment(TextAlignment.CENTER));
        table.addCell(new Cell().add(new Paragraph("Phone Number")).setTextAlignment(TextAlignment.CENTER));
        table.addCell(new Cell().add(new Paragraph("Address")).setTextAlignment(TextAlignment.CENTER));
        employeeRepository.findAll()
                .forEach(employee -> {
                    table.addCell(new Cell().add(new Paragraph(employee.getId().toString())).setFont(f1).setTextAlignment(TextAlignment.CENTER));
                    table.addCell(new Cell().add(new Paragraph(employee.getFullName())).setFont(f1).setMaxWidth(200));
                    table.addCell(new Cell().add(new Paragraph(employee.getRole().name().toLowerCase())).setFont(f1));
                    table.addCell(new Cell().add(new Paragraph(employee.getSalary().toBigInteger().toString())).setFont(f1));
                    table.addCell(new Cell().add(new Paragraph(employee.getDateOfBirth().toString())).setFont(f1));
                    table.addCell(new Cell().add(new Paragraph(employee.getDateOfStart().toString())).setFont(f1));
                    table.addCell(new Cell().add(new Paragraph(employee.getPhoneNumber())).setFont(f1));
                    table.addCell(new Cell().add(new Paragraph(employee.getCity() + ", " + employee.getStreet() + ", " + employee.getZipCode())).setFont(f1).setMaxWidth(200));
                });
        return table;
    }

    @SneakyThrows
    private Table storeProductsTable() {
        PdfFont f1 = PdfFontFactory.createFont("FreeSans.ttf", "Cp1251");
        Table table = new Table(5);
        table.setFontSize(12);
        table.setHorizontalAlignment(HorizontalAlignment.CENTER);
        table.addCell(new Cell().add(new Paragraph("UPC")).setTextAlignment(TextAlignment.CENTER));
        table.addCell(new Cell().add(new Paragraph("Name")).setTextAlignment(TextAlignment.CENTER));
        table.addCell(new Cell().add(new Paragraph("Price")).setTextAlignment(TextAlignment.CENTER));
        table.addCell(new Cell().add(new Paragraph("Product number")).setTextAlignment(TextAlignment.CENTER));
        table.addCell(new Cell().add(new Paragraph("Promotion")).setTextAlignment(TextAlignment.CENTER));
        storeProductRepository.findAll()
                .forEach(employee -> {
                    table.addCell(new Cell().add(new Paragraph(employee.getId().toString())).setFont(f1).setTextAlignment(TextAlignment.CENTER));
                    table.addCell(new Cell().add(new Paragraph(employee.getProduct().getName())).setFont(f1).setTextAlignment(TextAlignment.CENTER));
                    table.addCell(new Cell().add(new Paragraph(employee.getSellingPrice().setScale(2, BigDecimal.ROUND_UP).toString())).setFont(f1).setTextAlignment(TextAlignment.CENTER));
                    table.addCell(new Cell().add(new Paragraph(employee.getProductsNumber().toString())).setFont(f1).setTextAlignment(TextAlignment.CENTER));
                    table.addCell(new Cell().add(new Paragraph(employee.isPromotional() + "")).setFont(f1).setTextAlignment(TextAlignment.CENTER));
                });
        return table;
    }

    @SneakyThrows
    private IBlockElement checkTable(Integer id) {
        PdfFont f1 = PdfFontFactory.createFont("FreeSans.ttf", "Cp1251");
        CheckEntity checkEntity = checkRepository.findById(id).orElseThrow(
                () -> new NoSuchEntityException("Can't find check by id: " + id)
        );
        Div div = new Div();
        div.setMaxWidth(250);
        div.setPadding(10);
        div.setBorder(new SolidBorder(1));
        div.setHorizontalAlignment(HorizontalAlignment.CENTER);
        div.add(new Paragraph("Check №" + id).setTextAlignment(TextAlignment.CENTER).setFont(f1).setFontSize(15));
        if (checkEntity.getCustomerCard() != null)
            div.add(new Paragraph("Customer: " + checkEntity.getCustomerCard().getFullName())).setFont(f1);
        div.add(new Paragraph("Cashier: " + checkEntity.getEmployee().getFullName()).setFont(f1));
        div.add(new Paragraph("Print date: " + checkEntity.getPrintDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).setFont(f1));
        Table table = new Table(5);
        table.setFontSize(12);
        table.setHorizontalAlignment(HorizontalAlignment.CENTER);
        table.addCell(new Cell().add(new Paragraph("UPC")).setTextAlignment(TextAlignment.CENTER));
        table.addCell(new Cell().add(new Paragraph("Name")).setTextAlignment(TextAlignment.CENTER));
        table.addCell(new Cell().add(new Paragraph("Price")).setTextAlignment(TextAlignment.CENTER));
        table.addCell(new Cell().add(new Paragraph("Product number")).setTextAlignment(TextAlignment.CENTER));
        table.addCell(new Cell().add(new Paragraph("Cost")).setTextAlignment(TextAlignment.CENTER));
        checkEntity.getSales()
                .forEach(sale -> {
                    table.addCell(new Cell().add(new Paragraph(sale.getId().getStoreProduct().getId().toString())).setFont(f1).setTextAlignment(TextAlignment.CENTER));
                    table.addCell(new Cell().add(new Paragraph(sale.getId().getStoreProduct().getProduct().getName())).setFont(f1).setTextAlignment(TextAlignment.CENTER));
                    table.addCell(new Cell().add(new Paragraph(sale.getSellingPrice().setScale(2, BigDecimal.ROUND_UP).toString())).setFont(f1).setTextAlignment(TextAlignment.CENTER));
                    table.addCell(new Cell().add(new Paragraph(sale.getProductNumber() + "")).setFont(f1).setTextAlignment(TextAlignment.CENTER));
                    table.addCell(new Cell().add(new Paragraph(sale.getSellingPrice().multiply(BigDecimal.valueOf(sale.getProductNumber())).setScale(2, BigDecimal.ROUND_UP).toString())).setFont(f1).setTextAlignment(TextAlignment.CENTER));
                });
        table.setMarginBottom(10);
        table.setMarginTop(10);
        div.add(table);
        div.add(new Paragraph("Total sum: " + checkEntity.getSumTotal().setScale(2, BigDecimal.ROUND_DOWN).toString()));
        div.add(new Paragraph("Vat: " + checkEntity.getVat().setScale(2, BigDecimal.ROUND_DOWN).toString()));
        return div;
    }

    @SneakyThrows
    private IBlockElement getAllChecks() {
        Div div = new Div();
        div.setMaxWidth(250);
        div.setHorizontalAlignment(HorizontalAlignment.CENTER);
        checkRepository.findAll().forEach(check -> div.add(checkTable(check.getId())));
        return div;
    }
}
