package File;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.AreaBreakType;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
import financialManagement.account.Account;
import financialManagement.account.AccountUtils;
import financialManagement.bookingRecord.BookingRecord;
import financialManagement.bookingRecord.BookingRecordUtils;
import financialManagement.kennzahl.BilanzKennzahlen;
import financialManagement.kennzahl.GuvKennzahlen;
import financialManagement.kennzahl.Kennzahl;
import financialManagement.statements.BilanzSum;
import financialManagement.statements.GuvSum;
import utils.Date;
import utils.Enums;
import utils.HelpFunctions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class ExportPDF {
    PdfFont headerFont = PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD);

    public ExportPDF(String file) throws IOException {
        Paragraph header = new Paragraph("Übersicht vom " + getDuration());
        header.setFont(headerFont);
        header.setTextAlignment(TextAlignment.CENTER);

        PdfWriter pdfWriter = new PdfWriter(file);
        PdfDocument pdfDocument = new PdfDocument(pdfWriter);
        Document document = new Document(pdfDocument);

        document.add(header);

        writeAllAccounts(document);

        document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
        writeBookingRecords(document);

        document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
        writeBilanz(document);

        document.add(new AreaBreak(AreaBreakType.NEXT_AREA));
        writeBilanzKennzahlen(document);

        document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
        writeGuV(document);

        document.add(new AreaBreak(AreaBreakType.NEXT_AREA));
        writeGUVKennzahlen(document);

        document.close();
    }

    public String getDuration() {
        if (BookingRecordUtils.GetBookingRecords().size() > 0) {
            return BookingRecordUtils.GetBookingRecords().get(0).getFormattedDate() + " - " + BookingRecordUtils.GetBookingRecords().get(BookingRecordUtils.GetBookingRecords().size() - 1).getFormattedDate();
        }
        return Date.getTodayDate() + " - " + Date.getTodayDate();
    }

    public void writeAllAccounts(Document document) {
        Paragraph header = new Paragraph("Kontenübersicht");
        header.setFont(headerFont);
        header.setFontSize(18);
        header.setTextAlignment(TextAlignment.CENTER);

        Table table = new Table(6);

        table.addHeaderCell(new Cell().add("Konto").setFont(headerFont).setTextAlignment(TextAlignment.CENTER));
        table.addHeaderCell(new Cell().add("Bezeichnung").setFont(headerFont).setTextAlignment(TextAlignment.CENTER));
        table.addHeaderCell(new Cell().add("Art").setFont(headerFont).setTextAlignment(TextAlignment.CENTER));
        table.addHeaderCell(new Cell().add("Konto Steuer").setFont(headerFont).setTextAlignment(TextAlignment.CENTER));
        table.addHeaderCell(new Cell().add("AB (in €)").setFont(headerFont).setTextAlignment(TextAlignment.CENTER));
        table.addHeaderCell(new Cell().add("Saldo (in €)").setFont(headerFont).setTextAlignment(TextAlignment.CENTER));

        for (Map.Entry<String, Account> accountEntry : AccountUtils.GetAccounts().entrySet()) {
            String[] values = AccountUtils.setTextFieldAccountValues(accountEntry.getKey(), accountEntry.getValue());
            table.addCell(new Cell().add(values[0]).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));
            ;
            table.addCell(new Cell().add(values[1]));
            table.addCell(new Cell().add(values[2]).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));
            table.addCell(new Cell().add(values[3]).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));
            table.addCell(new Cell().add(values[4]).setTextAlignment(TextAlignment.RIGHT).setVerticalAlignment(VerticalAlignment.MIDDLE));
            table.addCell(new Cell().add(values[5]).setTextAlignment(TextAlignment.RIGHT).setVerticalAlignment(VerticalAlignment.MIDDLE));
        }
        document.add(header);
        document.add(table);
    }

    public void writeBookingRecords(Document document) {
        Paragraph header = new Paragraph("Buchungssätze");
        header.setFont(headerFont);
        header.setFontSize(18);
        header.setTextAlignment(TextAlignment.CENTER);

        float columnWidth[] = {40f, 100f, 300f, 120f};
        Table table = new Table(columnWidth);
        table.setHorizontalAlignment(HorizontalAlignment.CENTER);

        table.addHeaderCell(new Cell().add("Nr").setFont(headerFont).setTextAlignment(TextAlignment.CENTER));
        table.addHeaderCell(new Cell().add("Datum").setFont(headerFont).setTextAlignment(TextAlignment.CENTER));
        table.addHeaderCell(new Cell().add("Buchungssatz").setFont(headerFont).setTextAlignment(TextAlignment.CENTER));
        table.addHeaderCell(new Cell().add("Betrag").setFont(headerFont).setTextAlignment(TextAlignment.CENTER));


        for (int i = 0; i < BookingRecordUtils.GetBookingRecordsWithAbschluss().size(); i++) {
            BookingRecord bookingRecord = BookingRecordUtils.GetBookingRecordsWithAbschluss().get(i);
            Cell bookingAccountCell = new Cell();
            Cell bookingAmountCell = new Cell();

            for (String accountText : bookingRecord.getFormattedBookingTextPdf()) {
                bookingAccountCell.add(new Paragraph(accountText));
            }

            for (String amountText : bookingRecord.getFormattedBookingAmountPdf()) {
                bookingAmountCell.add(new Paragraph(amountText));
            }

            table.addCell(new Cell().add((i + 1) + "").setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));
            table.addCell(new Cell().add(bookingRecord.getFormattedDate()).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));
            table.addCell(new Cell().add(bookingAccountCell).setTextAlignment(TextAlignment.LEFT));
            table.addCell(new Cell().add(bookingAmountCell)).setTextAlignment(TextAlignment.RIGHT);
        }
        document.add(header);
        document.add(table);
    }

    public void writeBilanz(Document document) {
        Paragraph header = new Paragraph("Bilanz");
        header.setFont(headerFont);
        header.setFontSize(18);
        header.setTextAlignment(TextAlignment.CENTER);

        Table table = new Table(2);
        table.setHorizontalAlignment(HorizontalAlignment.CENTER);

        table.addCell(new Cell().add("Aktiva").setFont(headerFont).setFontSize(16));
        table.addCell(new Cell().add("Passiva").setFont(headerFont).setFontSize(16).setTextAlignment(TextAlignment.RIGHT));

        table.addCell(new Cell().add("A Anlagevermögen " + HelpFunctions.Format(BilanzSum.getAnlage())).setFont(headerFont).setFontSize(14));
        table.addCell(new Cell().add("A Eigenkapital " + HelpFunctions.Format(BilanzSum.getEk())).setFont(headerFont).setFontSize(14));

        table.addCell(new Cell().add(getFormattedForm("05")));
        table.addCell(new Cell().add(getFormattedForm("300")));

        table.addCell(new Cell().add(getFormattedForm("07")));
        table.addCell(new Cell().add(getFormattedForm("3001")));

        table.addCell(new Cell().add(getFormattedForm("084")));
        table.addCell(new Cell().add(""));

        table.addCell(new Cell().add("B Umlaufvermögen " + HelpFunctions.Format(BilanzSum.getUmlauf())).setFont(headerFont).setFontSize(14));
        table.addCell(new Cell().add("B Fremdkapital " + HelpFunctions.Format(BilanzSum.getFk())).setFont(headerFont).setFontSize(14));

        table.addCell(new Cell().add(getFormattedForm("20")));
        table.addCell(new Cell().add(getFormattedForm("420")));

        table.addCell(new Cell().add(getFormattedForm("240")));
        table.addCell(new Cell().add(getFormattedForm("425")));

        table.addCell(new Cell().add(getFormattedForm("27")));
        table.addCell(new Cell().add(getFormattedForm("44")));

        table.addCell(new Cell().add(getFormattedForm("280")));
        table.addCell(new Cell().add(""));

        table.addCell(new Cell().add(getFormattedForm("288")));
        table.addCell(new Cell().add(""));

        table.addCell(new Cell().add(""));
        table.addCell(new Cell().add(""));

        table.addCell(new Cell().add("Summe Aktiva " + HelpFunctions.Format(BilanzSum.getAktivSumme())).setFont(headerFont).setFontSize(14));
        table.addCell(new Cell().add("Summe Passiva " + HelpFunctions.Format(BilanzSum.getPassivSumme())).setFont(headerFont).setFontSize(14));

        document.add(header);
        document.add(new Paragraph("\n\n"));
        document.add(table);
    }

    public void writeBilanzKennzahlen(Document document) {
        Paragraph header = new Paragraph("Kennzahlen der Bilanz");
        header.setFont(headerFont);
        header.setFontSize(18);
        header.setTextAlignment(TextAlignment.CENTER);

        Paragraph header2 = new Paragraph("Vertikale Bilanzkennzahlen");
        Paragraph header3 = new Paragraph("Kapitalstruktur");
        Paragraph header4 = new Paragraph("Vermögensstruktur");
        Paragraph header5 = new Paragraph("Horizontale Bilanzkennzahlen");
        Paragraph header6 = new Paragraph("Goldene Bilanzkennzahlen");
        Paragraph header7 = new Paragraph("Liquiditätsgrade");
        Paragraph header8 = new Paragraph("Working Capital");

        Table tableKS = new Table(2);
        Table tableVS = new Table(2);
        Table tableGB = new Table(2);
        Table tableLG = new Table(2);
        Table tableWC = new Table(2);

        Table[] tables = {tableKS, tableVS, tableGB, tableLG, tableWC};
        Paragraph[] boldHeaders = {header2, header5};
        Paragraph[] subHeaders = {header3, header4, header6, header7, header8};

        for (Paragraph h : boldHeaders) {
            h.setFont(headerFont);
            h.setFontSize(16);
            h.setTextAlignment(TextAlignment.CENTER);
        }
        for (Paragraph h : subHeaders) {
            h.setFontSize(14);
            h.setTextAlignment(TextAlignment.CENTER);
        }

        for (Table table : tables) {
            table.setHorizontalAlignment(HorizontalAlignment.CENTER);
            table.addCell(new Cell().add("Kennzahl").setFont(headerFont));
            table.addCell(new Cell().add("").setFont(headerFont));
        }

        addKennzahlToTable(BilanzKennzahlen.GetBilanzKennzahlen(), Enums.KennzahlType.QUOTE, tableKS);
        addKennzahlToTable(BilanzKennzahlen.GetBilanzKennzahlen(), Enums.KennzahlType.INTENSITY, tableVS);
        addKennzahlToTable(BilanzKennzahlen.GetBilanzKennzahlen(), Enums.KennzahlType.DG, tableGB);
        addKennzahlToTable(BilanzKennzahlen.GetBilanzKennzahlen(), Enums.KennzahlType.LG, tableLG);
        addKennzahlToTable(BilanzKennzahlen.GetBilanzKennzahlen(), Enums.KennzahlType.WC, tableWC);

        document.add(header);
        document.add(new Paragraph("\n\n"));
        document.add(header2);
        document.add(header3);
        document.add(tableKS);
        document.add(header4);
        document.add(tableVS);
        document.add(new Paragraph("\n\n"));
        document.add(header5);
        document.add(header6);
        document.add(tableGB);
        document.add(header7);
        document.add(tableLG);
        document.add(header8);
        document.add(tableWC);
    }


    public void writeGuV(Document document) {
        Paragraph header = new Paragraph("Gewinn- und Verlustrechnung");
        header.setFont(headerFont);
        header.setFontSize(18);
        header.setTextAlignment(TextAlignment.CENTER);

        Table table = new Table(2);
        table.setHorizontalAlignment(HorizontalAlignment.CENTER);
        table.addCell(new Cell().add("Soll").setFont(headerFont).setFont(headerFont).setFontSize(16));
        table.addCell(new Cell().add("Haben").setFont(headerFont).setFont(headerFont).setFontSize(16).setTextAlignment(TextAlignment.RIGHT));

        table.addCell(new Cell().add(getFormattedForm("50")));
        table.addCell(new Cell().add(getFormattedForm("60")));

        table.addCell(new Cell().add(getFormattedForm("540")));
        table.addCell(new Cell().add(getFormattedForm("616")));

        table.addCell(new Cell().add(getFormattedForm("542")));
        table.addCell(new Cell().add(getFormattedForm("62")));

        table.addCell(new Cell().add(getFormattedForm("56")));
        table.addCell(new Cell().add(getFormattedForm("64")));

        table.addCell(new Cell().add(getFormattedForm("571")));
        table.addCell(new Cell().add(getFormattedForm("65")));

        table.addCell(new Cell().add(""));
        table.addCell(new Cell().add(getFormattedForm("670")));

        table.addCell(new Cell().add(""));
        table.addCell(new Cell().add(getFormattedForm("677")));

        table.addCell(new Cell().add(""));
        table.addCell(new Cell().add(getFormattedForm("68")));

        table.addCell(new Cell().add(""));
        table.addCell(new Cell().add(getFormattedForm("70")));

        table.addCell(new Cell().add(""));
        table.addCell(new Cell().add(getFormattedForm("75")));

        table.addCell(new Cell().add("Verlust " + HelpFunctions.Format(GuvSum.calculateLoose())));
        table.addCell(new Cell().add("Gewinn " + HelpFunctions.Format(GuvSum.calculateWin())));

        table.addCell(new Cell().add(""));
        table.addCell(new Cell().add(""));

        table.addCell(new Cell().add("Summe Soll " + HelpFunctions.Format((GuvSum.getIncomeSum() + GuvSum.calculateLoose()))).setFont(headerFont).setFontSize(14));
        table.addCell(new Cell().add("Summe Haben " + HelpFunctions.Format((GuvSum.getExpensesSum() + GuvSum.calculateWin()))).setFont(headerFont).setFontSize(14));

        document.add(header);
        document.add(new Paragraph("\n\n"));
        document.add(table);
    }

    public String getFormattedForm(String key) {
        return key + " " + AccountUtils.GetAccounts().get(key).getAccountName() + " " + AccountUtils.GetAccounts().get(key).getFormattedAccountBalance();
    }

    public void writeGUVKennzahlen(Document document) {
        Paragraph header = new Paragraph("Kennzahlen der Gewinn- und Verlustrechnung");
        header.setFont(headerFont);
        header.setFontSize(18);
        header.setTextAlignment(TextAlignment.CENTER);

        Paragraph header2 = new Paragraph("Jahresüberschuss");
        Paragraph header3 = new Paragraph("Kostenanteile");
        Paragraph header4 = new Paragraph("Umschlagskennzahlen");

        Table tableJU = new Table(2);
        Table tableKA = new Table(2);
        Table tableUZ = new Table(2);

        Paragraph[] boldHeaders = {header2, header3, header4};
        Table[] tables = {tableJU, tableKA, tableUZ};

        for (Paragraph h : boldHeaders) {
            h.setFont(headerFont);
            h.setFontSize(16);
            h.setTextAlignment(TextAlignment.CENTER);
        }

        for (Table table : tables) {
            table.setHorizontalAlignment(HorizontalAlignment.CENTER);
            table.addCell(new Cell().add("Kennzahl").setFont(headerFont));
            table.addCell(new Cell().add("").setFont(headerFont));
        }

        addKennzahlToTable(GuvKennzahlen.getGuvKennzahlen(), Enums.KennzahlType.ANNUAL, tableJU);
        addKennzahlToTable(GuvKennzahlen.getGuvKennzahlen(), Enums.KennzahlType.COSTPORTION, tableKA);
        addKennzahlToTable(GuvKennzahlen.getGuvKennzahlen(), Enums.KennzahlType.UMSCHLAG, tableUZ);

        document.add(header);
        document.add(new Paragraph("\n\n"));
        document.add(header2);
        document.add(tableJU);
        document.add(new Paragraph("\n\n"));
        document.add(header3);
        document.add(tableKA);
        document.add(new Paragraph("\n\n"));
        document.add(header4);
        document.add(tableUZ);
    }

    private void addKennzahlToTable(ArrayList<Kennzahl> kennzahlen, Enums.KennzahlType type, Table table) {
        for (Kennzahl kennzahl : kennzahlen) {
            if (kennzahl.getType().equals(type)) {
                table.addCell(new Cell().add(kennzahl.getName()));
                table.addCell(new Cell().add(kennzahl.FormattedAmount).setTextAlignment(TextAlignment.RIGHT));
            }
        }
    }

}
