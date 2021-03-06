package GraphProject;

import javafx.beans.property.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.List;
import java.io.File;

public class UIController {
    public static int jobs = 0;
    public static DoubleProperty progress = new SimpleDoubleProperty();
    public static BooleanProperty isDone = new SimpleBooleanProperty(false);
    public static BooleanProperty graphSavePre = new SimpleBooleanProperty(false);
    public static BooleanProperty graphSavePost = new SimpleBooleanProperty(false);
    public static StringProperty graphSaveFolder = new SimpleStringProperty(" ");

    @FXML
    private GridPane active_pane;
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

    @FXML
    private TextField grph_saveTo;
    @FXML
    private CheckBox grph_saveBefore;
    @FXML
    private CheckBox grph_saveAfter;

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

    //erdosh
    @FXML
    private TextField erd_a;
    @FXML
    private TextField erd_n;
    @FXML
    private TextField erd_m;

    //strogatz
    @FXML
    private TextField strog_a;
    @FXML
    private TextField strog_n;
    @FXML
    private TextField strog_k;
    @FXML
    private TextField strog_p;

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
    private void toggleErdosh(){
        erd_a.setDisable(!erd_a.isDisabled());
        erd_n.setDisable(!erd_n.isDisabled());
        erd_m.setDisable(!erd_m.isDisabled());
    }

    @FXML
    private void toggleStrogatz(){
        strog_a.setDisable(!strog_a.isDisabled());
        strog_n.setDisable(!strog_n.isDisabled());
        strog_k.setDisable(!strog_k.isDisabled());
        strog_p.setDisable(!strog_p.isDisabled());
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
        grph_saveBefore.selectedProperty().bindBidirectional(graphSavePre);
        grph_saveAfter.selectedProperty().bindBidirectional(graphSavePost);
        grph_saveTo.textProperty().bindBidirectional(graphSaveFolder);
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
        this.num_inputs = new TextField[]{bar_a, bar_n, bar_m, pet_a, pet_n, pet_k, hyp_a, hyp_dim, erd_a, erd_n, erd_m,
                strog_a, strog_n, strog_p, strog_k, whe_a, whe_sz, threads, ex_rnd_const_inp, ex_rnd_edg_inp,
                ex_rnd_vert_inp};
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
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter =
                new FileChooser.ExtensionFilter("Graph files (*.csv)", "*.csv");
        FileChooser.ExtensionFilter edgesFilter =
                new FileChooser.ExtensionFilter("Edges file (*.edges)", "*.edges");
        fileChooser.getExtensionFilters().add(edgesFilter);
        fileChooser.getExtensionFilters().add(extFilter);
        List<File> list = fileChooser.showOpenMultipleDialog(((Button)e.getSource()).getScene().getWindow());
        if (list != null) {
            for (File file : list) {
                String path = file.getAbsolutePath();
                cus_main.getItems().add(path);
            }
        }
        fileChooser.getExtensionFilters().removeAll();
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
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter csvFilter =
                new FileChooser.ExtensionFilter("Table files ", "*.csv");
        fileChooser.getExtensionFilters().add(csvFilter);

        File save = fileChooser.showSaveDialog(((Button)e.getSource()).getScene().getWindow());
        if(save != null)
            saveAddress.setText(save.getAbsolutePath());
        fileChooser.getExtensionFilters().removeAll();
    }
    @FXML
    private void handleSaveFolder(final ActionEvent e){
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File save = directoryChooser.showDialog(((Button)e.getSource()).getScene().getWindow());
        if(save != null)
            grph_saveTo.setText(save.getAbsolutePath());
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
        if((grph_saveAfter.selectedProperty().get()||grph_saveBefore.selectedProperty().get())
                &&graphSaveFolder.getValue().equals(" ")){
            this.sout.setText("No folder to save.");
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
        HashMap<String,Double> variables = this.getVariables();
        Runnable runnable = () -> {
            Experiments.randomExperiment(variables,cus_main.getItems().toArray(),saveAddress.getCharacters().toString());
        };
        Thread t = new Thread(runnable);
        t.start();
    }

    private HashMap<String,Double> getVariables(){
        HashMap<String,Double> variables = new HashMap();
        variables.put("bar_a",Double.parseDouble(bar_a.getCharacters().toString()));
        variables.put("bar_n",Double.parseDouble(bar_n.getCharacters().toString()));
        variables.put("bar_m",Double.parseDouble(bar_m.getCharacters().toString()));
        variables.put("pet_a",Double.parseDouble(pet_a.getCharacters().toString()));
        variables.put("pet_n",Double.parseDouble(pet_n.getCharacters().toString()));
        variables.put("pet_k",Double.parseDouble(pet_k.getCharacters().toString()));
        variables.put("hyp_a",Double.parseDouble(hyp_a.getCharacters().toString()));
        variables.put("hyp_dim",Double.parseDouble(hyp_dim.getCharacters().toString()));
        variables.put("erd_a",Double.parseDouble(erd_a.getCharacters().toString()));
        variables.put("erd_n",Double.parseDouble(erd_n.getCharacters().toString()));
        variables.put("erd_m",Double.parseDouble(erd_m.getCharacters().toString()));
        variables.put("strog_a",Double.parseDouble(strog_a.getCharacters().toString()));
        variables.put("strog_n",Double.parseDouble(strog_n.getCharacters().toString()));
        variables.put("strog_p",Double.parseDouble(strog_p.getCharacters().toString()));
        variables.put("strog_k",Double.parseDouble(strog_k.getCharacters().toString()));
        variables.put("whe_a",Double.parseDouble(whe_a.getCharacters().toString()));
        variables.put("whe_sz",Double.parseDouble(whe_sz.getCharacters().toString()));
        variables.put("threads",Double.parseDouble(threads.getCharacters().toString()));
        variables.put("ex_rnd_const_inp",Double.parseDouble(ex_rnd_const_inp.getCharacters().toString()));
        variables.put("ex_rnd_edg_inp",Double.parseDouble(ex_rnd_edg_inp.getCharacters().toString()));
        variables.put("ex_rnd_vert_inp",Double.parseDouble(ex_rnd_vert_inp.getCharacters().toString()));
        return variables;
    }

    private boolean validateInputs(){
        for(TextField input : num_inputs){
            try {
                Double.parseDouble(input.getCharacters().toString());
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
        result += Integer.parseInt(erd_a.getCharacters().toString());
        result += Integer.parseInt(strog_a.getCharacters().toString());
        result += Integer.parseInt(whe_a.getCharacters().toString());
        result += cus_main.getItems().size();
        return result;
    }
}
