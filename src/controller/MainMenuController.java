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
import model.dao.jdbc.JDBCCertificationDao;
import model.dao.jdbc.JDBCQualificationDao;
import model.dao.jdbc.JDBCUserDao;
import model.vo.Certification;
import model.vo.Qualification;
import model.vo.User;

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

    public void generate_cv_button_pressed(ActionEvent event) {
        User user = (User) table_object.getSelectionModel().getSelectedItem();
        if (user!=null){
            Document document = new Document();
            try{
                Font bold_size10 = new Font(Font.FontFamily.HELVETICA,10,Font.BOLD);
                Font bold_size9 = new Font(Font.FontFamily.HELVETICA,9,Font.BOLD);
                Font bold_size14 = new Font(Font.FontFamily.HELVETICA,14,Font.BOLD);
                Font bold_size8 = new Font(Font.FontFamily.HELVETICA,8,Font.BOLD);
                Font underline_size8 = new Font(Font.FontFamily.HELVETICA,8,Font.UNDERLINE);
                Font underline_size9 = new Font(Font.FontFamily.HELVETICA,9,Font.UNDERLINE);
                Font normal_size10 =new Font(Font.FontFamily.HELVETICA,10,Font.NORMAL);
                Font normal_size9 =new Font(Font.FontFamily.HELVETICA,9,Font.NORMAL);

                PdfWriter pdfWriter = PdfWriter.getInstance(document,new FileOutputStream(user.getName()+" "+
                        user.getSurname()+" CV "+".pdf"));
                document.open();
                PdfPTable mainTable = new PdfPTable(2);
                mainTable.setWidthPercentage(100.0f);
                mainTable.setWidths(new int[]{2,5});
                mainTable.setKeepTogether(false);
                // left side first table
                PdfPCell firstTableCell = new PdfPCell();
                firstTableCell.setBorder(PdfPCell.NO_BORDER);
                PdfPTable firstTable = new PdfPTable(1);

                //......... add some cells here ..........

                //add Logo
                Image image = Image.getInstance("src/resources/logo.png");
                firstTableCell.addElement(image);

                userDetails(user, bold_size10, underline_size8, mainTable, firstTableCell, firstTable);

                // Second table
                PdfPCell secondTableCell = new PdfPCell();
                secondTableCell.setBorder(PdfPCell.NO_BORDER);
                //PdfPTable secondTable = new PdfPTable(1);
                //......... add some cells here ...........
                //some kind of margin from top, basivally 3 newlines
                //secondTableCell.addElement(new Paragraph("\n \n \n"));

                //certifiation title
                Paragraph certTitle = new Paragraph("CERTIFICATION",bold_size14);
                certTitle.setAlignment(Element.ALIGN_CENTER);
                secondTableCell.addElement(certTitle);

                //certification table init
                PdfPTable certificationTable = new PdfPTable(2);
                certificationTable.setWidths(new int[]{1, 8});

                //certification table headers
                PdfPCell certyearCell = new PdfPCell(new Paragraph("Year",bold_size10));
                certyearCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                certificationTable.addCell(certyearCell);

                PdfPCell qualificationCell = new PdfPCell(new Paragraph("Qualification/Diploma",bold_size10));
                qualificationCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                certificationTable.addCell(qualificationCell);

                //table content
                JDBCCertificationDao jdbcCertificationDao = new JDBCCertificationDao();
                for (Certification certification:jdbcCertificationDao.select(user.getId())) {
                    certificationTable.addCell(new Paragraph(String.valueOf(certification.getYear()),normal_size9));
                    certificationTable.addCell(new Paragraph(certification.getQualification(),normal_size9));
                }

                //IMPORTANT add the table as an element to the 2nd table cell of the main celll
                secondTableCell.addElement(certificationTable);
                secondTableCell.addElement(new Paragraph("\n"));

                //qualification title
                Paragraph qualTitle = new Paragraph("QUALIFICATION",bold_size14);
                qualTitle.setAlignment(Element.ALIGN_CENTER);
                secondTableCell.addElement(qualTitle);

                //qualification table init
                PdfPTable qualificationTable = new PdfPTable(2);
                qualificationTable.setWidths(new int[]{1, 8});

                //qualification table headers
                PdfPCell qualYearCell = new PdfPCell(new Paragraph("Year",bold_size10));
                qualYearCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                qualificationTable.addCell(qualYearCell);

                PdfPCell qualdiplomaCell = new PdfPCell(new Paragraph("Qualification/Diploma",bold_size10));
                qualdiplomaCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                qualificationTable.addCell(qualdiplomaCell);

                //qualification add data
                JDBCQualificationDao jdbcQualificationDao = new JDBCQualificationDao();
                for (Qualification qualification:jdbcQualificationDao.select(user.getId())){
                    PdfPCell yearCell = new PdfPCell();
                    yearCell.addElement(new Paragraph(String.valueOf(qualification.getYear()),normal_size9));
                    yearCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    qualificationTable.addCell(yearCell);
                    PdfPCell diplomaCell = new PdfPCell();
                    diplomaCell.addElement(new Paragraph(qualification.getDiploma(),bold_size9));
                    diplomaCell.addElement(new Paragraph(qualification.getUniversity(),normal_size9));
                    diplomaCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    diplomaCell.setVerticalAlignment(Element.ALIGN_CENTER);
                    qualificationTable.addCell(diplomaCell);
                }
                //IMPORTANT moving this will cause tables to be placed at the wrong position
                qualificationTable.setKeepTogether(false);
                secondTableCell.addElement(qualificationTable);
                secondTableCell.addElement(qualificationTable);
                secondTableCell.addElement(qualificationTable);
                secondTableCell.addElement(qualificationTable);
                secondTableCell.addElement(qualificationTable);
                secondTableCell.addElement(qualificationTable);
                secondTableCell.addElement(qualificationTable);
                secondTableCell.addElement(qualificationTable);
                secondTableCell.addElement(qualificationTable);

                mainTable.addCell(secondTableCell);
                document.add(mainTable);

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

    private void userDetails(User user, Font bold_size10, Font underline_size9, PdfPTable mainTable, PdfPCell firstTableCell, PdfPTable firstTable) {
        Paragraph name = new Paragraph(user.getName(),bold_size10);
        name.setAlignment(Element.ALIGN_CENTER);
        firstTableCell.addElement(name);
        firstTableCell.addElement(new Paragraph("\n"));

        Paragraph surname = new Paragraph(user.getSurname(),bold_size10);
        surname.setAlignment(Element.ALIGN_CENTER);
        firstTableCell.addElement(surname);
        firstTableCell.addElement(new Paragraph("\n"));

        Paragraph position = new Paragraph(user.getPosition(),bold_size10);
        position.setAlignment(Element.ALIGN_CENTER);
        firstTableCell.addElement(position);
        firstTableCell.addElement(new Paragraph("\n"));

        Paragraph email = new Paragraph(user.getEmail(),underline_size9);
        email.setAlignment(Element.ALIGN_CENTER);
        firstTableCell.addElement(email);
        firstTableCell.addElement(new Paragraph("\n"));

        Paragraph site = new Paragraph("www.aequasys.com",underline_size9);
        site.setAlignment(Element.ALIGN_CENTER);
        firstTableCell.addElement(site);

        firstTableCell.addElement(firstTable);
        mainTable.addCell(firstTableCell);
    }

    public static PdfPCell createImageCell(String path) throws DocumentException, IOException {
        Image img = Image.getInstance(path);
        PdfPCell cell = new PdfPCell(img, true);
        return cell;
    }
}