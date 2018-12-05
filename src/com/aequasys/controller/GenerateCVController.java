package com.aequasys.controller;

import com.aequasys.eventsClasses.HeaderFooterPageEvent;
import com.aequasys.eventsClasses.IntField;
import com.aequasys.model.dao.jdbc.JDBCCertificationDao;
import com.aequasys.model.dao.jdbc.JDBCMasteryDao;
import com.aequasys.model.dao.jdbc.JDBCQualificationDao;
import com.aequasys.model.dao.jdbc.JDBCReportDao;
import com.aequasys.model.vo.*;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;

public class GenerateCVController {
    public boolean button_pressed;
    public TextField years_textfield;
    public ChoiceBox<String> choiceBox;
    public Button choose_folder_btn;
    public Button cancel_btn;
    private User user_selected;

    ObservableList listofTemplates = FXCollections.observableArrayList();

    public void init_user(User user){
        user_selected = user;
        IntField.convertToIntField(years_textfield);
        years_textfield.setText("5");
        listofTemplates.add("template 1");
        listofTemplates.add("template 2");
        choiceBox.setItems(listofTemplates);
        choiceBox.setValue("template 1");


    }

    private void addTextCell(PdfPTable table,String header,BaseColor baseColor,int elementAlign,Font font) {
        PdfPCell cell = new PdfPCell(new Paragraph(header,font));
        cell.setBackgroundColor(baseColor);
        cell.setHorizontalAlignment(elementAlign);
        table.addCell(cell);
    }

    private void createTitle(PdfPCell secondTableCell, String Title) {
        PdfPTable titleTable = new PdfPTable(1);
        titleTable.setWidthPercentage(85.0f);
        Paragraph parTitle = new Paragraph(Title, bold_font(14));
        parTitle.setAlignment(Element.ALIGN_CENTER);
        PdfPCell titleCell = new PdfPCell(parTitle);
        titleCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        titleCell.setBorder(Rectangle.TOP | Rectangle.BOTTOM);
        titleCell.setBorderColor(BaseColor.GRAY);
        titleTable.addCell(titleCell);
        secondTableCell.addElement(titleTable);
        secondTableCell.addElement(new Paragraph("\n"));
    }

    private void userDetails(User user, Font bold_size10, Font underline_size9, PdfPTable mainTable, PdfPCell firstTableCell, PdfPTable firstTable) {
        Paragraph name = new Paragraph(user.getName(),bold_size10);
        addParagraphAlignCenter(firstTableCell, name);

        Paragraph surname = new Paragraph(user.getSurname(),bold_size10);
        addParagraphAlignCenter(firstTableCell, surname);

        Paragraph position = new Paragraph(user.getPosition(),bold_size10);
        addParagraphAlignCenter(firstTableCell, position);

        Paragraph email = new Paragraph(user.getEmail(),underline_size9);
        addParagraphAlignCenter(firstTableCell, email);

        Paragraph site = new Paragraph("www.aequasys.com",underline_size9);
        addParagraphAlignCenter(firstTableCell, site);

        firstTableCell.addElement(firstTable);
        mainTable.addCell(firstTableCell);
    }

    private void addParagraphAlignCenter(PdfPCell firstTableCell, Paragraph surname) {
        surname.setAlignment(Element.ALIGN_CENTER);
        firstTableCell.addElement(surname);
        firstTableCell.addElement(new Paragraph("\n"));
    }

    private Font bold_font(int  size){
        return  new Font(Font.FontFamily.HELVETICA,size,Font.BOLD);
    }

    private Font underline_font(int size){
        return new Font(Font.FontFamily.HELVETICA,size,Font.UNDERLINE);
    }

    private Font normal_font(int size){
        return  new Font(Font.FontFamily.HELVETICA,size,Font.NORMAL);
    }

