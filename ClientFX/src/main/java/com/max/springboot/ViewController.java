package com.max.springboot;

import com.max.springboot.domain.Phone;
import com.max.springboot.service.PhoneRESTService;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;


@Component
/** Main controller class responsible for user interaction and service calls. */
public class ViewController implements Initializable {

    private final int SLEEPCOUNT = 2500;
    private static final Logger LOG = LoggerFactory.getLogger( ViewController.class );
    
    @Autowired private PhoneRESTService _PhoneService;
    @FXML private TableView<Phone> _TableView;
    @FXML private TableColumn<Phone, Integer> _IdColumn;
    @FXML private TableColumn<Phone, Boolean> _AvailableColumn;
    @FXML private TableColumn<Phone, String> _NameColumn;
    @FXML private TableColumn<Phone, String> _BookedByColumn;
    @FXML private TableColumn<Phone, Date> _DateBooked;
    @FXML private TextField _NameTextField;
    @FXML private TextField _BookedByTextField;
    @FXML private CheckBox _IsAvailableCheckbox;

    @FXML private Button _BookButton;
    @FXML private Button _ReturnButton;
    @FXML private Label _ResultLabel;
    @FXML private ProgressIndicator _ProgressIndicator;

    private Phone _SelectedItem;
    private final BooleanProperty _ModifiedProperty = new SimpleBooleanProperty( false );
    private final IntegerProperty _BackgroundActive = new SimpleIntegerProperty( 0 );
    private final ObservableList<Phone> _PhoneData = FXCollections.observableArrayList();


    @FXML
    private void bookButtonAction( ActionEvent actionEvent ){

        LOG.info( "Book " + _SelectedItem );
        Phone phone = _TableView.getSelectionModel().getSelectedItem();
        //phone.setName( _NameTextField.getText() );
        phone.setAvailable( false );
        phone.setBookedBy( _BookedByTextField.getText() );
        phone.setDateBooked( new Date() );

        final BookTask task = new BookTask( phone );
        _BackgroundActive.set( _BackgroundActive.get() + 1 );

        task.setOnSucceeded( t -> {

            _PhoneData.setAll( FXCollections.observableList( task.getValue() ) );
            _ResultLabel.setText( "Phone " + phone.getName() + " booked." );

            _BackgroundActive.set( _BackgroundActive.get() - 1 );
            _ModifiedProperty.set( false );

        } );

        task.setOnFailed( t -> {
            _BackgroundActive.set( _BackgroundActive.get() - 1 );
            _ResultLabel.setText( "Failed to book phone " + phone.getName() + ".\n"
                + task.getException().getLocalizedMessage() );
        } );

        new Thread( task ).start();

    }

    @FXML
    private void returnButtonAction( ActionEvent actionEvent ){

        LOG.info( "Return " + _SelectedItem );
        Phone phone = _TableView.getSelectionModel().getSelectedItem();
        phone.setAvailable( true );
        phone.setBookedBy( null );
        phone.setDateBooked( null );

        ReturnTask task = new ReturnTask( phone );
        _BackgroundActive.set( _BackgroundActive.get() + 1 );

        task.setOnSucceeded( t -> {

            _PhoneData.setAll( FXCollections.observableList( task.getValue() ) );
            _ResultLabel.setText( "Phone " + phone.getName() + " returned." );
            _BackgroundActive.set( _BackgroundActive.get() - 1 );

        } );

        task.setOnFailed( t -> {

            _BackgroundActive.set( _BackgroundActive.get() - 1 );
            _ResultLabel.setText( "Failed to return phone " + phone.getName() + ".\n"
                + task.getException().getLocalizedMessage() );

        } );

        new Thread( task ).start();

    }

    @FXML
    private void handleKeyAction( KeyEvent keyEvent ){

        final String text = _BookedByTextField.getText();
        final boolean hasText = null != text && !text.isBlank();
        _ModifiedProperty.set( hasText );

    }

    @Override
    public void initialize( URL location, ResourceBundle resources) {

        bindComponents();
        buildTableView();
        loadData();

    }


    private void bindComponents(){

        _BookButton.disableProperty().bind( _TableView.getSelectionModel().selectedItemProperty().isNull().or(
            _IsAvailableCheckbox.selectedProperty().not() ).or( _ModifiedProperty.not() ) );
        _ReturnButton.disableProperty().bind( _TableView.getSelectionModel().selectedItemProperty().isNull().or(
            _IsAvailableCheckbox.selectedProperty() ) );
        _BookedByTextField.editableProperty().bind( _IsAvailableCheckbox.selectedProperty() );
        _ProgressIndicator.visibleProperty().bind( _BackgroundActive.greaterThan( 0 ) );

        _TableView.setOnKeyPressed( event -> {

            if( KeyCode.F5 == event.getCode() ){

                loadData();
                event.consume();

            }

        } );

    }

