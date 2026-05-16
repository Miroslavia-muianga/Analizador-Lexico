package org.analizador;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.scene.Scene;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    // editor
    @FXML private TextArea codeEditor;
    @FXML private TextArea lineNumbers;

    // tabela de tokens
    @FXML private TableView<Token> tokenTable;
    @FXML private TableColumn<Token, String> colValor;
    @FXML private TableColumn<Token, String> colTipo;
    @FXML private TableColumn<Token, Integer> colLinha;
    @FXML private TableColumn<Token, Integer> colColuna;

    // erros e status
    @FXML private ListView<String> errorList;
    @FXML private Label lblTotal;
    @FXML private Label lblSpecial;
    @FXML private Label lblIdents;
    @FXML private Label lblErros;
    @FXML private Label lblFicheiro;
    @FXML private Button btnTheme;

    private boolean darkTheme = true;
    private String currentFile = null;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // configura as colunas da tabela
        colValor.setCellValueFactory(new PropertyValueFactory<>("valor"));
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colLinha.setCellValueFactory(new PropertyValueFactory<>("linha"));
        colColuna.setCellValueFactory(new PropertyValueFactory<>("coluna"));

        // actualiza numeração de linhas quando o texto muda
        codeEditor.textProperty().addListener((obs, oldVal, newVal) -> {
            updateLineNumbers(newVal);
        });

        // sincroniza o scroll da numeração com o editor
        codeEditor.scrollTopProperty().addListener((obs, oldVal, newVal) -> {
            lineNumbers.setScrollTop(newVal.doubleValue());
        });
    }

    private void updateLineNumbers(String text) {
        int lines = text.isEmpty() ? 1 : text.split("\n", -1).length;
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= lines; i++) {
            sb.append(i).append("\n");
        }
        lineNumbers.setText(sb.toString());
    }

    //metodos dos botoes

    @FXML
    private void analyze() {
        String code = codeEditor.getText();
        if (code.trim().isEmpty()) return;

        Lexer lexer = new Lexer(code);
        List<Token> tokens = lexer.tokenize();
        List<String> erros = lexer.getErros();

        // preenche a tabela de tokens (exclui o EOF)
        tokenTable.getItems().clear();
        for (Token t : tokens) {
            if (t.tipo != TipoToken.EOF) {
                tokenTable.getItems().add(t);
            }
        }

        // preenche a lista de erros
        errorList.getItems().clear();
        errorList.getItems().addAll(erros);

        // actualiza a barra de status
        long specials = tokens.stream().filter(t -> t.tipo == TipoToken.SIMBOLO_ESPECIAL).count();
        long idents   = tokens.stream().filter(t -> t.tipo == TipoToken.IDENTIFICADOR).count();
        long errs     = tokens.stream().filter(t -> t.tipo == TipoToken.LEXEMA_NAO_RECONHECIDO).count();


        lblTotal.setText("Tokens: " + (tokens.size() - 1)); // -1 para excluir EOF
        lblSpecial.setText("Síbolos Especiais: " + specials);
        lblIdents.setText("Identificadores: " + idents);
        lblErros.setText("Erros: " + errs);
    }

    @FXML
    private void openFile() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Abrir ficheiro Pascal");
        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Ficheiros Pascal", "*.pas", "*.pascal"),
                new FileChooser.ExtensionFilter("Ficheiros de texto", "*.txt"),
                new FileChooser.ExtensionFilter("Todos os ficheiros", "*.*")
        );

        File file = chooser.showOpenDialog(codeEditor.getScene().getWindow());
        if (file != null) {
            try {
                String content = Files.readString(file.toPath());
                codeEditor.setText(content);
                currentFile = file.getName();
                lblFicheiro.setText(currentFile);
            } catch (IOException e) {
                errorList.getItems().add("Erro ao abrir ficheiro: " + e.getMessage());
            }
        }
    }

    @FXML
    private void clear() {
        codeEditor.clear();
        tokenTable.getItems().clear();
        errorList.getItems().clear();
        lblTotal.setText("Tokens: 0");
        lblSpecial.setText("Símbolos Especiais: 0");
        lblIdents.setText("Identificadores: 0");
        lblErros.setText("Erros: 0");
        lblFicheiro.setText("Sem ficheiro aberto");
        currentFile = null;
    }

    @FXML
    private void toggleTheme() {
        Scene scene = codeEditor.getScene();
        scene.getStylesheets().clear();

        if (darkTheme) {
            scene.getStylesheets().add(
                    getClass().getResource("/org/analizador/light-theme.css").toExternalForm()
            );
            btnTheme.setText("🌙 Tema");
            darkTheme = false;
        } else {
            scene.getStylesheets().add(
                    getClass().getResource("/org/analizador/dark-theme.css").toExternalForm()
            );
            btnTheme.setText("☀ Tema");
            darkTheme = true;
        }
    }
}