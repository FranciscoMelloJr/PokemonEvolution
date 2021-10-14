package application;

import java.io.File;
import java.text.DecimalFormat;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

public class PrincipalController {
	
	@FXML private ImageView imageView;
	@FXML private Label meleon1;
	@FXML private Label meleon2;
	@FXML private Label rizard1;
	@FXML private Label rizard2;
	@FXML private Label rizard3;
	@FXML private Label naiveBayesMeleon;
	@FXML private Label naiveBayesRizard;

	private DecimalFormat df = new DecimalFormat("##0.0000");
	private double[] caracteristicasImgSel = {0,0,0,0,0};

	@FXML
		public void classificar() {
			double[] nb = AprendizagemBayesiana.naiveBayes(caracteristicasImgSel);
			naiveBayesMeleon.setText("Charmeleon: "+df.format(nb[0])+"%");
			naiveBayesRizard.setText("Charizard: "+df.format(nb[1])+"%");
		}
	
	@FXML
	public void extrair() {
		ExtratorCaracteristicas.extrair(false);
}
	
	@FXML
		public void selecionaImagem() {
			File f = buscaImg();
			if(f != null) {
				Image img = new Image(f.toURI().toString());
				imageView.setImage(img);
				imageView.setFitWidth(img.getWidth());
				imageView.setFitHeight(img.getHeight());
				caracteristicasImgSel = ExtratorCaracteristicas.extraiCaracteristicas(f, false);
				meleon1.setText("Corpo Vermelho: "+df.format(caracteristicasImgSel[0]));
				meleon2.setText("Barriga Bege Claro: "   +df.format(caracteristicasImgSel[1]));
				rizard1.setText("Corpo Laranja: "    +df.format(caracteristicasImgSel[2]));
				rizard2.setText("Barriga Bege: "   +df.format(caracteristicasImgSel[3]));
				rizard3.setText("Asas Verdes: "  +df.format(caracteristicasImgSel[4]));
			}
		}
	
		private File buscaImg() {
			FileChooser fileChooser = new FileChooser();
			fileChooser.getExtensionFilters().add(new 
					   FileChooser.ExtensionFilter(
							   "Imagens", "*.jpg", "*.JPG", 
							   "*.png", "*.PNG", "*.gif", "*.GIF", 
							   "*.bmp", "*.BMP")); 	
			 fileChooser.setInitialDirectory(new File("src/imagens"));
			 File imgSelec = fileChooser.showOpenDialog(null);
			 try {
				 if (imgSelec != null) {
				    return imgSelec;
				 }
			 } catch (Exception e) {
				e.printStackTrace();
			 }
			 return null;
		}
}
