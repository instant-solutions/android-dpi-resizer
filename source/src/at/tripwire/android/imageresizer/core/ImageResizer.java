package at.tripwire.android.imageresizer.core;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Method;

public class ImageResizer {

	public interface ProgressMonitor {
		void onStart();
		
		void onProgressChanged(int progress);
		
		void onEnd();
	}
	
	private static final String FOLDER_PREFIX = "drawable-";

	private Map<File, Density> filesToConvert;
	private List<Density> targetDensities;
	private File destDir;

	public ImageResizer(File destDir) {
		this.filesToConvert = new HashMap<File, Density>();
		this.targetDensities = new ArrayList<Density>();
		this.destDir = destDir;
	}

	public void add(File file, Density density) {
		filesToConvert.put(file, density);
	}
	
	public void add(Collection<File> files, Density density) {
		for(File file : files) {
			add(file, density);
		}
	}

	public void addTargetDensity(Density density) {
		targetDensities.add(density);
	}

	// TODO: check for write rights + error handling
	public void convert(ProgressMonitor monitor) {
		if(monitor != null) {
			monitor.onStart();
		}
		
		if (!destDir.exists()) {
			destDir.mkdirs();
		}

		for (Density targetDensity : targetDensities) {
			File targetFolder = new File(destDir, FOLDER_PREFIX + targetDensity.getName());
			if (!targetFolder.exists()) {
				targetFolder.mkdirs();
			}
		}

		float count = 0;
		for (File srcFile : filesToConvert.keySet()) {
			Density densityFrom = filesToConvert.get(srcFile);
			try {
				BufferedImage imgFrom = ImageIO.read(srcFile);
				for (Density targetDensity : targetDensities) {
					float heightFrom = imgFrom.getHeight();
					float widthFrom = imgFrom.getWidth();
					float heightTo = heightFrom / densityFrom.getFactorToMdpi() * targetDensity.getFactorToMdpi();
					float widthTo = widthFrom / densityFrom.getFactorToMdpi() * targetDensity.getFactorToMdpi();

					File destFile = new File(destDir, FOLDER_PREFIX + targetDensity.getName() + File.separator + srcFile.getName());
					BufferedImage imgTo = Scalr.resize(imgFrom, Method.ULTRA_QUALITY, (int) widthTo, (int) heightTo);
					ImageIO.write(imgTo, "png", destFile);
					count++;
					if(monitor != null) {
						monitor.onProgressChanged((int) (count / ((float) filesToConvert.size() * targetDensities.size()) * 100));
					}
				}
			} catch (IOException e) {
				// TODO: error handling
				e.printStackTrace();
			}

		}
		
		if(monitor != null) {
			monitor.onEnd();
		}
	}
}