    private void buildTableView(){

        _IdColumn.setStyle( "-fx-alignment: CENTER-RIGHT;");
        _IdColumn.setCellValueFactory( new PropertyValueFactory<>( "id" ) );
        _NameColumn.setCellValueFactory( new PropertyValueFactory<>( "name" ) );
        _BookedByColumn.setCellValueFactory( new PropertyValueFactory<>( "bookedBy" ) );

        _AvailableColumn.setCellValueFactory( ( e ) -> new ReadOnlyBooleanWrapper( e.getValue().isAvailable() ).getReadOnlyProperty() );
        _AvailableColumn.setCellFactory( CheckBoxTableCell.forTableColumn( (i) -> new SimpleBooleanProperty( _PhoneData.get( i  ).isAvailable())) );

        _DateBooked.setStyle( "-fx-alignment: CENTER-RIGHT;");
        _DateBooked.setCellValueFactory( new PropertyValueFactory<>( "dateBooked" ) );
        _DateBooked.setCellFactory( column -> { TableCell<Phone, Date> cell = new TableCell<>(){

                private SimpleDateFormat format = new SimpleDateFormat( "dd.MM.yyyy HH:mm:ss" );

                @Override
                protected void updateItem( Date item, boolean empty ){

                    super.updateItem( item, empty );
                    if( empty || null == item ){
                        setText( null );
                    }
                    else{
                        setText( format.format( item ) );
                    }

                }

            };

            return cell;

        } );

        _TableView.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> {

                _SelectedItem = newValue;
                _ModifiedProperty.set( false );

                if( newValue != null ){

                    _NameTextField.setText( _SelectedItem.getName() );
                    _BookedByTextField.setText( _SelectedItem.getBookedBy() );
                    _IsAvailableCheckbox.setSelected( _SelectedItem.isAvailable() );

                }
                else{
                    _NameTextField.setText( "" );
                    _BookedByTextField.setText( "" );
                    _IsAvailableCheckbox.setSelected( false );
                }

            });

    }


    private void loadData(){

        final LoadTask task = new LoadTask();
        _BackgroundActive.set( _BackgroundActive.get() + 1 );
        _TableView.setDisable( true );
        _NameTextField.setDisable( true );
        _BookedByTextField.setDisable( true );

        task.setOnSucceeded( t -> {

            _PhoneData.setAll( FXCollections.observableList( task.getValue() ) );
            _TableView.setItems( _PhoneData );

            _BackgroundActive.set( _BackgroundActive.get() - 1 );
            _TableView.setDisable( false );
            _NameTextField.setDisable( false );
            _BookedByTextField.setDisable( false );

        } );

        task.setOnFailed( t -> {

            _BackgroundActive.set( _BackgroundActive.get() - 1 );
            _TableView.setDisable( false );
            _NameTextField.setDisable( false );
            _BookedByTextField.setDisable( false );

            _ResultLabel.setText( "Failed to load phones " + ".\n" + task.getException().getLocalizedMessage() );
            _TableView.setPlaceholder( new Text( "Failed to load the phones. Check the database \nis available at http://localhost:8080/h2-console/" ) );

        } );

        new Thread( task ).start();

    }


    private class LoadTask extends Task<List<Phone>>{

        @Override
        protected List<Phone> call() throws Exception{

            Thread.sleep( 2 * SLEEPCOUNT );
            return _PhoneService.getPhones();

            //_TableView.getSelectionModel().clearSelection();
            //System.out.println( _PhoneData );

        }

    }


    private class BookTask extends Task<List<Phone>>{

        private final Phone phone;


        private BookTask( Phone phone ){
            this.phone = phone;
        }

        @Override
        protected List<Phone> call() throws Exception{
            Thread.sleep( SLEEPCOUNT );
            _PhoneService.updatePhone( phone );
            return _PhoneService.getPhones();
        }

    }


    private class ReturnTask extends Task<List<Phone>>{

        private final Phone phone;


        private ReturnTask( Phone phone ){
            this.phone = phone;
        }

        @Override
        protected List<Phone> call() throws Exception{
            Thread.sleep( SLEEPCOUNT );
            _PhoneService.updatePhone( phone );
            return _PhoneService.getPhones();
        }

    }

}
