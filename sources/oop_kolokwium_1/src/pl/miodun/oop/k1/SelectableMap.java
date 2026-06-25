package pl.miodun.oop.k1;

public final class SelectableMap extends VoivodeshipMap {

	private String selected = null;

	public void select(final String voivodeship) {
		this.selected = voivodeship;
	}

	@Override
	public void saveToSvg(final String filePath) {
		if (this.selected != null) this.setColor(this.selected, 0xFFFFFF);
		super.saveToSvg(filePath);
	}

}