    private void generateReportForUser(User user) {
        if (user!=null){
            float left = 10;
            float right = 10;
            float top = 90;
            float bottom = 80;
            Document document = new Document(PageSize.A4,left,right,top,bottom);
            String pathToFile;

            try{
                DirectoryChooser directoryChooser = new DirectoryChooser();
                File selectedDirectory = directoryChooser.showDialog(choose_folder_btn.getScene().getWindow());
                if(selectedDirectory == null){
                    pathToFile = new JFileChooser().getFileSystemView().getDefaultDirectory().toString();
                }else{
                    pathToFile = selectedDirectory.getAbsolutePath();
                }
                PdfWriter pdfWriter = PdfWriter.getInstance(document,new FileOutputStream(new File(pathToFile,user.getName()+" "+
                        user.getSurname()+" CV "+".pdf")));
                HeaderFooterPageEvent event1 = new HeaderFooterPageEvent();
                pdfWriter.setPageEvent(event1);
                document.open();
                document.setMargins(left, right, top, bottom);
                PdfPTable mainTable = new PdfPTable(2);
                mainTable.setWidthPercentage(100.0f);
                mainTable.setWidths(new int[]{2,5});
                //mainTable.setKeepTogether(false);
                // left side first table
                PdfPCell firstTableCell = new PdfPCell();
                firstTableCell.setBorder(PdfPCell.NO_BORDER);
                PdfPTable firstTable = new PdfPTable(1);

                //......... add some cells here ..........

                //add Logo
                Image image = Image.getInstance(getClass().getResource("/com/aequasys/resources/logo.png"));
                image.scaleToFit(90,90);
                image.setAlignment(Element.ALIGN_CENTER);
                firstTableCell.addElement(image);

                userDetails(user, bold_font(10), underline_font(8), mainTable, firstTableCell, firstTable);

                // Second table
                PdfPCell secondTableCell = new PdfPCell();
                secondTableCell.setBorder(PdfPCell.NO_BORDER);
                //PdfPTable secondTable = new PdfPTable(1);
                //......... add some cells here ...........

                //certifiation title
                createTitle(secondTableCell, "CERTIFICATION");

                //certification table init
                PdfPTable certificationTable = new PdfPTable(2);
                certificationTable.setWidths(new int[]{1, 8});

                //certification table headers
                addTextCell(certificationTable,"Year", BaseColor.GRAY,Element.ALIGN_CENTER,bold_font(10));
                addTextCell(certificationTable,"Qualification/Diploma",BaseColor.GRAY,Element.ALIGN_CENTER,bold_font(10));

                //table content
                JDBCCertificationDao jdbcCertificationDao = new JDBCCertificationDao();
                for (Certification certification:jdbcCertificationDao.select(user.getId())) {
                    addTextCell(certificationTable, String.valueOf(certification.getYear()), BaseColor.WHITE, Element.ALIGN_CENTER,normal_font(9));
                    addTextCell(certificationTable, certification.getQualification(), BaseColor.WHITE, Element.ALIGN_CENTER,normal_font(9));
                }

                //IMPORTANT add the table as an element to the 2nd table cell of the main celll
                secondTableCell.addElement(certificationTable);
                secondTableCell.addElement(new Paragraph("\n"));

                //qualification title
                createTitle(secondTableCell, "QUALIFICATION");

                //qualification table init
                PdfPTable qualificationTable = new PdfPTable(2);
                qualificationTable.setWidths(new int[]{1, 8});

                //qualification table headers
                addTextCell(qualificationTable,"Year",BaseColor.GRAY,Element.ALIGN_BOTTOM,bold_font(10));
                addTextCell(qualificationTable,"Qualification/Diploma",BaseColor.GRAY,Element.ALIGN_TOP,bold_font(10));

                //qualification add data
                JDBCQualificationDao jdbcQualificationDao = new JDBCQualificationDao();
                for (Qualification qualification:jdbcQualificationDao.select(user.getId())){
                    addTextCell(qualificationTable,String.valueOf("\n"+qualification.getYear()),BaseColor.WHITE,Element.ALIGN_CENTER,normal_font(9));
                    PdfPCell diplomaCell = new PdfPCell();
                    diplomaCell.addElement(new Paragraph(qualification.getDiploma(),bold_font(9)));
                    diplomaCell.addElement(new Paragraph(qualification.getUniversity(),normal_font(9)));
                    diplomaCell.setVerticalAlignment(Element.ALIGN_CENTER);
                    diplomaCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    qualificationTable.addCell(diplomaCell);
                }
                //IMPORTANT moving this will cause tables to be placed at the wrong position
                //qualificationTable.setKeepTogether(false);
                secondTableCell.addElement(qualificationTable);
                secondTableCell.addElement(new Paragraph("\n"));

                //Masteries Title
                createTitle(secondTableCell, "MASTERIES");

                //Mastery table init
                PdfPTable masteryTable = new PdfPTable(2);
                masteryTable.setWidths(new int[]{1, 1});

                //masteries table headers
                addTextCell(masteryTable,"Technology",BaseColor.GRAY,Element.ALIGN_CENTER,bold_font(10));

                addTextCell(masteryTable,"Years of Experience",BaseColor.GRAY,Element.ALIGN_CENTER,bold_font(10));

                //masteries add data
                JDBCMasteryDao jdbcMasteryDao = new JDBCMasteryDao();
                for (Mastery mastery : jdbcMasteryDao.select(user.getId())) {
                    addTextCell(masteryTable,mastery.getTechnology(),BaseColor.WHITE,Element.ALIGN_CENTER,normal_font(10));
                    addTextCell(masteryTable,String.valueOf(mastery.getExperience()),BaseColor.WHITE,Element.ALIGN_CENTER,normal_font(9));

                }

                //IMPORTANT moving this will cause tables to be placed at the wrong position
                secondTableCell.addElement(masteryTable);
                secondTableCell.addElement(new Paragraph("\n"));
                //final adding all right side
                mainTable.addCell(secondTableCell);
                document.add(mainTable);

                //moving on to the reports which will use the whole page, so we create a new page:
                document.newPage();

                //Experiences Title
//                Paragraph experiencesTitle = new Paragraph("EXPERIENCES(PROJECTS OF THE LAST 5 YEARS)",bold_font(14));
//                experiencesTitle.setAlignment(Element.ALIGN_CENTER);
//                document.add(experiencesTitle);

                PdfPTable expTitleTable = new PdfPTable(1);
                expTitleTable.setWidthPercentage(100.0f);
                PdfPCell exptitlecell = new PdfPCell();
                exptitlecell.setBorder(Rectangle.NO_BORDER);
                createTitle(exptitlecell,"EXPERIENCES(PROJECTS OF THE LAST 5 YEARS)");
                expTitleTable.addCell(exptitlecell);
                document.add(expTitleTable);
                //init experience table
                PdfPTable experienceTable = new PdfPTable(3);
                experienceTable.setWidths(new int[]{1,1,6});

                //experience table headers
                addTextCell(experienceTable,"Country",BaseColor.GRAY,Element.ALIGN_CENTER,bold_font(10));

                addTextCell(experienceTable,"Year",BaseColor.GRAY,Element.ALIGN_CENTER,bold_font(10));

                addTextCell(experienceTable,"Project Details",BaseColor.GRAY,Element.ALIGN_CENTER,bold_font(10));

                //experiencce data
                JDBCReportDao jdbcReportDao = new JDBCReportDao();
                for (Report report : jdbcReportDao.selectByLastYears(user.getId(),Integer.parseInt(years_textfield.getText()))) {
                    addTextCell(experienceTable,report.getCountry(),BaseColor.WHITE,Element.ALIGN_CENTER,normal_font(10));
                    addTextCell(experienceTable,String.valueOf(report.getYear()),BaseColor.WHITE,Element.ALIGN_CENTER,normal_font(10));
                    addTextCell(experienceTable,String.valueOf(report.getDetails()),BaseColor.WHITE,Element.ALIGN_LEFT,normal_font(10));
                }

                document.add(experienceTable);

                document.close();
                pdfWriter.close();


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (DocumentException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void cancel_btn_pressed(ActionEvent event) {
        closeWindow();
    }

    public void choose_folder_btn_pressed(ActionEvent event) {
        if(choiceBox.getValue().equals("template 1")){
            generateReportForUser(user_selected);
            closeWindow();
        }

    }

    private void closeWindow() {
        Stage stage = (Stage) cancel_btn.getScene().getWindow();
        stage.close();
    }

}
