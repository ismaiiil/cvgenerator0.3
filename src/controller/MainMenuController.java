package controller;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.dao.jdbc.*;
import model.vo.*;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainMenuController implements Initializable {

    @FXML
    public Button button_refresh;
    public TableColumn col_id;
    public TableColumn col_name;
    public TableColumn col_surname;
    public TableColumn col_email;
    public TableColumn col_position;
    public Button add_btn;
    public Button edit_btn;
    public Button all_details_btn;
    public Button generate_cv_button;

    @FXML
    private Button delete_btn;

    @FXML
    private TableView table_object;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        col_id.setCellValueFactory(
                new PropertyValueFactory<User,Integer>("id")
        );
        col_name.setCellValueFactory(
                new PropertyValueFactory<User,String>("name")
        );
        col_surname.setCellValueFactory(
                new PropertyValueFactory<User,String>("surname")
        );
        col_email.setCellValueFactory(
                new PropertyValueFactory<User,String>("email")
        );
        col_position.setCellValueFactory(
                new PropertyValueFactory<User,String>("position")
        );
        Column.setCellWrappable(col_email);
        Column.setCellWrappable(col_surname);
        refreshTable();


    }


    public void refreshTable() {
        JDBCUserDao jdbcUserDao = new JDBCUserDao();
        table_object.setItems(jdbcUserDao.select());
    }


    public void button_refresh_pressed(ActionEvent event) {
        refreshTable();

    }

    public void delete_btn_pressed(ActionEvent event) {
        User user = (User) table_object.getSelectionModel().getSelectedItem();
        JDBCUserDao jdbcUserDao = new JDBCUserDao();
        if(user != null){
            jdbcUserDao.delete(user.getId());
        }
        refreshTable();
    }

    public void add_btn_pressed(ActionEvent event) {
        FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("../view/UserBasicDetails.fxml"));
        Parent root1 = null;
        try {
            root1 = (Parent) fxmlloader.load();
            UserBasicDetailsController userBasicDetailsController = fxmlloader.getController();
            Stage stage = new Stage();
            stage.setTitle("Add a User");
            stage.setScene(new Scene(root1));
            stage.setResizable(false);
            stage.showAndWait();
            if(userBasicDetailsController.button_pressed){
                refreshTable();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }



    }

    public void edit_btn_pressed(ActionEvent event) {
        User user = (User) table_object.getSelectionModel().getSelectedItem();
        if (user != null) {
            FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("../view/UserBasicDetails.fxml"));
            try {
                Parent root2 = (Parent) fxmlloader.load();
                UserBasicDetailsController userBasicDetailsController = fxmlloader.getController();
                userBasicDetailsController.init_user_details(user);
                userBasicDetailsController.initdata();
                Stage stage = new Stage();
                stage.setTitle("Edit User:" + user.getName() + user.getSurname());
                stage.setScene(new Scene(root2));
                //stage.setResizable(false);
                stage.showAndWait();
                if(userBasicDetailsController.button_pressed){
                    refreshTable();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }


        }

    }

    public void all_details_btn_pressed(ActionEvent event) {
        User user = (User) table_object.getSelectionModel().getSelectedItem();
        if (user != null) {
            FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("../view/UserRecordsDetails.fxml"));
            try {
                Parent root2 = (Parent) fxmlloader.load();
                UserRecordsDetailsController userRecordsDetailsController = fxmlloader.getController();
                userRecordsDetailsController.initUserId(user);
                Stage stage = new Stage();
                stage.setTitle("Edit User:" + user.getName() + user.getSurname());
                stage.setScene(new Scene(root2));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }

    }

    private Font bold_font(int size){
        return  new Font(Font.FontFamily.HELVETICA,size,Font.BOLD);
    }
    private Font underline_font(int size){
        return new Font(Font.FontFamily.HELVETICA,size,Font.UNDERLINE);
    }
    private Font normal_font(int size){
        return  new Font(Font.FontFamily.HELVETICA,size,Font.NORMAL);
    }

    public void generate_cv_button_pressed(ActionEvent event) {
        User user = (User) table_object.getSelectionModel().getSelectedItem();
        if (user!=null){
            float left = 10;
            float right = 10;
            float top = 60;
            float bottom = 60;
            Document document = new Document(PageSize.A4,left,right,top,bottom);
            try{

                PdfWriter pdfWriter = PdfWriter.getInstance(document,new FileOutputStream(user.getName()+" "+
                        user.getSurname()+" CV "+".pdf"));
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
                Image image = Image.getInstance("src/resources/logo.png");
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
                addHeader(certificationTable);

                PdfPCell qualificationCell = new PdfPCell(new Paragraph("Qualification/Diploma",bold_font(10)));
                qualificationCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                certificationTable.addCell(qualificationCell);

                //table content
                JDBCCertificationDao jdbcCertificationDao = new JDBCCertificationDao();
                for (Certification certification:jdbcCertificationDao.select(user.getId())) {
                    certificationTable.addCell(new Paragraph(String.valueOf(certification.getYear()),normal_font(9)));
                    certificationTable.addCell(new Paragraph(certification.getQualification(),normal_font(9)));
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
                PdfPCell qualYearCell = new PdfPCell(new Paragraph("Year",bold_font(10)));
                qualYearCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                qualificationTable.addCell(qualYearCell);

                PdfPCell qualdiplomaCell = new PdfPCell(new Paragraph("Qualification/Diploma",bold_font(10)));
                qualdiplomaCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                qualificationTable.addCell(qualdiplomaCell);

                //qualification add data
                JDBCQualificationDao jdbcQualificationDao = new JDBCQualificationDao();
                for (Qualification qualification:jdbcQualificationDao.select(user.getId())){
                    PdfPCell yearCell = new PdfPCell();
                    yearCell.addElement(new Paragraph(String.valueOf(qualification.getYear()),normal_font(9)));
                    yearCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    qualificationTable.addCell(yearCell);
                    PdfPCell diplomaCell = new PdfPCell();
                    diplomaCell.addElement(new Paragraph(qualification.getDiploma(),bold_font(9)));
                    diplomaCell.addElement(new Paragraph(qualification.getUniversity(),normal_font(9)));
                    diplomaCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    diplomaCell.setVerticalAlignment(Element.ALIGN_CENTER);
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
                PdfPCell masteryTechCell = new PdfPCell(new Paragraph("Technologies",bold_font(10)));
                masteryTechCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                masteryTechCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                masteryTable.addCell(masteryTechCell);

                PdfPCell masteryYearsCell = new PdfPCell(new Paragraph("Years of experience",bold_font(10)));
                masteryYearsCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                masteryYearsCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                masteryTable.addCell(masteryYearsCell);

                //masteries add data
                JDBCMasteryDao jdbcMasteryDao = new JDBCMasteryDao();
                for (Mastery mastery : jdbcMasteryDao.select(user.getId())) {
                    PdfPCell techcell = new PdfPCell(new Paragraph(mastery.getTechnology(),normal_font(10)));
                    techcell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    techcell.setVerticalAlignment(Element.ALIGN_CENTER);
                    masteryTable.addCell(techcell);

                    PdfPCell yearCell = new PdfPCell(new Paragraph(String.valueOf(mastery.getExperience()),normal_font(9)));
                    yearCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    yearCell.setVerticalAlignment(Element.ALIGN_CENTER);
                    masteryTable.addCell(yearCell);
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
                Paragraph experiencesTitle = new Paragraph("EXPERIENCES(PROJECTS OF THE LAST 5 YEARS)",bold_font(14));
                experiencesTitle.setAlignment(Element.ALIGN_CENTER);
                document.add(experiencesTitle);
                document.add(new Paragraph("\n \n"));
                //init experience table
                PdfPTable experienceTable = new PdfPTable(3);
                experienceTable.setWidths(new int[]{1,1,6});

                //experience table headers
                PdfPCell countryCell = new PdfPCell(new Paragraph("Country",bold_font(10)));
                countryCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                countryCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                experienceTable.addCell(countryCell);

                PdfPCell ExpYearCell = new PdfPCell(new Paragraph("Year",bold_font(10)));
                ExpYearCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                ExpYearCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                experienceTable.addCell(ExpYearCell);

                PdfPCell ProjectDetailsCell = new PdfPCell(new Paragraph("Project Details",bold_font(10)));
                ProjectDetailsCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                ProjectDetailsCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                experienceTable.addCell(ProjectDetailsCell);

                //experiencce data
                JDBCReportDao jdbcReportDao = new JDBCReportDao();
                for (Report report : jdbcReportDao.select(user.getId())) {
                    PdfPCell reportCountryCell = new PdfPCell(new Paragraph(report.getCountry(),normal_font(10)));
                    reportCountryCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    experienceTable.addCell(reportCountryCell);

                    PdfPCell reportYearCell = new PdfPCell(new Paragraph(String.valueOf(report.getYear()),normal_font(9)));
                    reportYearCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    experienceTable.addCell(reportYearCell);

                    PdfPCell reportDetailsCell = new PdfPCell(new Paragraph(report.getDetails(),normal_font(10)));
                    experienceTable.addCell(reportDetailsCell);
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

    private void addHeader(PdfPTable certificationTable) {
        PdfPCell certyearCell = new PdfPCell(new Paragraph("Year",bold_font(10)));
        certyearCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        certificationTable.addCell(certyearCell);
    }

    private void createTitle(PdfPCell secondTableCell, String certification2) {
        Paragraph certTitle = new Paragraph(certification2, bold_font(14));
        certTitle.setAlignment(Element.ALIGN_CENTER);
        secondTableCell.addElement(certTitle);
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

    public static PdfPCell createImageCell(String path) throws DocumentException, IOException {
        Image img = Image.getInstance(path);
        PdfPCell cell = new PdfPCell(img, true);
        return cell;
    }
}