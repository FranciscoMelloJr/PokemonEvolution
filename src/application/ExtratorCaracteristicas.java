package application;

import java.io.File;
import java.io.FileOutputStream;

import org.opencv.core.Mat;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;

public class ExtratorCaracteristicas {

	public static double[] extraiCaracteristicas(File f, boolean exibeImagem) {

		double[] caracteristicas = new double[6];

		double corpoVermelhoMeleon = 0;
		double barrigaBegeClaroMeleon = 0;
		double corpoLaranjaRizard = 0;
		double barrigaBegeRizard = 0;
		double asasVerdesRizard = 0;

		Image img = new Image(f.toURI().toString());
		PixelReader pr = img.getPixelReader();

		Mat imagemOriginal = Imgcodecs.imread(f.getPath());
		Mat imagemProcessada = imagemOriginal.clone();

		int w = (int) img.getWidth();
		int h = (int) img.getHeight();

		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {

				Color cor = pr.getColor(j, i);

				double r = cor.getRed() * 255;
				double g = cor.getGreen() * 255;
				double b = cor.getBlue() * 255;

				if (isCorpoVermelhoMeleon(r, g, b)) {
					corpoVermelhoMeleon++;
					imagemProcessada.put(i, j, new double[] { 0, 255, 128 });
				}
				if (i > (h / 2) && isBarrigaBegeClaroMeleon(r, g, b)) {
					barrigaBegeClaroMeleon++;
					imagemProcessada.put(i, j, new double[] { 0, 255, 128 });
				}
				if (isCorpoLaranjaRizard(r, g, b)) {
					corpoLaranjaRizard++;
					imagemProcessada.put(i, j, new double[] { 0, 255, 255 });
				}
				if (i > (h / 2) && isBarrigaBegeRizard(r, g, b)) {
					barrigaBegeRizard++;
					imagemProcessada.put(i, j, new double[] { 0, 255, 255 });
				}				
				if ((i > (h/6) && (i < (h/1.20)) && isAsasVerdesRizard(r, g, b))) {
					asasVerdesRizard++;
					imagemProcessada.put(i, j, new double[] { 0, 255, 255 });
				}
			}
		}

		corpoVermelhoMeleon = (corpoVermelhoMeleon / (w * h)) * 100;
		barrigaBegeClaroMeleon = (barrigaBegeClaroMeleon / (w * h)) * 100;
		corpoLaranjaRizard = (corpoLaranjaRizard / (w * h)) * 100;
		barrigaBegeRizard = (barrigaBegeRizard / (w * h)) * 100;
		asasVerdesRizard = (asasVerdesRizard / (w * h)) * 100;

		caracteristicas[0] = corpoVermelhoMeleon;
		caracteristicas[1] = barrigaBegeClaroMeleon;
		caracteristicas[2] = corpoLaranjaRizard;
		caracteristicas[3] = barrigaBegeRizard;
		caracteristicas[4] = asasVerdesRizard;
		caracteristicas[5] = f.getName().charAt(4) == 'm' ? 0 : 1;
		if (exibeImagem) {
			HighGui.imshow("Imagem original", imagemOriginal);
			HighGui.imshow("Imagem processada", imagemProcessada);

			HighGui.waitKey(0);
		}

		return caracteristicas;
	}

	
	public static boolean isCorpoVermelhoMeleon(double r, double g, double b) {
		
		if ((r >= 135 && g <= 120 && b <= 105 && r+g+b <= 700) && 
				(( g <= 50 || (r >= 170 && g - 22 <= b )) ||
						( g <= 62 && r >= 200 )))
				 {
			return true;
		}
		return false;
	}

	public static boolean isBarrigaBegeClaroMeleon(double r, double g, double b) {
		
		double maxRtoG = 60;
		double minRtoG = 0;
		double maxGtoB = 47;
		double minGtoB = 2;
		
		double auxG = r - maxRtoG;
		double auxB = g - maxGtoB;
		double difG = auxG + maxRtoG - minRtoG;
		double difB = auxB + maxGtoB + minGtoB;
		
			if ((b >= auxB && b <= difB && g >= auxG && g <= difG && r >= 195 && r <= 255) && (r+g+b <700)){
			return true;
		}
		return false;
	}

	public static boolean isCorpoLaranjaRizard(double r, double g, double b) {
		
		if (r >= 125 && g >= 75 && g <= 180 &&  b <= 160 && r+g+b <= 700 && g - 28 >= b && r - 50 > g)
				 {
			return true;
		}
		return false;
	}

	public static boolean isBarrigaBegeRizard(double r, double g, double b) {

		double maxRtoG = 52;
		double minRtoG = 14;
		double maxGtoB = 115;
		double minGtoB = 53;
		
		double auxG = r - maxRtoG;
		double auxB = g - maxGtoB;
		double difG = auxG + maxRtoG + minRtoG;
		double difB = auxB + maxGtoB - minGtoB;

		if ((b >= auxB && b <= difB && g >= auxG && g <= difG && r >= 201 && r <= 255)){
			return true;
		}
		return false;
	}
	
	public static boolean isAsasVerdesRizard(double r, double g, double b) {
		if ((b <= 175 || (b <= 186 && b <= g + 40) || (b<= 240 && g>= 160 && b >= r + 110)) && g >= 39 && g <= 225 && r <= 96 && g >= r+15 && b >= r+5) {
			return true;
		}
		return false;
	}

	public static void extrair(boolean exibeImagem) {

		String exportacao = "@relation caracteristicas\n\n";
		exportacao += "@attribute corpo_vermelho_charmeleon real\n";
		exportacao += "@attribute barriga_bege_claro_charmeleon real\n";
		exportacao += "@attribute corpo_laranja_charizard real\n";
		exportacao += "@attribute barriga_bege_charizard real\n";
		exportacao += "@attribute asas_verdes_charizard real\n";
		exportacao += "@attribute classe {Charmeleon, Charizard}\n\n";
		exportacao += "@data\n";

		File diretorio = new File("src\\imagens");
		File[] arquivos = diretorio.listFiles();

		double[][] caracteristicas = new double[405][6];

		int cont = -1;
		for (File imagem : arquivos) {
			cont++;
			caracteristicas[cont] = extraiCaracteristicas(imagem, exibeImagem);

			String classe = caracteristicas[cont][5] == 0 ? "Charmeleon" : "Charizard";

			System.out.println("Corpo Vermelho Charmeleon: " + caracteristicas[cont][0] + " - Barriga Bege Claro Charmeleon: "
					+ caracteristicas[cont][1] + " - Corpo Laranja Charizard: " + caracteristicas[cont][2] + " - Barriga Bege Charizard: "
					+ caracteristicas[cont][3] + " - Asas Verdes Charizard: " + caracteristicas[cont][4] + " - Classe: "
					+ classe);

			exportacao += caracteristicas[cont][0] + "," + caracteristicas[cont][1] + "," + caracteristicas[cont][2]
					+ "," + caracteristicas[cont][3] + "," + caracteristicas[cont][4] +  "," + classe + "\n";
		}

		try {
			File arquivo = new File("caracteristicas.arff");
			FileOutputStream f = new FileOutputStream(arquivo);
			f.write(exportacao.getBytes());
			f.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}