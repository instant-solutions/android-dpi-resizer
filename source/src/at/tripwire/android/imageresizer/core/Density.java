package at.tripwire.android.imageresizer.core;

public class Density {

	public static final Density LDPI = new Density("ldpi", 0.75f);
	public static final Density MDPI = new Density("mdpi", 1f);
	public static final Density HDPI = new Density("hdpi", 1.5f);
	public static final Density XHDPI = new Density("xhdpi", 2f);
	public static final Density XXHDPI = new Density("xxhdpi", 3f);
	public static final Density TVDPI = new Density("tvdpi", 1.33f);
	
	private float factorToMdpi;
	private String name;
	
	public Density(String name, float factorToMdpi) {
		this.name = name;
		this.factorToMdpi = factorToMdpi;
	}
	
	public float getFactorToMdpi() {
		return factorToMdpi;
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Density)) {
			return false;
		}
		
		return factorToMdpi == ((Density)obj).factorToMdpi;
	}
}
