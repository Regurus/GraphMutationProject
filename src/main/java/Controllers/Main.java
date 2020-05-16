package Controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.awt.Desktop;
import java.util.List;
import java.io.File;
import java.io.IOException;

public class Main {
    public static String saveAddress = "";
    final FileChooser fileChooser = new FileChooser();

    //<editor-fold desc="Parts Fields">
    //barabasi
    @FXML
    private TextField bar_a;
    @FXML
    private TextField bar_n;
    @FXML
    private TextField bar_m;

    // petersen
    @FXML
    private TextField pet_a;
    @FXML
    private TextField pet_n;
    @FXML
    private TextField pet_k;

    //hypercube
    @FXML
    private TextField hyp_a;
    @FXML
    private TextField hyp_dim;

    //kleinberg
    @FXML
    private TextField kle_a;
    @FXML
    private TextField kle_n;
    @FXML
    private TextField kle_p;
    @FXML
    private TextField kle_q;
    @FXML
    private TextField kle_r;

    //windmill
    @FXML
    private TextField wind_a;
    @FXML
    private TextField wind_m;
    @FXML
    private TextField wind_n;

    //wheel
    @FXML
    private TextField whe_a;
    @FXML
    private TextField whe_sz;

    //custom
    @FXML
    private Button cus_rm;
    @FXML
    private Button cus_add;
    @FXML
    private ListView cus_main;
    //</editor-fold>

    final private ToggleGroup ex_rnd_grp = new ToggleGroup();
    final private ToggleGroup ex_grp = new ToggleGroup();
    @FXML
    private RadioButton ex_rnd;
    @FXML
    private RadioButton ex_rnd_const;
    @FXML
    private TextField ex_rnd_const_inp;
    @FXML
    private RadioButton ex_rnd_edg;
    @FXML
    private TextField ex_rnd_edg_inp;
    @FXML
    private RadioButton ex_rnd_vert;
    @FXML
    private TextField ex_rnd_vert_inp;

    //<editor-fold desc="Part Fields Toggles">
    @FXML
    private void toggleBarabasi(){
        bar_a.setDisable(!bar_a.isDisabled());
        bar_m.setDisable(!bar_m.isDisabled());
        bar_n.setDisable(!bar_n.isDisabled());
    }

    @FXML
    private void togglePetersen(){
        pet_a.setDisable(!pet_a.isDisabled());
        pet_n.setDisable(!pet_n.isDisabled());
        pet_k.setDisable(!pet_k.isDisabled());
    }

    @FXML
    private void toggleHyper(){
        hyp_a.setDisable(!hyp_a.isDisabled());
        hyp_dim.setDisable(!hyp_dim.isDisabled());
    }

    @FXML
    private void toggleKleinberg(){
        kle_a.setDisable(!kle_a.isDisabled());
        kle_n.setDisable(!kle_n.isDisabled());
        kle_p.setDisable(!kle_p.isDisabled());
        kle_q.setDisable(!kle_q.isDisabled());
        kle_r.setDisable(!kle_r.isDisabled());
    }

    @FXML
    private void toggleWindmill(){
        wind_a.setDisable(!wind_a.isDisabled());
        wind_m.setDisable(!wind_m.isDisabled());
        wind_n.setDisable(!wind_n.isDisabled());
    }

    @FXML
    private void toggleWheel(){
        whe_a.setDisable(!whe_a.isDisabled());
        whe_sz.setDisable(!whe_sz.isDisabled());
    }

    @FXML
    private void toggleCustom(){
        cus_main.setDisable(!cus_main.isDisabled());
        cus_add.setDisable(!cus_add.isDisabled());
        cus_rm.setDisable(!cus_rm.isDisabled());
    }

    @FXML
    private void closeAction(){
        Stage stage = (Stage) whe_a.getScene().getWindow();
        stage.close();
    }
    //</editor-fold>

    @FXML
    public void initialize(){
        //type selector init
        ex_rnd.setToggleGroup(ex_grp);
        ex_rnd.setSelected(true);
        // random experiment group init
        ex_rnd_const.setToggleGroup(ex_rnd_grp);
        ex_rnd_const_inp.setDisable(true);
        ex_rnd_edg.setToggleGroup(ex_rnd_grp);
        ex_rnd_edg.setSelected(true);
        ex_rnd_vert.setToggleGroup(ex_rnd_grp);
        ex_rnd_vert_inp.setDisable(true);
        //setting open list to multiple choise
        cus_main.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    @FXML
    private void resetInputsExRnd(ActionEvent e){
        ex_rnd_const_inp.setDisable(true);
        ex_rnd_edg_inp.setDisable(true);
        ex_rnd_vert_inp.setDisable(true);
        switch (((RadioButton)e.getSource()).getText()){
            case "Constant amount":
                ex_rnd_const_inp.setDisable(false);
                break;
            case "Percentage of edges":
                ex_rnd_edg_inp.setDisable(false);
                break;
            case "Percentage of vertices":
                ex_rnd_vert_inp.setDisable(false);
                break;
        }
    }
    @FXML
    public void handleAdd(final ActionEvent e) {
        FileChooser.ExtensionFilter extFilter =
                new FileChooser.ExtensionFilter("Graph files (*.csv)", "*.csv");
        fileChooser.getExtensionFilters().add(extFilter);
        List<File> list = fileChooser.showOpenMultipleDialog(((Button)e.getSource()).getScene().getWindow());
        if (list != null) {
            for (File file : list) {
                String path = file.getAbsolutePath();
                cus_main.getItems().add(path);
            }
        }
    }
    @FXML
    private void handleRemove(final ActionEvent e){
        Object[] selected = cus_main.getSelectionModel().getSelectedItems().toArray();
        if(selected.length==0)
            return;
        for(Object item: selected){
            cus_main.getItems().remove(item);
        }
    }
    @FXML
    private void handleSave(final ActionEvent e){
        FileChooser.ExtensionFilter extFilter =
                new FileChooser.ExtensionFilter("Table files (*.csv)", "*.csv");
        fileChooser.getExtensionFilters().add(extFilter);
        File save = fileChooser.showSaveDialog(((Button)e.getSource()).getScene().getWindow());
        if(save != null)
            saveAddress = save.getAbsolutePath();
    }
}
