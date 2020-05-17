import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.awt.Desktop;
import java.util.HashMap;
import java.util.List;
import java.io.File;
import java.io.IOException;

public class UIController {
    public static int jobs = 0;
    public static DoubleProperty progress = new SimpleDoubleProperty();
    public static BooleanProperty isDone = new SimpleBooleanProperty(false);
    final FileChooser fileChooser = new FileChooser();
    @FXML
    private VBox main_toolpane;
    @FXML
    private Label sout;
    @FXML
    private ProgressBar progress_1;
    @FXML
    private ProgressIndicator progress_2;
    @FXML
    private TextField threads;
    @FXML
    private TextField saveAddress;
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

    private TextField[] num_inputs = null;
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
        main_toolpane.disableProperty().bindBidirectional(isDone);
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
        this.num_inputs = new TextField[]{bar_a, bar_n, bar_m, pet_a, pet_n, pet_k, hyp_a, hyp_dim, kle_a, kle_n, kle_p, kle_q, kle_r,
                wind_a, wind_m, wind_n, whe_a, whe_sz, threads, ex_rnd_const_inp, ex_rnd_edg_inp, ex_rnd_vert_inp};
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
                new FileChooser.ExtensionFilter("Table file (*.csv)", "*.csv");
        fileChooser.getExtensionFilters().add(extFilter);
        File save = fileChooser.showSaveDialog(((Button)e.getSource()).getScene().getWindow());
        if(save != null)
            saveAddress.setText(save.getAbsolutePath());
    }

    @FXML
    private void start(){
        jobs = getJobs();
        progress_1.progressProperty().bind(progress);
        progress_2.progressProperty().bind(progress);
        if(!validateInputs()){
            this.sout.setText("Incorrect parameter inputs!");
            return;
        }
        if(saveAddress.getCharacters().length()==0){
            this.sout.setText("No file to save.");
            return;
        }
        isDone.setValue(true);
        switch (((RadioButton)ex_grp.getSelectedToggle()).getText()){
            case "Random Experiment":
                randomScenario();
                break;
            default:
                this.sout.setText("Something went wrong...");
        }
    }

    private void randomScenario(){
        HashMap<String,Integer> variables = this.getVariables();
        Runnable runnable = () -> {
            Experiments.randomExperiment(variables,cus_main.getItems().toArray(),saveAddress.getCharacters().toString());
        };
        Thread t = new Thread(runnable);
        t.start();
    }

    private HashMap<String,Integer> getVariables(){
        HashMap<String,Integer> variables = new HashMap();
        variables.put("bar_a",Integer.parseInt(bar_a.getCharacters().toString()));
        variables.put("bar_n",Integer.parseInt(bar_n.getCharacters().toString()));
        variables.put("bar_m",Integer.parseInt(bar_m.getCharacters().toString()));
        variables.put("pet_a",Integer.parseInt(pet_a.getCharacters().toString()));
        variables.put("pet_n",Integer.parseInt(pet_n.getCharacters().toString()));
        variables.put("pet_k",Integer.parseInt(pet_k.getCharacters().toString()));
        variables.put("hyp_a",Integer.parseInt(hyp_a.getCharacters().toString()));
        variables.put("hyp_dim",Integer.parseInt(hyp_dim.getCharacters().toString()));
        variables.put("kle_a",Integer.parseInt(kle_a.getCharacters().toString()));
        variables.put("kle_n",Integer.parseInt(kle_n.getCharacters().toString()));
        variables.put("kle_p",Integer.parseInt(kle_p.getCharacters().toString()));
        variables.put("kle_q",Integer.parseInt(kle_q.getCharacters().toString()));
        variables.put("kle_r",Integer.parseInt(kle_r.getCharacters().toString()));
        variables.put("wind_a",Integer.parseInt(wind_a.getCharacters().toString()));
        variables.put("wind_m",Integer.parseInt(wind_m.getCharacters().toString()));
        variables.put("wind_n",Integer.parseInt(wind_n.getCharacters().toString()));
        variables.put("whe_a",Integer.parseInt(whe_a.getCharacters().toString()));
        variables.put("whe_sz",Integer.parseInt(whe_sz.getCharacters().toString()));
        variables.put("threads",Integer.parseInt(threads.getCharacters().toString()));
        variables.put("ex_rnd_const_inp",Integer.parseInt(ex_rnd_const_inp.getCharacters().toString()));
        variables.put("ex_rnd_edg_inp",Integer.parseInt(ex_rnd_edg_inp.getCharacters().toString()));
        variables.put("ex_rnd_vert_inp",Integer.parseInt(ex_rnd_vert_inp.getCharacters().toString()));
        return variables;
    }

    private boolean validateInputs(){
        for(TextField input : num_inputs){
            try {
                Integer.parseInt(input.getCharacters().toString());
            }
            catch (Exception e){
                return false;
            }
        }
        return true;
    }

    private int getJobs(){
        int result = 0;
        result += Integer.parseInt(bar_a.getCharacters().toString());
        result += Integer.parseInt(pet_a.getCharacters().toString());
        result += Integer.parseInt(hyp_a.getCharacters().toString());
        result += Integer.parseInt(kle_a.getCharacters().toString());
        result += Integer.parseInt(wind_a.getCharacters().toString());
        result += Integer.parseInt(whe_a.getCharacters().toString());
        result += cus_main.getItems().size();
        return result;
    }
}
