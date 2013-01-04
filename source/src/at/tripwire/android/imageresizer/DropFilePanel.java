package at.tripwire.android.imageresizer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.painter.Painter;

@SuppressWarnings("serial")
public class DropFilePanel extends JXPanel {

	private static final Stroke border = new BasicStroke(6f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 15f, new float[] { 15f }, 0f);

	private List<File> elements;
	
	private String state;

	public DropFilePanel(final String name) {
		super();
		setDropTarget(new FileDropTarget());
		setOpaque(false);
		elements = new ArrayList<File>();
		state = "0 FILES";

		setBackgroundPainter(new Painter<JXPanel>() {
			@Override
			public void paint(Graphics2D g, JXPanel panel, int width, int height) {
				g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g.setStroke(border);
				g.setColor(Color.gray);
				g.drawRoundRect(5, 5, width - 10, height - 10, 40, 40);

				g.setFont(g.getFont().deriveFont(18f));

				FontMetrics metrics = g.getFontMetrics();
				int stringWidth = metrics.stringWidth(name);
				int x = getStringX(width, stringWidth);
				g.drawString(name, x, 50);
				
				g.setFont(g.getFont().deriveFont(12f));
				metrics = g.getFontMetrics();
				stringWidth = metrics.stringWidth(state);
				x = getStringX(width, stringWidth);
				g.drawString(state, x, 100);
			}
		});
	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(150, 150);
	}
	
	private int getStringX(int width, int stringWidth) {
		return (int) ((float) width - stringWidth) / 2;
	}
	
	public Collection<File> getElements() {
		return elements;
	}
	
	private class FileDropTarget extends DropTarget {

		@Override
		public synchronized void dragEnter(DropTargetDragEvent dtde) {
			state = "DROP FILES";
			repaint();
		}
		
		@Override
		public synchronized void dragExit(DropTargetEvent dte) {
			state = elements.size() + " FILES";
			repaint();
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public synchronized void drop(DropTargetDropEvent dtde) {
			dtde.acceptDrop(DnDConstants.ACTION_COPY);
			Transferable t = dtde.getTransferable();
			
			List<File> fileList;
			try {
				fileList = (List<File>) t.getTransferData(DataFlavor.javaFileListFlavor);
				for(File file : fileList) {
					if(file.getAbsolutePath().endsWith(".png")) {
						elements.add(file);
					}
				}
			} catch (UnsupportedFlavorException e) {
			} catch (IOException e) {
			}
			
			state = elements.size() + " FILES";
			repaint();
		}
	}
}
