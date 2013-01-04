package at.tripwire.android.imageresizer;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

import net.miginfocom.swing.MigLayout;

import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXLabel;
import org.jdesktop.swingx.JXPanel;

import at.tripwire.android.imageresizer.core.Density;
import at.tripwire.android.imageresizer.core.ImageResizer;
import at.tripwire.android.imageresizer.core.ImageResizer.ProgressMonitor;

@SuppressWarnings("serial")
public class MainFrame extends JXFrame {

	public MainFrame() {
		super("Android Image Resizer");

		JXPanel contentPanel = new JXPanel(new MigLayout("fill", "[]25[]25[]25[]25[]"));

		final DropFilePanel ldpiPanel = new DropFilePanel("LDPI");
		final DropFilePanel mdpiPanel = new DropFilePanel("MDPI");
		final DropFilePanel hdpiPanel = new DropFilePanel("HDPI");
		final DropFilePanel xhdpiPanel = new DropFilePanel("XHDPI");
		final DropFilePanel xxhdpiPanel = new DropFilePanel("XXHDPI");

		contentPanel.add(ldpiPanel, "grow");
		contentPanel.add(mdpiPanel, "grow");
		contentPanel.add(hdpiPanel, "grow");
		contentPanel.add(xhdpiPanel, "grow");
		contentPanel.add(xxhdpiPanel, "grow, wrap");

		JXLabel label = new JXLabel("Create following formats");
		label.setForeground(Color.DARK_GRAY);
		contentPanel.add(label, "gaptop 20, spanx, wrap");

		final JCheckBox ldpiCheck = new JCheckBox("LDPI");
		ldpiCheck.setForeground(Color.DARK_GRAY);
		ldpiCheck.setSelected(true);

		final JCheckBox mdpiCheck = new JCheckBox("MDPI");
		mdpiCheck.setForeground(Color.DARK_GRAY);
		mdpiCheck.setSelected(true);

		final JCheckBox hdpiCheck = new JCheckBox("HDPI");
		hdpiCheck.setForeground(Color.DARK_GRAY);
		hdpiCheck.setSelected(true);

		final JCheckBox xhdpiCheck = new JCheckBox("XHDPI");
		xhdpiCheck.setForeground(Color.DARK_GRAY);
		xhdpiCheck.setSelected(true);

		final JCheckBox xxhdpiCheck = new JCheckBox("XXHDPI");
		xxhdpiCheck.setForeground(Color.DARK_GRAY);
		xxhdpiCheck.setSelected(true);

		contentPanel.add(ldpiCheck, "spanx, split 7");
		contentPanel.add(mdpiCheck, "");
		contentPanel.add(hdpiCheck, "");
		contentPanel.add(xhdpiCheck, "");
		contentPanel.add(xxhdpiCheck, "");
		contentPanel.add(new JXLabel(), "grow");

		JXButton create = new JXButton("Create");
		create.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				if (JFileChooser.APPROVE_OPTION == fileChooser.showSaveDialog(MainFrame.this)) {
					File selectedDir = fileChooser.getSelectedFile();
					if (selectedDir != null) {
						final ImageResizer resizer = new ImageResizer(selectedDir);

						if (ldpiCheck.isSelected()) {
							resizer.addTargetDensity(Density.LDPI);
						}
						if (mdpiCheck.isSelected()) {
							resizer.addTargetDensity(Density.MDPI);
						}
						if (hdpiCheck.isSelected()) {
							resizer.addTargetDensity(Density.HDPI);
						}
						if (xhdpiCheck.isSelected()) {
							resizer.addTargetDensity(Density.XHDPI);
						}
						if (xxhdpiCheck.isSelected()) {
							resizer.addTargetDensity(Density.XXHDPI);
						}

						resizer.add(ldpiPanel.getElements(), Density.LDPI);
						resizer.add(mdpiPanel.getElements(), Density.MDPI);
						resizer.add(hdpiPanel.getElements(), Density.HDPI);
						resizer.add(xhdpiPanel.getElements(), Density.XHDPI);
						resizer.add(xxhdpiPanel.getElements(), Density.XXHDPI);

						Thread thread = new Thread("ResizerThread") {
							public void run() {
								resizer.convert(new ProgressMonitor() {
									
									@Override
									public void onStart() {
										System.out.println("onStart");
									}
									
									@Override
									public void onProgressChanged(int progress) {
										System.out.println(progress);
									}
									
									@Override
									public void onEnd() {
										System.out.println("onEnd");
									}
								});
							}
						};
						thread.start();
					}
				}
			}
		});
		contentPanel.add(create);

		setContentPane(contentPanel);
		pack();
		setResizable(false);
	}

	public static void main(String[] args) {
		MainFrame frame = new MainFrame();
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setVisible(true);
	}
}
